/*
 * 
 */
package inside.dumpster.monitor;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.function.DoublePredicate;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import jdk.jfr.Configuration;
import jdk.jfr.consumer.RecordingStream;

/**
 * Example to show how to dump a JFR file when the CPU load is too high. 
 * A recording stream is used to monitor the CPULoad event and then trigger 
 * a JFR file to be written when the CPU load is above a certain level.
 * 
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class CPULoadMonitorImpl implements CPULoadMonitor {
  
  /**
   * Monitors the JFR CPU load event. The event is triggered based on JFR
   * configuration.
   * @param cpuLevelTester
   * @param jfrRecordingDestination
   * @throws Exception 
   */
  @Override
  public void monitor(DoublePredicate cpuLevelTester, Supplier<Optional<File>> jfrRecordingDestination) throws Exception {
    System.out.println("Adding JDK17 type CPU level monitor");
    Configuration c = Configuration.getConfiguration("default");
    // create a recording stream using the "default" JFC configuration
    try (RecordingStream stream = new RecordingStream(c)) {

      stream.onEvent("jdk.CPULoad", (event) -> {
        // get CPU measurement: "jvmSystem", "jvmUser" or "machineTotal"
        float cpuLoad = event.getFloat("machineTotal");

        if(cpuLevelTester.test(cpuLoad)) {
          System.out.println("High CPU level noticed: "+cpuLoad);
          Optional<File> jfrFileDestination = jfrRecordingDestination.get();
          jfrFileDestination.ifPresent((file -> {
            try {
              stream.dump(file.toPath());
              System.out.println("Dumped JFR recording to: "+file.getAbsolutePath());
            } catch (IOException ex) {
              Logger.getLogger(CPULoadMonitorImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
          }));
        } else {
          System.out.println("CPU level ok: "+cpuLoad);
        }
      });

      stream.start();
    }
  }
  
  
}
