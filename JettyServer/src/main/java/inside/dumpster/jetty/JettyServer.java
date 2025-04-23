/*
 *
 */
package inside.dumpster.jetty;

//import com.sun.tools.sjavac.server.ServerMain;
import inside.dumpster.outside.Bug;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.tomcat.InstanceManager;
import org.apache.tomcat.SimpleInstanceManager;
import org.eclipse.jetty.jsp.JettyJspServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class JettyServer {
  private static final Logger logger = Logger.getLogger(JettyServer.class.getName());
  // Resource path pointing to where the WEBROOT is
  private static final String WEBROOT_INDEX = "/web/";

  private final int port;
  private Server server;

  public JettyServer(int port) {
    this.port = port;
  }

  public void start() throws Exception {
    Bug.registerMXBean();
    
    logger.log(Level.SEVERE, "Logging SEVERE");
    logger.log(Level.CONFIG, "Logging CONFIG");
    logger.log(Level.WARNING, "Logging WARNING");
    logger.log(Level.INFO, "Logging INFO");
    logger.log(Level.FINE, "Logging FINE");
    logger.log(Level.FINEST, "Logging FINEST");
    
    server = new Server();

    // Define ServerConnector
    ServerConnector connector = new ServerConnector(server);
    connector.setPort(port);
    server.addConnector(connector);

    // Base URI for servlet context
    URI baseUri = getWebRootResourceUri();

    // Create Servlet context
    ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
    servletContextHandler.setContextPath("/");
    servletContextHandler.setResourceBase(baseUri.toASCIIString());

    // Since this is a ServletContextHandler we must manually configure JSP support.
    enableEmbeddedJspSupport(servletContextHandler);

    // Add Application Servlets
    servletContextHandler.addServlet(JettyServlet.class, "/business/dest/*");
    servletContextHandler.addServlet(TestServlet.class, "/test/");
    // Create Example of mapping jsp to path spec
    ServletHolder holderAltMapping = new ServletHolder();
    holderAltMapping.setName("default.jsp");
    holderAltMapping.setForcedPath("/web/default.jsp");
    servletContextHandler.addServlet(holderAltMapping, "/web/");

    // Default Servlet (always last, always named "default")
    ServletHolder holderDefault = new ServletHolder("default", DefaultServlet.class);
    holderDefault.setInitParameter("resourceBase", baseUri.toASCIIString());
    holderDefault.setInitParameter("dirAllowed", "true");
    servletContextHandler.addServlet(holderDefault, "/");
    server.setHandler(servletContextHandler);

    // Start Server
    // server.setDumpAfterStart(true);
    server.start();
  }

  /**
   * Setup JSP Support for ServletContextHandlers.
   * <p>
   * NOTE: This is not required or appropriate if using a WebAppContext.
   * </p>
   *
   * @param servletContextHandler the ServletContextHandler to configure
   * @throws IOException if unable to configure
   */
  private void enableEmbeddedJspSupport(ServletContextHandler servletContextHandler) throws IOException {
    // Establish Scratch directory for the servlet context (used by JSP compilation)
    File tempDir = new File(System.getProperty("java.io.tmpdir"));
    File scratchDir = new File(tempDir.toString(), "embedded-jetty-jsp");

    if (!scratchDir.exists()) {
      if (!scratchDir.mkdirs()) {
        throw new IOException("Unable to create scratch directory: " + scratchDir);
      }
    }
    servletContextHandler.setAttribute("javax.servlet.context.tempdir", scratchDir);

    // Set Classloader of Context to be sane (needed for JSTL)
    // JSP requires a non-System classloader, this simply wraps the
    // embedded System classloader in a way that makes it suitable
    // for JSP to use
    ClassLoader jspClassLoader = new URLClassLoader(new URL[0], this.getClass().getClassLoader());
    servletContextHandler.setClassLoader(jspClassLoader);

    // Manually call JettyJasperInitializer on context startup
    servletContextHandler.addBean(new EmbeddedJspStarter(servletContextHandler));

    // Create / Register JSP Servlet (must be named "jsp" per spec)
    ServletHolder holderJsp = new ServletHolder("jsp", JettyJspServlet.class);
    holderJsp.setInitOrder(0);
    holderJsp.setInitParameter("scratchdir", scratchDir.toString());
    holderJsp.setInitParameter("logVerbosityLevel", "DEBUG");
    holderJsp.setInitParameter("fork", "false");
    holderJsp.setInitParameter("xpoweredBy", "false");
    holderJsp.setInitParameter("compilerTargetVM", "1.8");
    holderJsp.setInitParameter("compilerSourceVM", "1.8");
    holderJsp.setInitParameter("keepgenerated", "true");
    servletContextHandler.addServlet(holderJsp, "*.jsp");

    servletContextHandler.setAttribute(InstanceManager.class.getName(), new SimpleInstanceManager());
  }

  private URI getWebRootResourceUri() throws FileNotFoundException, URISyntaxException {
    URL indexUri = this.getClass().getResource(WEBROOT_INDEX);
    if (indexUri == null) {
      throw new FileNotFoundException("Unable to find resource " + WEBROOT_INDEX);
    }
    // Points to wherever /webroot/ (the resource) is
    return indexUri.toURI();
  }

  public void stop() throws Exception {
    server.stop();
  }

  /**
   * Cause server to keep running until it receives a Interrupt.
   * <p>
   * Interrupt Signal, or SIGINT (Unix Signal), is typically seen as a result of
   * a kill -TERM {pid} or Ctrl+C
   *
   * @throws InterruptedException if interrupted
   */
  public void waitForInterrupt() throws InterruptedException {
    server.join();
  }

  public static void main(String[] args) throws Exception {
    JettyServer server;
    if (args.length == 1) {
      server = new JettyServer(Integer.parseInt(args[0]));
    } else {
      server = new JettyServer(8090);
    }
    server.start();
    server.waitForInterrupt();
  }
}
