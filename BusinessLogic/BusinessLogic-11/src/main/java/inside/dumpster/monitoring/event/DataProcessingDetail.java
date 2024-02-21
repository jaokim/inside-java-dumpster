/*
 *
 */
package inside.dumpster.monitoring.event;

import jdk.jfr.Category;
import jdk.jfr.Description;
import jdk.jfr.Enabled;
import jdk.jfr.Event;
import jdk.jfr.Label;
import jdk.jfr.Name;
import jdk.jfr.Threshold;

/**
 * Event that tracks the detailed data processing. This event should only be
 * enabled when running certain tests, and detailed profiling. Never enable in
 * a production environment.
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
@Name("inside.dumpster.ProcessDataDetail")
@Label("Detailed Data Processing")
@Category({"Business Application", "Data", "Processing", "Profiling"})
@Description("Detailed statistics over data processing events. Don't enable this event in production!")
@Threshold("50 ms")
@Enabled(false)
public class DataProcessingDetail extends Event {
  public int byteIndex;
  public int inValue;
  public byte outValue;
  public String reason;
  public int x;
  public int y;
}
