/*
 *
 */
package inside.dumpster.energy;

import inside.dumpster.bl.BusinessLogicException;
import inside.dumpster.bl.BusinessLogicService;
import inside.dumpster.outside.Bug;
import inside.dumpster.util.ThreadHelper;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class EnergyService extends BusinessLogicService<EnergyPayload, EnergyResult> {
  private static final Map<String, EnergyDeposit> threads = new HashMap<String, EnergyDeposit>();
  public static boolean EXIT_EARLY = true;
  private static void log(String mess) {
    System.out.println(Thread.currentThread().getName() + " "+mess);
  }
  class EnergyDeposit implements Runnable {
    final Queue<EnergyPayload> payloads = new LinkedTransferQueue<>();
    final Queue<EnergyResult> results = new LinkedTransferQueue<>();
    final ReadWriteLock payloadsLock;
    final Condition payloadCondition;
    final Condition resultCondition;
    public EnergyDeposit() {
      payloadsLock = new ReentrantReadWriteLock();
      payloadCondition = payloadsLock.writeLock().newCondition();
      resultCondition = payloadsLock.writeLock().newCondition();
      log("Starting energy deposit thread");
    }

    private EnergyDeposit(EnergyPayload payload) {
      this();

    }


    public EnergyResult getResult() throws BusinessLogicException {
      Lock lock;
      if (Bug.isBuggy(this)) {
        lock = payloadsLock.writeLock();
      } else {
        lock = payloadsLock.writeLock();
      }
      try {
        lock.lock();
        if(EXIT_EARLY) {
          log("Awaiting results for 1 us");
          resultCondition.await(1, TimeUnit.SECONDS);
        } else {
          log("Awaiting results..");
          resultCondition.await();
        }
        return results.poll();
      } catch (InterruptedException ex) {
        throw new BusinessLogicException("Awaiting results interrupted", ex);
      } finally {
        lock.unlock();
      }
    }


    @Override
    public void run() {
      log("payloadsLock.readLock()");
      Lock payloadRead = payloadsLock.writeLock();
      while(true) {
        Double v;

        long start = System.currentTimeMillis();

        try {
          payloadRead.lock();
          if(payloads.isEmpty()) {
            payloadCondition.await();
          }
          EnergyPayload payload;
          log("Awaiting payload cond");
          payload = payloads.remove();

          int dur = 100000;
          if(payload.getDuration() != null) {
            dur = Integer.parseInt(payload.getDuration()) * 10000;
          }

          int duration = payload.getDuration() != null ? Integer.parseInt(payload.getDuration()) : 1000;
          log("Starting energy calc for "+duration+" ms "+ "("+payload.getDuration()+")");
         // Thread.sleep(duration);
//          v = new WindPowerCalculator().Borwein(dur);
v=0.9;
          log("");
          if (fw != null ) {try {fw.append("energyLoad: " + v);} catch (IOException ex) { }}

          final EnergyResult result = new EnergyResult();
          result.setResult("energyLoad of "+duration +"ms gave: "+v);

          results.add(result);
          log("We have a result");
          resultCondition.signal();


        } catch (InterruptedException ex) {
          Logger.getLogger(EnergyService.class.getName()).log(Level.SEVERE, null, ex);
          v = 0.0;
        } finally {
          log("payloadsLock.unLock(), after " + (System.currentTimeMillis() - start));
          payloadRead.unlock();
        }

//        final EnergyResult result = new EnergyResult();
//        result.setResult("energyLoad: "+v);
//
//        log("Adding res: "+result.getResult() + " to results");
//        Lock r = payloadsLock.writeLock();
//        try {
//          r.lock();
//
//          results.add(result);
//          resultCondition.signal();
//        } finally {
//          r.unlock();
//        }
      }
    }

    private void addPayload(EnergyPayload payload) {
      Lock writeLock = payloadsLock.writeLock();
      try {
        log(payload.getDestination().toString() + " Waiting for payload write lock");
        writeLock.lock();
        payloads.add(payload);
        log(payload.getDestination().toString() + "Signalling payload cond");
        payloadCondition.signal();
      } finally {
        log(payload.getDestination().toString() + "Done with payload write lock");
        writeLock.unlock();
      }
    }
  }


  private File file;
  private FileWriter fw;
  public EnergyService(Class<EnergyPayload> payloadClass, Class<EnergyResult> resultClass) {
    super(payloadClass, resultClass);
  }



  @Override
  public EnergyResult invoke(EnergyPayload payload) throws BusinessLogicException {
    EnergyDeposit deposit = getEnergyDepoist(payload);

    deposit.addPayload(payload);

    final EnergyResult result = deposit.getResult();
    return result;
  }

  @Override
  protected void finalize() throws Throwable {
  }


  private EnergyDeposit getEnergyDepoist(EnergyPayload payload) {
    final String key = payload.getDstPort() != null ? (!payload.getDstPort().startsWith("Port") ? "Port"+payload.getDstPort():payload.getDstPort()) : "spare";
    final EnergyDeposit deposit;
    synchronized(threads) {
      if(threads.containsKey(key)) {
        deposit = threads.get(key);
      } else {
        deposit = new EnergyDeposit(payload);
        Thread t = ThreadHelper.createThread(deposit, key);
        threads.put(key, deposit);
        log("Starting new energy deposit thread");
        t.start();
      }
    }
    return deposit;
  }
}
