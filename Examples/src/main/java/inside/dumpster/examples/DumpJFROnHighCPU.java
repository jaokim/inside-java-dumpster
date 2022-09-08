/*
 * 
 */
package inside.dumpster.examples;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jdk.jfr.Configuration;
import jdk.jfr.consumer.RecordedEvent;
import jdk.jfr.consumer.RecordingStream;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class DumpJFROnHighCPU {
  
  public static void main(String[] args) throws Exception {
    Configuration c = Configuration.getConfiguration("default");
    
    // create a recording stream using the "default" configuration
    try (RecordingStream rs = new RecordingStream(c)) {
      rs.onEvent("jdk.CPULoad", (event) -> {
        float cpuLevel = event.getFloat("machineTotal");
        System.out.println("cpu: "+cpuLevel);
        if(cpuLevel > 0.3) {
          try {
            Path jfrFilePath = File.createTempFile("highcpu", "jfr").toPath();
            rs.dump(jfrFilePath);
            System.out.println("JFR file dumped: "+jfrFilePath);
          } catch (IOException ex) {
            ex.printStackTrace();
          }
        }
      });
      rs.startAsync(); 
      
      double val1 = 9;
      double val2;
      while(true) {
        val2 = Math.sqrt(val1);
        val1 = Math.IEEEremainder(val1, val2);
        
      }
      
    }
  }
}
