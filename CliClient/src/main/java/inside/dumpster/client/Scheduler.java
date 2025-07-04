/*
 *
 */
package inside.dumpster.client;

import inside.dumpster.client.cli.CreateJspFromRequestRunnable;
import inside.dumpster.client.cli.LocalRequestRunnable;
import inside.dumpster.client.web.*;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author JSNORDST
 */
public class Scheduler<P extends Payload> {

  private static final Logger logger = Logger.getLogger("WebClient");

  private static long totalCount = 0;
  private final static Object lock = new Object();
  void scheduleForExit() {
    threadPool.shutdown();
    while (!threadPool.isTerminated()) {
        System.out.println("Not terminated");
        try {
            Thread.sleep(Duration.ofSeconds(1).toMillis());
        } catch (InterruptedException ex) {
            Logger.getLogger(Scheduler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    Thread.dumpStack();
    System.out.println("Terminated.");
    System.exit(0);
    
  }
  ScheduledThreadPoolExecutor threadPool = new ScheduledThreadPoolExecutor(20);
  /** Start in milliseconds. */
  final long start;
  long count = 0;
  boolean debug;
  private final long timeForFirstRequest;
  private String baseURI;
  private String generateJspPageDestination;
  private long lastlogtime = -1;
  private Duration durationToPostRequests;
  private int delayThreshold = 100;
  final static AtomicLong LAST_REQUEST = new AtomicLong();
  public Scheduler(long firsttime) {
    this(null, firsttime, false);
  }

  public Scheduler(String baseUri, long firsttime) {
    this(baseUri, firsttime, false);
  }


  public void setBaseURI(String baseURI) {
    this.baseURI = baseURI;
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

  public void setDelayThreshold(int delayThreshold) {
    this.delayThreshold = delayThreshold;
  }

  public void setDuration(Duration duration) {
    this.durationToPostRequests = duration;
  }

  public void scheduleRequest(final Payload req) {
    final Runnable runner;

    if(debug) {
      runner = new Runnable() {
        @Override
        public void run() {
          System.out.println("Doing " + req);
        }
      };
    } else if (baseURI != null) {
      runner = new NetworkRequestRunnable(req, baseURI);
    } else if (generateJspPageDestination != null) {
      runner = new CreateJspFromRequestRunnable(req, Path.of(generateJspPageDestination));
    } else {
      runner = new LocalRequestRunnable(req);
    }

    if(lastlogtime == -1) {
      lastlogtime = req.getTime() * 1000;
    }
    final long timeForThisRequest = req.getTime() * 1000;
    final long now = System.nanoTime() / 1_000_000;


    final long delayFromFirstRequest = timeForThisRequest - timeForFirstRequest;
    long delayFromStart = now - start;
    long delay = delayFromFirstRequest - delayFromStart;
    delay = delay / delayThreshold;

    lastlogtime = req.getTime();

    long time = now + (delay);
    if (!threadPool.isShutdown()) {
      ScheduledFuture future = threadPool.schedule(runner, delay, TimeUnit.MILLISECONDS);
      LAST_REQUEST.incrementAndGet();
    }
    Duration currentDuration = Duration.ofMillis(delayFromStart);

    count++;
    int thrCount = threadPool.getQueue().size();
    synchronized (lock) {
      // pause if there are too many requests scheduled
      while (thrCount >= 200) {
        if (threadPool.isShutdown()) {
          System.out.println("Shutting down");
          System.out.println(threadPool.toString());

          threadPool.shutdownNow();
          break;
        }
        System.out.println(" "+(threadPool.isShutdown()?"in shutdown":"not in shutdown")+ " "+threadPool.toString());
        try {
          // if there's  a duration limit, we want to cancel
          if(durationToPostRequests != null) {
            if(durationToPostRequests.minus(currentDuration).isNegative()) {
              logger.warning("The time to end has come, not processing any more log lines.");
              threadPool.awaitTermination(10, TimeUnit.SECONDS);
              List leftRunnables = threadPool.shutdownNow();
              logger.warning(String.format("There were %d requests posted that weren't executed", leftRunnables.size()));
              logger.info(String.format("There were %d requests made during the %d s duration", count, currentDuration.getSeconds()));
              //System.exit(0);
              break;
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
    logger.fine(String.format("Queue: %d, %s, duration left: %d s", threadPool.getQueue().size(), sdf.format(new Date(time)), durationToPostRequests != null ? durationToPostRequests.minus(currentDuration).getSeconds(): -1));
//    logger.info("Queue: " + sdf.format(new Date(timeForThisRequest)) + ", next: " + sdf.format(new Date(time))+ ", exper: " + sdf.format(new Date(now + myDelay)) + ", delay: "+delay + ", q.size: "+ threadPool.getQueue().size() + ", total count: " + ++totalCount +
//            ", nt:"+ sdf.format(new Date(newtime)) + ", nd: "+newDelay);
  }

}
