/*
 *
 */
package inside.dumpster.credits;

import inside.dumpster.bl.BusinessLogicException;
import inside.dumpster.bl.BusinessLogicService;
import inside.dumpster.outside.Bug;
import inside.dumpster.outside.Buggy;
import java.util.concurrent.atomic.AtomicLong;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
@Buggy(because = "it takes up way too much CPU", enabled = true)
public class CreditsService extends BusinessLogicService<CreditsPayload, CreditsResult> {
  public CreditsService() {
    super(CreditsPayload.class, CreditsResult.class);
  }
  public CreditsService(Class<CreditsPayload> type, Class<CreditsResult> type1) {
    super(type, type1);
  }


  @Override
  public CreditsResult invoke(CreditsPayload payload) throws BusinessLogicException {

    CreditsResult res = new CreditsResult();
    if (Bug.isBuggy(this)) {
      if (payload.getSrcPort() != null && payload.getSrcPort().equals("Port06042")) {
        final Runnable cpuConsumerThread =new CPUConsumer();
        System.out.println("CCCCCCCCOOOOOOOOOONNNNNNNNSSSSSSUUUUUUUMMMMMMMEEEEEERRRRRRR");
        System.out.println("------------------------------------------------------------");
        System.out.println("------------------------------------------------------------");
        System.out.println("------------------------------------------------------------  ");
        for (int i = 0; i < 20; i++) {
          new Thread(cpuConsumerThread, "CreditCrunch "+i).start();
        }
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
