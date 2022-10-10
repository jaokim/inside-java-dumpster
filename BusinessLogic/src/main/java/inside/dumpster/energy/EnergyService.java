/*
 * 
 */
package inside.dumpster.energy;

import inside.dumpster.bl.BusinessLogicException;
import inside.dumpster.bl.BusinessLogicService;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class EnergyService extends BusinessLogicService<EnergyPayload, EnergyResult> {

  public EnergyService(Class<EnergyPayload> payloadClass, Class<EnergyResult> resultClass) {
    super(payloadClass, resultClass);
  }
  @Override
  public EnergyResult invoke(EnergyPayload payload) throws BusinessLogicException {
    EnergyResult result = new EnergyResult();
    int energyLoad = payload.getDstBytes() * payload.getSrcBytes();
    result.setResult("energyLoad: "+energyLoad);
    return result;
  }
  
}
