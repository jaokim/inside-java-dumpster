/*
 * 
 */
package inside.dumpster.monitoring.service;

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
  @Name("Destination")
  @Label("Service Destination")
  private String destination;
  
  @Name("Class")
  @Label("Service Implementation Class")
  private Class clazz;
  

  public void setDestination(String destination) {
    this.destination = destination;
  }

  public void setClass(Class aClass) {
    this.clazz = aClass;
  }

}
