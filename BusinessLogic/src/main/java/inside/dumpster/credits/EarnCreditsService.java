/*
 * 
 */
package inside.dumpster.credits;

import inside.dumpster.bl.BusinessLogicException;
import inside.dumpster.bl.BusinessLogicService;
import inside.dumpster.outside.Buggy;
import java.util.concurrent.atomic.AtomicLong;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
@Buggy(because = "")
public class EarnCreditsService extends BusinessLogicService<EarnCreditsPayload, EarnCreditsResult> {
  public EarnCreditsService(Class<EarnCreditsPayload> type, Class<EarnCreditsResult> type1) {
    super(type, type1);
  }
  
  
  @Override
  public EarnCreditsResult invoke(EarnCreditsPayload payload) throws BusinessLogicException {
    
    EarnCreditsResult res = new EarnCreditsResult();
    if(payload.getSrcPort() != null && payload.getSrcPort().equals("Port06042")) {
      final Runnable cpuConsumerThread =new CPUConsumer();
      System.out.println("CCCCCCCCOOOOOOOOOONNNNNNNNSSSSSSUUUUUUUMMMMMMMEEEEEERRRRRRR");
      System.out.println("------------------------------------------------------------");
      System.out.println("------------------------------------------------------------");
      System.out.println("------------------------------------------------------------  ");
      for(int i = 0; i < 20; i++) {
        new Thread(cpuConsumerThread, "CreditCrunch "+i).start();
      }
    }
    return res;
  }
  
  
  
  private static class CPUConsumer implements Runnable {

    @Override
    public void run() {
      final AtomicLong credit = new AtomicLong();
      try {
        System.out.println("Starting credit crunching thread");
        long start = System.currentTimeMillis();
        long then = start;
        while ((then - start) < 15_000) {
          double res = Math.random();
          then = System.currentTimeMillis();
          res = Math.IEEEremainder(res, Math.random());
          credit.addAndGet(Double.doubleToLongBits(res));
           
        }
      //} catch (InterruptedException e) {
        //System.out.println("CPU consuming thread was interrupted");
      } finally { 
        System.out.println("Got :"+credit.toString()+ " credits");
      }
    }
  }
  
}
