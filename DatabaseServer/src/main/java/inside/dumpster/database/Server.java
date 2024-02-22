/*
 *
 */
package inside.dumpster.database;

import java.io.PrintWriter;
import org.apache.derby.drda.NetworkServerControl;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class Server {
  public static void main(String[] args) throws Exception {
    // This starts the server on the default port, 1527, listening on localhost (all interfaces).
    NetworkServerControl server = new NetworkServerControl();
    server.start(new PrintWriter(System.out));
    while (true) {
      Thread.sleep(10000);
    }
  }
}
