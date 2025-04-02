/*
 *
 */
package inside.dumpster.database;

import com.github.jaokim.arguably.Arguments;
import java.io.PrintWriter;
import java.net.InetAddress;
import org.apache.derby.drda.NetworkServerControl;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class Server extends Arguments {
  public final Arg LogConn = new Arguments.Arg("-logConnections", 
          "logconnections", 
          Boolean.class, 
          Arguments.Arg.Is.Optional, 
          "Should connections be logged", 
          "false");
  public static void main(String[] args) throws Exception {
    Server server = new Server(args);
    server.parseArgs(args);
    server.main();
  }

  public Server(String[] args) throws Exception {
    super(args);
  }
  public void main() throws Exception {
    
    // This starts the server on the default port, 1527, listening on localhost (all interfaces).
    NetworkServerControl server = new NetworkServerControl();//InetAddress.getLoopbackAddress(), 1528);
    server.start(new PrintWriter(System.out));
    if (Verbose.isTrue()) {
      System.out.println("Verbose mode is on");
    }
    server.logConnections(Verbose.isTrue());
    server.trace(Verbose.isTrue());
    while (true) {
      Thread.sleep(10000);
    }
  }
}
