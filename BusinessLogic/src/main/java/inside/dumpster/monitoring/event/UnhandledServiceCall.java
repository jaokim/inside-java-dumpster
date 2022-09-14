/*
 * 
 */
package inside.dumpster.monitoring.event;

import jdk.jfr.Category;
import jdk.jfr.Event;
import jdk.jfr.Label;
import jdk.jfr.Name;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
@Name("inside.dumpster.UnhandledServiceCall")
@Label("Unhandled Service Call")
@Category({"Business Application", "Services"})
public class UnhandledServiceCall extends Event {
  @Name("Destination")
  @Label("Unhandled Service Destination")
  public String destination;
  

}
