/*
 * 
 */
package inside.dumpster.client.impl;

import inside.dumpster.client.Payload;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 * @param <P>
 */
public interface PayloadProcessor<P extends Payload> {
  public void processPayload(P payload);
}
