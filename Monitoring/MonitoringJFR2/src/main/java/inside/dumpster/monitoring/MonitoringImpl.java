/*
 *
 */
package inside.dumpster.monitoring;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class MonitoringImpl implements Monitoring {
  static StringEvent event = new StringEvent();
  @Override
  public MonitoringEvent createEvent() {
//    StringEvent event = new StringEvent();

    event.begin();
    return event;
  }

  public MonitoringImpl() {
  }



}
