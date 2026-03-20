/*
 *
 */
package inside.dumpster.monitoring;

import jdk.jfr.Category;
import jdk.jfr.Event;
import jdk.jfr.Name;
import jdk.jfr.StackTrace;


/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
@Category({"inside", "dumpster"})
@Name("constantString")
@StackTrace(true)
public class StringEvent extends Event implements MonitoringEvent {

  public String name;

  public String data;



  @Override
  public void doCommit() {
    commit();
  }

  @Override
  public void set(String name, String value) {
    if(name.equals("name")) {
      this.name = value;
    } else {
      this.data = value;
    }
  }

  @Override
  public void doBegin() {
    this.begin();
  }

  @Override
  public void doEnd() {
    this.end();
  }

}
