/*
 * 
 */
package inside.dumpster.jetty;

import inside.dumpster.outside.Bug;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class JettyServer {
  private final int port;
  private Server server;
  public JettyServer(int port) {
    this.port = port;
  }
  public void start() throws Exception {
    Bug.registerMXBean();
    server = new Server();
    ServerConnector connector = new ServerConnector(server);
    connector.setPort(port);
    server.setConnectors(new Connector[]{connector});

    ServletHandler servletHandler = new ServletHandler();
    servletHandler.addServletWithMapping(JettyServlet.class, "/business/dest/*");
    
    server.setHandler(servletHandler);

    
  }

  public static void main(String[] args) throws Exception {
    JettyServer server;
    if(args.length == 1) {
      server = new JettyServer(Integer.parseInt(args[0]));
    } else {
      server = new JettyServer(8090);
    }
    server.start();
    server.server.start();
    server.server.join();
  }
}
