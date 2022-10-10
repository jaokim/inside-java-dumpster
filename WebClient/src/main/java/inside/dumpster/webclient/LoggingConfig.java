/*
 * 
 */
package inside.dumpster.webclient;

import java.io.InputStream;
import java.util.logging.*;

public class LoggingConfig {
    public LoggingConfig() {
//        try {
//            // Load a properties file from class path that way can't be achieved with java.util.logging.config.file
//            
//            final LogManager logManager = LogManager.getLogManager();
//            try (final InputStream is = getClass().getResourceAsStream("/logging.properties")) {
//                logManager.readConfiguration(is);
//            }
//            
//
//            // Programmatic configuration
//            System.setProperty("java.util.logging.SimpleFormatter.format",
//                    "%1$tY-%1$tm-%1$td %n");
////                    "%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS.%1$tL %4$-7s [%3$s] (%2$s) %5$s %6$s%n");
//
//            final ConsoleHandler consoleHandler = new ConsoleHandler();
//            consoleHandler.setLevel(Level.FINEST);
//            consoleHandler.setFormatter(new SimpleFormatter());
//
//            final Logger app = Logger.getLogger("WebClient");
//            app.setLevel(Level.FINEST);
//            app.setUseParentHandlers(false);
//            app.addHandler(consoleHandler);
//        } catch (Exception e) {
//            // The runtime won't show stack traces if the exception is thrown
//            e.printStackTrace();
//        }
    }
}