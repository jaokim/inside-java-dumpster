/*
 * 
 */
package inside.dumpster.bl;

import inside.dumpster.client.Payload;
import inside.dumpster.client.Result;


/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 * @param <P> type of payload
 * @param <R> type of result
 */
public abstract class BusinessLogicService<P extends Payload, R extends Result> {
  final Class<P> payloadClass;
  public BusinessLogicService(Class<P> payloadClass, Class<R> resultClass) {
    this.payloadClass = payloadClass;
  }
 
    public abstract R invoke(P payload) throws BusinessLogicException;
//    public Class<P> getPayloadClass();
//    public Class<R> getResultClass();

  
}
