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

  public ServiceCall() {
    System.out.println("Java 8 service call");
  }

  public String destination;

  public Class serviceClass;
}
