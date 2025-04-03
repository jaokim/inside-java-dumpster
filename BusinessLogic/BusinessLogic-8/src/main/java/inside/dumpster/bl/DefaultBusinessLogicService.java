/*
 * 
 */
package inside.dumpster.bl;

import inside.dumpster.client.Payload;
import inside.dumpster.client.Result;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class DefaultBusinessLogicService extends BusinessLogicService<Payload, Result> {

  public DefaultBusinessLogicService() {
    super(Payload.class, Result.class);
  }


    @Override
    public Result invoke(Payload payload) throws BusinessLogicException {
        Result result = new Result();
        result.setResult("nok");
        return result;
    }

//  public Class<Payload> getPayloadClass() {
//    return Payload.class;
//  }
//
//  public Class<Result> getResultClass() {
//    return Result.class;
//  }
    
}
