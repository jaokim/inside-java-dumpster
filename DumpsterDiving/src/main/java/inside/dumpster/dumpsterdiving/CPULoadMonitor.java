/*
 *
 */
package inside.dumpster.dumpsterdiving;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import jdk.jfr.Configuration;
import jdk.jfr.consumer.RecordingStream;
import jdk.management.jfr.RemoteRecordingStream;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class CPULoadMonitor {
  private static String user = "dumpsterdiver", pwd =  "dumpster";
  public static void main(String[] args) throws Exception {
    Path dumpPath = new File(args[0]).toPath();
    if(args.length > 1) {
      String host = args[1];
      System.out.println("Starting remote monitor: "+host);
      CPULoadMonitor monitor = new CPULoadMonitor(host, dumpPath);
    } else {
        System.out.println("Starting local monitor");
        CPULoadMonitor monitor = new CPULoadMonitor(dumpPath);
    }
  }


  private CPULoadMonitor(Path dumpPath) throws Exception {
    Configuration conf = Configuration.getConfiguration("default");
    try (var stream = new RecordingStream()) {
      stream.enable("jdk.CPULoad").withPeriod(Duration.ofSeconds(1));
      stream.onEvent("jdk.CPULoad", event -> {
        float cpuLoad = event.getFloat("jvmUser");
        System.out.printf("CPU load: %f\n", cpuLoad);
        if (cpuLoad > 0.8) {
          if (!dumpPath.toFile().exists()) {
            try {
              stream.dump(dumpPath);
            } catch (IOException ex) {
              ex.printStackTrace();
            }
          }
        }
      });
      stream.start();
    }
  }

  private CPULoadMonitor(String host, Path dumpPath) throws Exception {

    JMXServiceURL u = new JMXServiceURL(
            "service:jmx:rmi:///jndi/rmi://" + host + "/jmxrmi");
    JMXConnector c = JMXConnectorFactory.connect(u,
            Map.of("jmx.remote.credentials", new String[]{user, pwd}));
    MBeanServerConnection conn = c.getMBeanServerConnection();

    try (var stream = new RemoteRecordingStream(conn)) {
      stream.setSettings(Collections.EMPTY_MAP);
      stream.setMaxAge(Duration.ofMinutes(10));
      stream.enable("jdk.CPULoad").withPeriod(Duration.ofSeconds(1));
      final AtomicInteger counter = new AtomicInteger();
      stream.onEvent("jdk.CPULoad", event -> {
        float cpuLoad = event.getFloat("jvmUser");
        System.out.printf("CPU load (%d): %f\n", counter.get(), cpuLoad);
        if (cpuLoad > 0.4) {
          int val = counter.addAndGet(1);
          if(val > 7) {
            File f = new File(dumpPath.toFile(), String.format("highcpu_%d.jfr",System.currentTimeMillis()));
            System.out.println("Dumping to: "+f.getAbsolutePath());
            try {
              stream.dump(f.toPath());
            } catch (IOException ex) {
              ex.printStackTrace();
            }

            counter.set(0);
          }
        }
      });
      stream.start();
    }
  }
}
