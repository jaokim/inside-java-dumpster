/*
 *
 */
package inside.dumpster.energy;

import inside.dumpster.bl.BusinessLogicException;
import inside.dumpster.bl.BusinessLogicService;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class EnergyService extends BusinessLogicService<EnergyPayload, EnergyResult> {
  private static final Logger logger = Logger.getLogger(EnergyService.class.getName());

  private class EnergyThread implements Runnable {
    final EnergyDeposit deposit;
    private EnergyResult result;
    final EnergyPayload payload;

    public EnergyThread(EnergyPayload payload) {
      this.payload = payload;
      deposit = EnergyDeposit.getEnergyDeposit(payload.getDstPort());
      result = new EnergyResult(payload.getRequestedWattage());
      logger.info(String.format("EnergyThread: %s done, result: %s", getThreadName(), result.toString()));
    }

    final String getThreadName() {
      return "EnergyThread "+payload.getSrcPort() + "_"+payload.getTransactionId();
    }

    @Override
    public void run() {
      this.result = deposit.exchangeEnergy(payload);
      synchronized (this) {
        logger.info("Notifying all EnergyThreads");
        this.notifyAll();
      }
    }
  }

  public EnergyService(Class<EnergyPayload> payloadClass, Class<EnergyResult> resultClass) {
    super(payloadClass, resultClass);
  }

  @Override
  public EnergyResult invoke(EnergyPayload payload) throws BusinessLogicException {
    final EnergyThread energyThread = new EnergyThread(payload);

    synchronized(energyThread) {
      Thread t = new Thread(energyThread, energyThread.getThreadName());
      t.start();
      try {
        // the energy threads can be time consuming, allow for early bailout
        energyThread.wait(40000);
      } catch (InterruptedException ex) {
        logger.log(Level.SEVERE, "Interrupted waiting for EnergyThread: " + energyThread.getThreadName(), ex);
      }
    }
    logger.info(String.format("Returning result from EnergyThread %s, result: %s", energyThread.getThreadName(), energyThread.result));
    return energyThread.result;
  }
}
