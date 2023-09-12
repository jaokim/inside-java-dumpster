/*
 * 
 */
package inside.dumpster.backend.api;

import inside.dumpster.client.Payload;
import inside.dumpster.client.Result;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public interface CreditVault {
  public Result doDeposit(Payload payload);
}
