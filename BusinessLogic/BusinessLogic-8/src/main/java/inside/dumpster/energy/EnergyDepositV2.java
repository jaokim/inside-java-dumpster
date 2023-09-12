/*
 *
 */
package inside.dumpster.energy;

import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class EnergyDepositV2 extends EnergyDeposit {

  public EnergyDepositV2(String port) {
    super(port + "_v2");
  }

  @Override
  public EnergyResult exchangeEnergy(EnergyPayload payload) {
    EnergyResult result = new EnergyResult(payload.getRequestedWattage());
    try {
      synchronized (depositLock) {
        deposit = deposit.add(BigDecimal.valueOf(payload.getIngoingWattage()));
      }
      synchronized (iterationsLock) {
        this.iterations = payload.getIterations();

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

  @Override
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

        }
        Thread.sleep(1);
        synchronized (iterationsLock) {
          iterationsLock.notify();
        }
        Thread.sleep(1);
      }
    } catch (InterruptedException ex) {
      Logger.getLogger(EnergyDeposit.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

}
