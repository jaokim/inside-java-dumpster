/*
 * 
 */
package inside.dumpster.monitor;

import java.io.File;
import java.util.Optional;
import java.util.function.DoublePredicate;
import java.util.function.Supplier;

/**
 * 
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public interface CPULoadMonitor {
  enum Attribute {
    SystemLoad, 
    ProcessLoad
  }
  /**
   * Add a CPU level monitor
   * @param cpuLevelTester returns true to dump a JFR recording.
   * @param jfrRecordingDestination supplies a file if a recording should be 
   * dumped. Should be used to limit the number of files created.
   * @throws Exception 
   */
  
  void monitor(DoublePredicate cpuLevelTester, Supplier<Optional<File>> jfrRecordingDestination) throws Exception;
}
