/*
 * 
 */
package inside.dumpster.monitoring.service;

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
  private String destination;
  
  

  public void setDestination(String destination) {
    this.destination = destination;
  }

}
