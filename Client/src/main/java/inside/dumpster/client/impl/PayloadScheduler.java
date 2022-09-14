/*
 * 
 */
package inside.dumpster.client.impl;

import inside.dumpster.client.Payload;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author JSNORDST
 */
public class PayloadScheduler<P extends Payload> {

  private static final Logger logger = Logger.getLogger(PayloadScheduler.class.getName());

  static long totalCount = 0;
  final static Object lock = new Object();
  ScheduledThreadPoolExecutor threadPool = new ScheduledThreadPoolExecutor(20);
  final long start = System.currentTimeMillis();
  long count = 0;
  boolean debug;
  private long lastTime = -1;
  private final long firsttimeFromLog;
  private final PayloadProcessor payloadProcessor;
  public PayloadScheduler(long firsttime, PayloadProcessor payloadProcessor) {
    this.firsttimeFromLog = firsttime;
    this.payloadProcessor = payloadProcessor;
  }

  public PayloadScheduler(long firsttime, PayloadProcessor payloadProcessor, boolean debug) {
    this.firsttimeFromLog = firsttime;
    this.debug = debug;
    this.payloadProcessor = payloadProcessor;
  }

  static String log(String s) {
    System.out.println(s);
    return s;
  }

  public void scheduleRequest(final P req) {
    long delay;
    if(lastTime == -1) {
      delay = 0;
    } else {
      delay = req.getTime() - lastTime;
    }
    lastTime = req.getTime();
//    long delayFromFirstLog = req.getTime() - firsttimeFromLog;
//    long now = System.currentTimeMillis();
//
//    long offset = now - start;
//
//    long delay = offset - delayFromFirstLog;

//    delay = delay / 100;
    long time = System.currentTimeMillis() + (delay);
    
    ScheduledFuture future = threadPool.schedule(() -> {
      try {
        payloadProcessor.processPayload(req);
      } catch(Exception e) {
        e.printStackTrace();
      }
    }, delay, TimeUnit.MILLISECONDS);

//    if (count % 100 == 0) {
      //System.out.println("Scheduled for: " + delay + " -> " + new Date(time) + ", now: " + new Date().toString());
//    }
    count++;
    int thrCount = threadPool.getQueue().size();
    synchronized (lock) {
      while (thrCount >= 50) {
        try {
          lock.wait(200);
          thrCount = threadPool.getQueue().size();
        } catch (InterruptedException ex) {
          Logger.getLogger(PayloadScheduler.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    }
//    logger.info("Queue:" + threadPool.getQueue().size() + ", total count: " + ++totalCount);
//        threadPool.shutdown();
  }

}
