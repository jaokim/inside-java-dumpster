/*
 *
 */
package inside.dumpster.energy;

import inside.dumpster.bl.BusinessLogicFactory;
import inside.dumpster.bl.BusinessLogicService;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class EnergyServiceTest {

  public EnergyServiceTest() {
  }

  /**
   * Test of invoke method, of class EnergyService.
   */
  @Test
  public void testInvoke() throws Exception {
    EnergyService service = new EnergyService(EnergyPayload.class, EnergyResult.class);
    EnergyService.EXIT_EARLY = true;
    EnergyPayload p = new EnergyPayload();
    p.setSrcDevice("Port898989");
    p.setDstPort("1234");
    service.invoke(p);


    EnergyService service2 = new EnergyService(EnergyPayload.class, EnergyResult.class);
    //service2.invoke(p);

  }

}
