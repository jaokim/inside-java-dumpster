/*
 *
 */
package inside.dumpster.client.cli;

import inside.dumpster.bl.BusinessLogicException;
import inside.dumpster.bl.BusinessLogicFactory;
import inside.dumpster.bl.BusinessLogicServiceWrapper;
import inside.dumpster.client.Cli;
import inside.dumpster.client.Payload;
import inside.dumpster.client.impl.PayloadDataGenerator;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class LocalRequestRunnable implements Runnable {

  private final Payload req;

  public LocalRequestRunnable(Payload req) {
    this.req = req;
  }

  @Override
  public void run() {
    try {
      BusinessLogicServiceWrapper wrapper = new BusinessLogicFactory().lookupService(req.getDestination());
      PayloadDataGenerator generator = new PayloadDataGenerator();
      req.setInputStream(generator.genetarePayloadData(req));
      wrapper.invoke(req);
    } catch (IOException | BusinessLogicException ex) {
      Logger.getLogger(Cli.class.getName()).log(Level.SEVERE, null, ex);
    }
    System.out.println("Doing " + req);
  }

}
