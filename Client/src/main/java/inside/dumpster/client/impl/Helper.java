/*
 *
 */
package inside.dumpster.client.impl;

import inside.dumpster.client.Payload;
import inside.dumpster.client.Result;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class Helper {


  /**
   * Convert from a general payload object, to a more specific service payload.
   * @param <P> the wanted Payload class
   * @param payloadClass the Class for the wanted Payload
   * @param payload the ingoing plain payload
   * @return
   */
  public static <P extends Payload> P convertPayload(Class<P> payloadClass, Payload payload) {
    P p = null;
    if(payload == null) {
      return null;
    }
    try {
      p = payloadClass.getDeclaredConstructor().newInstance();
      p.setTransactionId(payload.getTransactionId());
      p.setDstBytes(payload.getDstBytes());
      p.setDstDevice(payload.getDstDevice());
      p.setDstPackets(payload.getDstPackets());
      p.setDstPort(payload.getDstPort());
      p.setDuration(payload.getDuration());
      p.setProtocol(payload.getProtocol());
      p.setSrcBytes(payload.getSrcBytes());
      p.setSrcDevice(payload.getSrcDevice());
      p.setSrcPackets(payload.getSrcPackets());
      p.setSrcPort(payload.getSrcPort());
      p.setTime(payload.getTime());
      p.setInputStream(payload.getInputStream());

    } catch (Exception ex) {
      Logger.getLogger(Helper.class.getName()).log(Level.SEVERE, null, ex);
    }


    return p;
  }


  public <R extends Result> R convertResult(Class<R> resultlass, Result result) {
    return null;
  }
}
