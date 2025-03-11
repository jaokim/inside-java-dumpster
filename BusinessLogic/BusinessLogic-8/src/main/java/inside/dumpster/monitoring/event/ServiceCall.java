/*
 *
 */
package inside.dumpster.monitoring.event;

import inside.dumpster.monitoring.Event;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class ServiceCall extends Event {

  public ServiceCall() {System.out.println("New secrice vall"); }

  public String destination;

  public Class serviceClass;
}
