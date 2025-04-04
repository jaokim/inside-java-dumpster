/*
 */
package inside.dumpster.bidding.bid;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

/**
 *
 * @author jsnor
 */
public class BidProcessor implements Callable<BidResult> {
    private static final Logger logger = Logger.getLogger(BidProcessor.class.getName());
    private final Bidder bidder;
    private final List<Object> objects;
    
    public BidProcessor(Bidder bidder) {
        this.bidder = bidder;
        this.objects = new ArrayList<>();
        long finalBid = 7;
        for (int i = 0 ; i< 700_000; i++) {
            objects.add(new Object());
            Object o = objects.get(objects.size()-1);
            finalBid += o.hashCode() / objects.size();
//            finalBid *= Math.random();
            
        }        
    }

    @Override
    public BidResult call() throws Exception {
        
//        Database db = Backend.getInstance().getDatabase();
//        InputStream is = db.getTextData(bidder.toString());
//        System.out.println("Bidding calll got data");
//        ByteArrayOutputStream baos = Utils.inputStreamToOutputStream(is);
        long finalBid = 7;//baos.size();
        logger.info("Bidding call loop: "+bidder.id);
//        for (int i = 0 ; i< 700_000; i++) {
//            objects.add(new Object());
//            Object o = objects.get(objects.size()-1);
//            finalBid += o.hashCode() / objects.size();
////            finalBid *= Math.random();
//            
//        }
        try {
            if (finalBid > 10) {
                throw new IOException("Final bid");
            }
        } catch (IOException e) {
            
        }
//        for (Object o2 : objects) {
//                finalBid += o2.hashCode() / objects.size();
//                finalBid *= Math.random();
//            }
        logger.info("Bidding call loop done: "+bidder.id+" final bid: "+finalBid);
        final BidResult res = new BidResult(finalBid);
        
        return res;
    }
    
}
