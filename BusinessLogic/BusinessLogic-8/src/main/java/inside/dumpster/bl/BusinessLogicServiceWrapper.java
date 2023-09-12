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
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Wrapper for a business logic service to facilitate service mapping. This only
 * serves to lessen the boilerplate code needed to map the network logs to
 * service calls.This construct is only used for the mimicked business
 * application -- this is -not- a viable design pattern to take inspiration
 * from. You've been warned!
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 * @param <SpecificPayload> class extending Payload
 * @param <SpecificResult> class extending Result
 */
public class BusinessLogicServiceWrapper<SpecificPayload extends Payload, SpecificResult extends Result> {
  private final Authenticator authenticator = new Authenticator();
  private final BusinessLogicService<SpecificPayload, SpecificResult> service;
  private static final SecureRandom random = new SecureRandom();
  private static final Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();

  BusinessLogicServiceWrapper(BusinessLogicService<SpecificPayload, SpecificResult> service) {
    this.service = service;
  }

  /**
   * Invoke the service using the supplied payload.
   *
   * @param payload
   * @return
   * @throws BusinessLogicException
   */
  public SpecificResult invoke(Payload payload) throws BusinessLogicException {
    payload.setTransactionId(generateTransactionId());
    User user = authenticator.getLoggedInUser();
    ServiceInvocation serviceInvocation = new ServiceInvocation();
    serviceInvocation.serviceClass = service.getClass();
    serviceInvocation.registerPayloadData(payload);
    serviceInvocation.begin();

    SpecificPayload convertedPayload = Helper.convertPayload(this.service.payloadClass, payload);

    SpecificResult result = service.invoke(convertedPayload);

    serviceInvocation.end();
    serviceInvocation.commit();

    return result;
  }

  private String generateTransactionId() {
    byte[] buffer = new byte[20];
    random.nextBytes(buffer);
    return encoder.encodeToString(buffer);
  }
}
