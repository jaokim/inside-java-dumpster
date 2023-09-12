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

  public ServiceCall() { }

  public String destination;

  public Class serviceClass;
}
