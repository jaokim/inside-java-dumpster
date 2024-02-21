/*
 *
 */
package inside.dumpster.client;

import inside.dumpster.client.impl.NetFlowData;
import inside.dumpster.client.arguments.CliArguments;
import inside.dumpster.client.impl.ParseLine;
import inside.dumpster.client.web.HttpPayload;
import inside.dumpster.client.web.HttpPayloadParseLine;
import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.stream.Stream;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class Cli {
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


  public static void main(String[] args) throws Exception {
    CliArguments arguments = new CliArguments(args);
    new Cli().start(arguments);
  }

  public void start(final CliArguments args) throws IOException {
    String address;
    NetFlowData data;
    if(args.Address.isSet()) {
      data = new NetFlowData<HttpPayload>(new HttpPayloadParseLine());

      address = args.Address.getValue();

      System.out.println("Starting webclient for endpoint: " + address);

    } else {
      data = new NetFlowData<>(new ParseLine<Payload>() {
        public Payload createPayload() {
          return new Payload();
        }
      });
      address = null;

    }

    Scheduler scheduler = new Scheduler(data.getFirsttime());
    scheduler.setBaseURI(address);

    Stream<Payload> stream = data.getStream();


    if(args.Filter.isSet()) {
      System.out.println("Filter requests: "+args.Filter.getValue());
      stream = stream.filter((Payload payload) -> {
        for (String filter : args.Filter.getValue().split(",")) {
          System.out.println("Comparing: "+filter+" with: "+payload.getDestination().name());
          return payload.getDestination().name().matches(filter);
        }
        return false;
      });
    }
    if(args.Limit.isSet()) {
      System.out.println("Limited requests: "+args.Limit.getInteger());
      stream = stream.limit(args.Limit.getInteger());
    }
    if(args.Duration.isSet()) {
      int runningtimeSeconds = args.Duration.getInteger();
      Duration duration = Duration.ofSeconds(runningtimeSeconds);
      scheduler.setDuration(duration);
      Thread t = new Thread (new Runnable() {
        @Override
        public void run() {
          try {
            Thread.sleep(duration.toMillis());
          } catch (InterruptedException ex) {
          }
          System.out.println("Okay, we've reached the end, scheduling for exit.");
          scheduler.scheduleForExit();
          //System.exit(0);
          //Runtime.getRuntime().halt(0);
        }
      });
      t.setDaemon(true);
      t.start();
    }


    if(args.DelayThreshold.isSet()) {
      scheduler.setDelayThreshold(args.DelayThreshold.getInteger());
    }
    if(args.Interactive.isTrue()) {
        stream = stream.filter((t) -> {
          System.out.println("Press enter for new req.");
          try {
            int ch = System.in.read();
            if ('q' == ch) {
                return false;
            }
          } catch (IOException ex) { }
          return true;
        });
    }
    stream.forEach(scheduler::scheduleRequest);

    scheduler.scheduleForExit();
  }
}
