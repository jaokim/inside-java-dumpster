/*
 *
 */
package inside.dumpster.monitoring.event;

import inside.dumpster.monitoring.Event;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */

public class UnhandledServiceCall extends Event {
  public String destination;
}
