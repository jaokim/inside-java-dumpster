/*
 *
 */
package inside.dumpster.monitoring;
//import com.oracle.jrockit.jfr.*;
//import com.oracle.jrockit.jfr.EventToken;
//import com.oracle.jrockit.jfr.InvalidEventDefinitionException;
//import com.oracle.jrockit.jfr.InvalidValueException;
//import com.oracle.jrockit.jfr.Producer;
//import com.oracle.jrockit.jfr.InstantEvent;
//import com.oracle.jrockit.jfr.ValueDefinition;
//import com.oracle.jrockit.jfr.management.FlightRecordingMBean;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
//@EventDefinition(path = "com/jsnordst/durationevent/in", name = "My Event", description = "An event triggered by doStuff.", stacktrace = true, thread = true)
public class Event {
  public void begin() {
      System.out.println("Event: "+this.getClass().getName());
  }
  public void end() {}
  public void commit() {}
}
