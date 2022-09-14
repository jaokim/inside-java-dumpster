/*
 * 
 */
package inside.dumpster.monitoring;

import inside.dumpster.client.Payload;
import jdk.jfr.Event;
import jdk.jfr.Label;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public abstract class TransactionEvent extends Event {
  @TransactionId
  @Label("Transaction Id")
  public String transactionId;
  
  
  public void registerPayloadData(Payload payload) {
    this.transactionId = payload.getTransactionId();
  }
}
