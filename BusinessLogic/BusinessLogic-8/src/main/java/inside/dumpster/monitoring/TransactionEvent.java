/*
 *
 */
package inside.dumpster.monitoring;

import inside.dumpster.client.Payload;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public abstract class TransactionEvent extends Event {
  public String transactionId;


  public void registerPayloadData(Payload payload) {
    this.transactionId = payload.getTransactionId();
  }
}
