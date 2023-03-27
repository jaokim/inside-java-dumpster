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
@Name("inside.dumpster.ServiceCall")
@Label("Service Call")
@Category({"Business Application", "Services"})
public class ServiceCall extends Event {

  @Label("Service Destination")
  public String destination;

  @Name("service")
  @Label("Service Implementation Class")
  public Class serviceClass;
}
