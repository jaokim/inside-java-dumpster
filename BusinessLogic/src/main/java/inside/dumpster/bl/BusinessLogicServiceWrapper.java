/*
 * 
 */
package inside.dumpster.bl;

import inside.dumpster.client.Payload;
import inside.dumpster.client.Result;
import inside.dumpster.client.impl.Helper;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class BusinessLogicServiceWrapper<P extends Payload, R extends Result> {

  public BusinessLogicService<P, R> service;

  BusinessLogicServiceWrapper(BusinessLogicService<P, R> service) {
    this.service = service;
  }


  public R invoke(Payload payload) throws BusinessLogicException {
    P p = Helper.convertPayload(this.service.payloadClass, payload);
    return service.invoke(p);
  }

}
