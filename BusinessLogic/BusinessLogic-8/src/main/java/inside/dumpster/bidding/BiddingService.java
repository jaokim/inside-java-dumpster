/*
 *
 */
package inside.dumpster.bidding;

import inside.dumpster.bidding.bid.BidProcessor;
import inside.dumpster.bidding.bid.BidResult;
import inside.dumpster.bidding.bid.Bidder;
import inside.dumpster.bl.BusinessLogicException;
import inside.dumpster.bl.BusinessLogicService;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jsnor
 */
public class BiddingService extends BusinessLogicService<BiddingPayload, BiddingResult> {
    private static final Logger logger = Logger.getLogger(BiddingService.class.getName());
    public BiddingService(Class<BiddingPayload> type, Class<BiddingResult> type1) {
        super(type, type1);
    }

    public BiddingService() {
        super(BiddingPayload.class, BiddingResult.class);
    }

    class SimpleThreadFactory implements ThreadFactory {

        String name;
        AtomicInteger threadNo = new AtomicInteger(0);

        public SimpleThreadFactory(String name) {
            this.name = name;
        }

        public Thread newThread(Runnable r) {
            String threadName = name + ":" + threadNo.incrementAndGet();
            //System.out.println("threadName:"+threadName);
            return new Thread(r, threadName);
        }
    }

    @Override
    public BiddingResult invoke(BiddingPayload payload) throws BusinessLogicException {
        SimpleThreadFactory factory = new SimpleThreadFactory(payload.getTransactionId());
        final BiddingResult result = new BiddingResult();
        List<BidProcessor> bids = new ArrayList<>();
        List<Future<BidResult>> biddingResult = null;
        for(Bidder bidder : payload.getBidders()){
            bids.add(new BidProcessor(bidder));
        }
        logger.info("There are " + bids.size()+ " bidders.");

        int threadPoolSize = bids.size();
        ExecutorService executorService = Executors.newFixedThreadPool(threadPoolSize, factory);
        try {
            biddingResult = executorService.invokeAll(bids);
            
            
        } catch (InterruptedException ex) {
            Logger.getLogger(BiddingService.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            executorService.shutdown();
            while (!executorService.isTerminated()) {
                executorService.awaitTermination(5, TimeUnit.SECONDS);
            }
            if (biddingResult != null) {
                try {
                    BidResult res = biddingResult.stream().max((Future<BidResult> o1, Future<BidResult> o2) -> {
                        try {
                            return Long.compare(o1.get().getResult(), o2.get().getResult());
                        } catch (InterruptedException | ExecutionException ex) {
                            Logger.getLogger(BiddingService.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        return 0;
                    }).get().get();
                    result.setResult(res.toString() + " won with: "+res.getResult());
                } catch (ExecutionException ex) {
                    Logger.getLogger(BiddingService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        } catch (InterruptedException ex) {
            Logger.getLogger(BiddingService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return result;
    }

}
