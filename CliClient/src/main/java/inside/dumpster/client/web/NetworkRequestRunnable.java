/*
 *
 */
package inside.dumpster.client.web;

import inside.dumpster.client.Payload;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class NetworkRequestRunnable implements Runnable {

  final Payload req;
  final String baseURI;
  public NetworkRequestRunnable(Payload req, String baseURI) {
    this.req = req;
    this.baseURI = baseURI;
  }

  @Override
  public void run() {
    try {
      HttpResult result = new PostRequest(baseURI).doRequest(req);
    } catch (Exception ex) {
      Logger.getLogger(PerformRequests.class.getName()).log(Level.SEVERE, "Req failed: "+req, ex);
    }
  }

  @Override
  public String toString() {
    return "Starting at: " + req.getTime();
  }

}
