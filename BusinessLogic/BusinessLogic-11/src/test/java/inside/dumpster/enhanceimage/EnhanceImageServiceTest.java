/*
 *
 */
package inside.dumpster.enhanceimage;

import inside.dumpster.bl.BusinessLogicFactory;
import inside.dumpster.bl.BusinessLogicServiceWrapper;
import inside.dumpster.client.Payload;
import inside.dumpster.client.Result;
import inside.dumpster.client.impl.PayloadDataGenerator;
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
    EnhanceImagePayload payload = new EnhanceImagePayload();
    payload.setDstDevice("Comp2");
    payload.setDstBytes(300);
    payload.setSrcBytes(300);
    PayloadDataGenerator generator = new PayloadDataGenerator();
    payload.setInputStream(generator.generatePayloadData(payload, PayloadDataGenerator.Type.Image));

    BusinessLogicFactory blf = new BusinessLogicFactory();
    BusinessLogicServiceWrapper w = blf.lookupService(Payload.Destination.Comp2);
    Result res = w.invoke(payload);
    System.out.println("Res: "+res.toString());

  }

}
