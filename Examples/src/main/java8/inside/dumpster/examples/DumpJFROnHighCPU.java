/*
 * 
 */
package inside.dumpster.examples;

import java.io.File;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

/**
 * Example to show how to dump a JFR file when the CPU load is too high. 
 * A recording stream is used to monitor the CPULoad event and then trigger 
 * a JFR file to be written when the CPU load is above a certain level.
 * 
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class DumpJFROnHighCPU {
  public static final double CPU_LEVEL_THRESHOLD = 0.3;
  
  public static void main(String[] args) throws Exception {
    CPULoadMonitor cpuLoad = new CPULoadMonitorImpl();
    cpuLoad.monitor(cpuLevel -> cpuLevel > CPU_LEVEL_THRESHOLD, 
            () -> jfrDumpDestination());
  }

  private static final int DUMPINTERVAL_SECONDS = 60;
  
  /** Keeps track of last dumped JFR file. */
  private static Date lastJFRDump = new Date();
  
  /**
   * Returns an optional file to dump the JFR recording. 
   * @return 
   */
  private synchronized static Optional<File> jfrDumpDestination() {
    final Date now = new Date();
    if(now.after(lastJFRDump)) {
      final String tmpdir = System.getProperty("java.io.tmpdir");
      final String filename = new SimpleDateFormat("yyyyMMddHHmm").format(now);
      lastJFRDump = new Date(now.getTime() + DUMPINTERVAL_SECONDS * 1000);
      final File jfrFile = new File(tmpdir, filename + ".jfr");
      return Optional.of(jfrFile);
    } else {
      return Optional.empty();
    }
  }
}
