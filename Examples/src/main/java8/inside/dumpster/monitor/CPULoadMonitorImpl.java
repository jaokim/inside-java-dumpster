/*
 * 
 */
package inside.dumpster.monitor;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.DoublePredicate;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.MBeanServer;
import javax.management.ObjectName;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class CPULoadMonitorImpl implements CPULoadMonitor {
  private final MBeanServer mrBean = ManagementFactory.getPlatformMBeanServer();

  /**
   * Monitors the CPU load every 5 seconds. If the cpuLevelTestewr returns true,
   * a JFR recording is dumped.
   * @param cpuLevelTester returns true to dump a JFR recording.
   * @param jfrRecordingDestination supplies a file if a recording should be dumped
   * @throws Exception 
   */
  @Override
  public void monitor(DoublePredicate cpuLevelTester, Supplier<Optional<File>> jfrRecordingDestination) throws Exception {
    System.out.println("Adding JDK8 type CPU level monitor");
    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    executor.scheduleAtFixedRate(()->{
      try {
        double cpuLoad = getProcessCpuLoad();
        if (cpuLevelTester.test( cpuLoad )) {
          System.out.println("High CPU level noticed: "+cpuLoad);
          Optional<File> jfrFileDestination = jfrRecordingDestination.get();
          if(jfrFileDestination.isPresent()) {
            dumpJFR(jfrFileDestination.get());
          }
        } else {
          System.out.println("CPU level ok: "+cpuLoad);
        }
      } catch (Exception ex) {
        Logger.getLogger(CPULoadMonitorImpl.class.getName()).log(Level.SEVERE, null, ex);
      }
    }, 0, 5, TimeUnit.SECONDS);
  }

  
  /**
   * Dump a JFR recording to file.
   * @param jfrFile the file to dump recording to.
   * @throws Exception 
   */
  private void dumpJFR(File jfrFile) throws Exception {
    final String[] signature = {"[Ljava.lang.String;"};
    final ObjectName name = ObjectName.getInstance("com.sun.management:type=DiagnosticCommand");
    final Object[] params = new Object[1];
    params[0] = new String[]{"name=1", "filename="+jfrFile};
    Object res = mrBean.invoke(name, "jfrDump", params, signature);
    if(res != null) { 
      System.out.println("Dumped JFR recording to: "+jfrFile.getAbsolutePath());
    } else {
      System.out.println("Result: "+res);
    }
  }
  
  /**
   * Get the System CPU load. The CPU load is retrieved using managed beans.
   * @return
   * @throws Exception 
   */
  private double getProcessCpuLoad() throws Exception {
    final ObjectName name    = ObjectName.getInstance("java.lang:type=OperatingSystem");
    // Get wanted attribute; ProcessCpuLoad or SystemCpuLoad
    final Double cpuLevel = (double)mrBean.getAttribute(name, "SystemCpuLoad");
    return cpuLevel;
  }

}
