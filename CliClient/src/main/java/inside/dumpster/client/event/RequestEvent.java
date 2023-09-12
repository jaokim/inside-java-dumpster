/*
 *
 */
package inside.dumpster.client.event;

import jdk.jfr.Category;
import jdk.jfr.Event;
import jdk.jfr.Name;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
@Name("inside.dumpster.client.Request")
@Category({"Inside", "Dumpster", "Client"})
public class RequestEvent extends Event {
  public String uri;
  public String result;
  public String destination;
  public int status;
  public enum Type {
    Network,Local
  }
  public String type;
}
