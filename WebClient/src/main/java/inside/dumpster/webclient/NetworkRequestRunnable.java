/*
 * 
 */
package inside.dumpster.webclient;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
class NetworkRequestRunnable implements Runnable {
  
  final HttpPayload req;

  NetworkRequestRunnable(HttpPayload req) {
    this.req = req;
  }

  @Override
  public void run() {
    try {
      new PostRequest(req.baseURI).doRequest(req);
    } catch (Exception ex) {
      Logger.getLogger(PerformRequests.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  @Override
  public String toString() {
    return "Starting at: " + req.getTime();
  }
  
}
