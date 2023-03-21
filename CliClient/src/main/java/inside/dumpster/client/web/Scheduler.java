/*
 *
 */
package inside.dumpster.client.web;

import inside.dumpster.client.Payload;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author JSNORDST
 */
@Deprecated
public class Scheduler {

  private static final Logger logger = Logger.getLogger("WebClient");

  private static long totalCount = 0;
  private final static Object lock = new Object();
  ScheduledThreadPoolExecutor threadPool = new ScheduledThreadPoolExecutor(20);
  /** Start in milliseconds. */
  final long start;
  long count = 0;
  boolean debug;
  private final long timeForFirstRequest;
  private final String baseURI;
  private long lastlogtime = -1;
  private Duration durationToPostRequests;
  public Scheduler(String baseUri, long firsttime) {
    this(baseUri, firsttime, false);
  }

  public Scheduler(String baseUri, long firsttime, boolean debug) {
    this.timeForFirstRequest = firsttime * 1000;
    this.baseURI = baseUri;
    this.debug = debug;
    this.start = System.nanoTime() / 1_000_000;
    if(durationToPostRequests != null) {
      logger.info("Running for a duration of "+durationToPostRequests.getSeconds() + "s");
    }
  }


  public void setDuration(Duration duration) {
    this.durationToPostRequests = duration;
  }

  public void scheduleNetworkRequest(final Payload req) {
    final Runnable runner;
//    req.setBaseURI(baseURI);

    if (!debug) {
      runner = new NetworkRequestRunnable(req, baseURI);
    } else {
      runner = new Runnable() {
        @Override
        public void run() {
          System.out.println("Doing " + req);
        }
      };
    }

    if(lastlogtime == -1) {
      lastlogtime = req.getTime() * 1000;
    }
    final long timeForThisRequest = req.getTime() * 1000;
    final long now = System.nanoTime() / 1_000_000;


    final long delayFromFirstRequest = timeForThisRequest - timeForFirstRequest;
    final long fromNow = delayFromFirstRequest + start;
    long delayFromStart = now - start;
    long delay = delayFromFirstRequest - delayFromStart;
    delay = delay / 100;

    lastlogtime = req.getTime();

    long time = now + (delay);
    ScheduledFuture future = threadPool.schedule(runner, delay, TimeUnit.MILLISECONDS);
    Duration currentDuration = Duration.ofMillis(delayFromStart);

    count++;
    int thrCount = threadPool.getQueue().size();
    synchronized (lock) {
      while (thrCount >= 50) {
        try {
          if(durationToPostRequests != null) {
            if(durationToPostRequests.minus(currentDuration).isNegative()) {
              logger.warning("The time to end has come, not processing more log lines.");
              threadPool.awaitTermination(30, TimeUnit.SECONDS);
              List leftRunnables = threadPool.shutdownNow();
              logger.warning(String.format("There were %d requests posted that won't be executed", leftRunnables.size()));
              logger.info(String.format("There were %d requests made during the %d s duration", count, currentDuration.getSeconds()));
              System.exit(0);
            }
          }
          lock.wait(200);
          thrCount = threadPool.getQueue().size();
        } catch (InterruptedException ex) {
          logger.log(Level.SEVERE, null, ex);
        }
      }
    }
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    logger.warning(String.format("Queue: %d, last req scheduled for: %s, duration left: %d s", threadPool.getQueue().size(), sdf.format(new Date(time)), durationToPostRequests != null ? durationToPostRequests.minus(currentDuration).getSeconds(): -1));
//    logger.info("Queue: " + sdf.format(new Date(timeForThisRequest)) + ", next: " + sdf.format(new Date(time))+ ", exper: " + sdf.format(new Date(now + myDelay)) + ", delay: "+delay + ", q.size: "+ threadPool.getQueue().size() + ", total count: " + ++totalCount +
//            ", nt:"+ sdf.format(new Date(newtime)) + ", nd: "+newDelay);
  }
}
