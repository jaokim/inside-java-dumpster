/*
 * 
 */
package inside.dumpster.bl;

import inside.dumpster.bl.auth.Authenticator;
import inside.dumpster.bl.auth.User;
import inside.dumpster.client.Payload;
import inside.dumpster.client.Result;
import inside.dumpster.client.impl.Helper;
import inside.dumpster.monitoring.event.ServiceInvocation;
import java.util.UUID;

/**
 * Wrapper for a business logic service to facilitate service mapping. This
 * only serves to lessen the boilerplate code needed to map the network logs
 * to service calls. 
 * This construct is only used for the mimicked business application -- this
 * is -not- a viable design pattern to take inspiration from. 
 * You've been warned!
 * 
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class BusinessLogicServiceWrapper<P extends Payload, R extends Result> {
  private final Authenticator authenticator = new Authenticator();
  private final BusinessLogicService<P, R> service;

  BusinessLogicServiceWrapper(BusinessLogicService<P, R> service) {
    this.service = service;
  }

  /**
   * Invoke the service using the supplied payload.
   * @param payload
   * @return
   * @throws BusinessLogicException 
   */
  public R invoke(Payload payload) throws BusinessLogicException {
    payload.setTransactionId(UUID.randomUUID().toString());
    User user = authenticator.getLoggedInUser();
    ServiceInvocation serviceInvocation = new ServiceInvocation();
    serviceInvocation.serviceClass = service.getClass();
    serviceInvocation.registerPayloadData(payload);
    serviceInvocation.begin();
    
    P convertedPayload = Helper.convertPayload(this.service.payloadClass, payload);
    
    R result = service.invoke(convertedPayload);
   
    serviceInvocation.end();
    serviceInvocation.commit();
    
    return result;
  }

}
