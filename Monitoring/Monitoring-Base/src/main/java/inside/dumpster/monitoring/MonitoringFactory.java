/*
 *
 */
package inside.dumpster.monitoring;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class MonitoringFactory {
  static final Monitoring monitoring;
  static {
    Monitoring temp = new Monitoring() {
      @Override
      public MonitoringEvent createEvent() {
        return new MonitoringEvent() {
        @Override
        public void doCommit() {
          // nop
        }

        @Override
        public void set(String name, String value) {
          // nop
        }

          @Override
          public void doBegin() {
            // nop
          }

          @Override
          public void doEnd() {
            // nop
          }
      };
      }
    };
    try {
      temp = (Monitoring) Class.forName("inside.dumpster.monitoring.MonitoringImpl").getConstructor().newInstance();
    } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
      Logger.getLogger(Monitoring.class.getName()).log(Level.SEVERE, "No MonitorImpl class found on classpath, setting nop variant", ex);
    } finally {
      monitoring = temp;
    }
  }


  public static Monitoring getMonitoring() {
    return monitoring;
  }

}
