/*
 * 
 */
package inside.dumpster.client.web;

import inside.dumpster.client.impl.NetFlowData;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author JSNORDST
 */
public class PerformRequests {
  static {
    ConsoleHandler handler = new ConsoleHandler();
    handler.setFormatter(new SimpleFormatter() {
      private static final String format = "[%1$tF %1$tT] [%2$-7s] %3$s%n";
      @Override
      public String formatMessage(LogRecord record) {
        return String.format(format,
            new Date(record.getMillis()),
            record.getLevel().getLocalizedName(),
            record.getMessage()
        );
        
      }

      @Override
      public String format(LogRecord record) {
        return formatMessage(record); 
      }

    });
    Logger logger = Logger.getLogger("WebClient");
    logger.setUseParentHandlers(false);
    for(Handler h : logger.getHandlers()) {
      logger.removeHandler(h);
    }
    logger.addHandler(handler);
  }
  public static void main(String[] args) throws FileNotFoundException, InterruptedException, IOException {
    new PerformRequests().start(args);
  }

  public void start(String[] args) throws FileNotFoundException, InterruptedException, IOException {
    int port = args.length >= 2 ? Integer.parseInt(args[1]) : 8081;
    final String address = String.format("http://localhost:%d/", port);
    System.out.println("Starting webclient for endpoint: " + address);
    
    NetFlowData<HttpPayload> data = new NetFlowData<>(new HttpPayloadParseLine());
    Scheduler scheduler = new Scheduler(address, data.getFirsttime(), false);
    if(args.length >= 1) {
      int runningtimeSeconds = Integer.parseInt(args[0]);
      Duration duration = Duration.ofSeconds(runningtimeSeconds);
      scheduler.setDuration(duration);
    }
    data.getStream()
            .forEach(scheduler::scheduleNetworkRequest);

  }

}
