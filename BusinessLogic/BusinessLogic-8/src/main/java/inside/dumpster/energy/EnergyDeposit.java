/*
 *
 */
package inside.dumpster.energy;

import inside.dumpster.outside.Bug;
import inside.dumpster.outside.Buggy;
import inside.dumpster.util.ThreadHelper;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
@Buggy(because = "it has a deadlock", enabled = true)
public class EnergyDeposit implements Runnable {
  private static final Logger logger = Logger.getLogger(EnergyDeposit.class.getName());
  private static final Map<String, EnergyDepositThread> threads = new HashMap<>();
  protected BigDecimal deposit = new BigDecimal(0);
  protected final DepositLock depositLock = new DepositLock();
  protected final IterationsLock iterationsLock = new IterationsLock();
  private final String port;
  protected long iterations;

  protected static String getPortName(String destPort) {
    final String key;
    key = destPort != null ? (!destPort.startsWith("Port") ? "Port"+destPort:destPort) : "spare";
    return key;
  }

  private static class EnergyDepositThread {
    public EnergyDepositThread(Thread thread, EnergyDeposit deposit) {
      this.thread = thread;
      this.deposit = deposit;
    }
    Thread thread;
    EnergyDeposit deposit;
  }

  public static EnergyDeposit getEnergyDeposit(String destPort) {
    final EnergyDeposit deposit;
    final String key = getPortName(destPort);
    synchronized(threads) {
      if(threads.containsKey(key)) {
        logger.log(Level.INFO, "Re-using energy deposit thread "+key);
        deposit = threads.get(key).deposit;
      } else {
        if (Bug.isBuggy(EnergyDeposit.class)) {
        // EnergyDepsoit is the buggy one
        deposit = new EnergyDeposit(destPort);
      } else {
        // this is the not buggy version, and the bug is not enabled
        deposit = new EnergyDepositV2(destPort);
      }
        Thread t = ThreadHelper.createThread(deposit, key);
        threads.put(key, new EnergyDepositThread(t, deposit));
        threads.notifyAll();

        logger.log(Level.INFO, "Starting energy deposit thread "+key);
        t.start();
      }
    }


    return deposit;
  }


  protected EnergyDeposit(String port) {
    this.port = port;
  }

  protected static class DepositLock { };
  protected static class IterationsLock { };


  public EnergyResult exchangeEnergy(EnergyPayload payload) {
    EnergyResult result = new EnergyResult(payload.getRequestedWattage());
    try {

      synchronized (iterationsLock) {
        this.iterations = payload.getIterations();
        synchronized (depositLock) {
          deposit = deposit.add(BigDecimal.valueOf(payload.getIngoingWattage()));
        }
        iterationsLock.notify();
      }


      while (!result.isReached()) {
        int wattageTaken = 0;
        synchronized (depositLock) {
          while (!result.isReached()) {
            if (deposit.compareTo(BigDecimal.ONE) < 0) {
              depositLock.wait();

            } else {
              deposit = deposit.subtract(BigDecimal.ONE);
              result.addWattage(1);

              wattageTaken++;
              if (wattageTaken >= payload.getRequestedWattage() * 0.1) {
                // let others take some deposit
                depositLock.notifyAll();
                break;
              }
            }
          }
        }
        Thread.sleep(10);
      }

    } catch (InterruptedException ex) {
      Logger.getLogger(EnergyDeposit.class.getName()).log(Level.SEVERE, null, ex);
    }

    return result;
  }

  protected void doDepositRun() {
    try {
      while (true) {
        double initialDeposit;
        double depositBonus;
        final long localIterations;
        synchronized (iterationsLock) {
          while (iterations == 0) {

            iterationsLock.wait();
          }
          localIterations = iterations;
          iterations = 0;
          initialDeposit = deposit.doubleValue();
          depositBonus = calculateDepositBonus(initialDeposit, localIterations);
        }
        
        synchronized (depositLock) {
          deposit = /*deposit.add*/(new BigDecimal(depositBonus));
          depositLock.notify();

          Thread.sleep(1);

          synchronized (iterationsLock) {
            iterationsLock.notify();
          }
        }
        Thread.sleep(1);
      }
    } catch (InterruptedException ex) {
      Logger.getLogger(EnergyDeposit.class.getName()).log(Level.SEVERE, null, ex);
    }
  }


  public void setIterations(long iterations) {
    this.iterations = iterations;
  }

  @Override
  public void run() {
      doDepositRun();
  }



  protected final double calculateDepositBonus(double deposit, long iterations) {
    long start = System.currentTimeMillis();
    for (long i = 0; i < iterations; i++) {
      deposit *= 1.00001;
    }
    long now = System.currentTimeMillis() - start;

    return deposit;
  }


}
