/*
 *
 */
package inside.dumpster.monitoring;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public interface MonitoringEvent {
  void set(String name, String value);
  void doBegin();
  void doEnd();
  void doCommit();
}
