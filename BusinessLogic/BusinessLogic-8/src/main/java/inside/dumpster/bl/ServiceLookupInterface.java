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
public interface ServiceLookupInterface {
  BusinessLogicService<? extends Payload, ? extends Result> lookupServiceWrapper(Payload.Destination destination) throws BusinessLogicException;
}
