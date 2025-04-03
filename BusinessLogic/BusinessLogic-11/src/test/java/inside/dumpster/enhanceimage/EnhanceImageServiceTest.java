/*
 *
 */
package inside.dumpster.enhanceimage;

import inside.dumpster.backend.Backend;
import inside.dumpster.backend.BackendException;
import inside.dumpster.backend.database.DummyDatabaseImpl;
import inside.dumpster.bl.BusinessLogicFactory;
import inside.dumpster.bl.BusinessLogicServiceWrapper;
import inside.dumpster.client.Payload;
import inside.dumpster.client.Result;
import inside.dumpster.client.impl.PayloadDataGenerator;
import java.io.IOException;
import java.io.InputStream;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
  @Test()
  public void testInvoke() throws Exception {
    final PayloadDataGenerator generator = new PayloadDataGenerator();
    final EnhanceImagePayload payload = new EnhanceImagePayload();
    payload.setDstDevice("Comp2");
    payload.setDstBytes(300);
    payload.setSrcBytes(300);

    Backend.useThis(Backend.builder().setDatabase(new DummyDatabaseImpl()
    {
      @Override
      public InputStream getImageData(String dstPort) throws BackendException {
        try {

          return generator.generatePayload(payload, EnhanceImageService.class);
        } catch (IOException ex) {
          throw new BackendException(ex);
        }
      }
    }
    ).build());


    BusinessLogicFactory blf = new BusinessLogicFactory();
    BusinessLogicServiceWrapper w = blf.lookupService(Payload.Destination.Comp2);
    Result res = w.invoke(payload);
    System.out.println("Res: "+res.toString());
    assertNotNull(res.getResult());
  }
}
