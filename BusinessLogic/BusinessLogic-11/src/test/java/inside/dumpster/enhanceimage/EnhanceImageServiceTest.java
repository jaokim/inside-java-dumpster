/*
 *
 */
package inside.dumpster.enhanceimage;

import inside.dumpster.backend.Backend;
import inside.dumpster.backend.BackendException;
import inside.dumpster.backend.database.Database;
import inside.dumpster.bl.BusinessLogicFactory;
import inside.dumpster.bl.BusinessLogicServiceWrapper;
import inside.dumpster.client.Payload;
import inside.dumpster.client.Result;
import inside.dumpster.client.impl.PayloadDataGenerator;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class EnhanceImageServiceTest {

  public EnhanceImageServiceTest() {
  }

  /**
   * Test of invoke method, of class EnhanceImageService.
   */
  @Test
  public void testInvoke() throws Exception {
    final PayloadDataGenerator generator = new PayloadDataGenerator();
    final EnhanceImagePayload payload = new EnhanceImagePayload();
    payload.setDstDevice("Comp2");
    payload.setDstBytes(300);
    payload.setSrcBytes(300);

    Backend.useThis(Backend.builder().setDatabase(new Database()
    {
      @Override
      public InputStream getImageData(String dstPort) throws BackendException {
        try {

          return generator.generatePayloadData(payload, PayloadDataGenerator.Type.Image);
        } catch (IOException ex) {
          throw new BackendException(ex);
        }
      }

      @Override
      public InputStream getTextData(String srcPort) throws BackendException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
      }

      @Override
      public void insertImageData(String dstPort, InputStream iStream) throws BackendException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
      }

      @Override
      public void insertImageData(String dstPort, InputStream iStream, boolean overwrite) throws BackendException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
      }

      @Override
      public void insertTextData(String srcPort, InputStream iStream) throws BackendException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
      }

      @Override
      public void insertTextData(String srcPort, InputStream iStream, boolean overwrite) throws BackendException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
      }

    }

    ).build());


    BusinessLogicFactory blf = new BusinessLogicFactory();
    BusinessLogicServiceWrapper w = blf.lookupService(Payload.Destination.Comp2);
    Result res = w.invoke(payload);
    System.out.println("Res: "+res.toString());

  }

}
