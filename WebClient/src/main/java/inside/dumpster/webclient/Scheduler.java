/*
 * 
 */
package inside.dumpster.webclient;

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
public class Scheduler {

  private static final Logger logger = Logger.getLogger(PostRequest.class.getName());

  static long totalCount = 0;
  final static Object lock = new Object();
  ScheduledThreadPoolExecutor threadPool = new ScheduledThreadPoolExecutor(20);
  final long start = System.currentTimeMillis();
  long count = 0;
  boolean debug;
  private final long firsttimeFromLog;
  private static String baseURI;

  public Scheduler(String baseUri, long firsttime) {
    baseURI = baseUri;
    this.firsttimeFromLog = firsttime;
  }

  public Scheduler(String baseUri, long firsttime, boolean debug) {
    this.firsttimeFromLog = firsttime;
    baseURI = baseUri;
    this.debug = debug;
  }

  static String log(String s) {
    System.out.println(s);
    return s;
  }

  public void scheduleNetworkRequest(final HttpPayload req) {
    req.setBaseURI(baseURI);
    long delayFromFirstLog = req.getTime() - firsttimeFromLog;
    long now = System.currentTimeMillis();

    long offset = now - start;

    long delay = offset - delayFromFirstLog;

    //System.out.println(String.format("dff: %d offset: %d  delay: %d", delayFromFirstLog, offset, delay));
    delay = delay / 10;
    long time = System.currentTimeMillis() + (delay);
    Runnable runner;
    if (!debug) {
      runner = new NetworkRequestRunnable(req);
    } else {
      runner = new Runnable() {
        @Override
        public void run() {
          System.out.println("Doing " + req);
        }
      };
    }

    ScheduledFuture future = threadPool.schedule(runner, delay, TimeUnit.MILLISECONDS);

    // if (count % 100 == 0) {
    System.out.println(future.toString() + "Scheduled for: " + delay + " -> " + new Date(time) + ", now: " + new Date().toString());
    //}
    count++;
    int thrCount = threadPool.getQueue().size();
    synchronized (lock) {
      while (thrCount >= 50) {
        try {
          lock.wait(200);
          thrCount = threadPool.getQueue().size();
        } catch (InterruptedException ex) {
          Logger.getLogger(PerformRequests.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    }
    logger.info("Queue:" + threadPool.getQueue().size() + ", total count: " + ++totalCount);
//        threadPool.shutdown();
  }

//    static long calculateDelay(long startOfTime, long timestamp, TimeUnit timeUnit) {
//        long delay;
//        //long now = System.nanoTime();
//        switch(timeUnit) {
//            case NANOSECONDS:
//                delay = (timestamp - startOfTime)/1000;
//                break;
//            default:
//                throw new IllegalStateException("Unsupported timeunit: "+timeUnit.name());
//        }
//        return delay;
//    }
}
