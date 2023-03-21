/*
 *
 */
package inside.dumpster.monitoring.event;

import inside.dumpster.monitoring.TransactionEvent;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class DataProcessing extends TransactionEvent {
  public String datatype;

  public String processType;
}