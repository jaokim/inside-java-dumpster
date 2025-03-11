/*
 * 
 */
package inside.dumpster.bl;

import inside.dumpster.client.Payload;
import inside.dumpster.client.Result;
import inside.dumpster.service.Service;


/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 * @param <P> type of payload
 * @param <R> type of result
 */
public abstract class BusinessLogicService<P extends Payload, R extends Result> implements Service {
  final Class<P> payloadClass;
  public BusinessLogicService(Class<P> payloadClass, Class<R> resultClass) {
    this.payloadClass = payloadClass;
  }
// public BusinessLogicService() {
//    this.payloadClass = null;
//  }
 
    public abstract R invoke(P payload) throws BusinessLogicException;
    
}
