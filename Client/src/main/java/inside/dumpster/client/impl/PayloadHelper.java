/*
 * 
 */
package inside.dumpster.client.impl;

import inside.dumpster.client.Payload;
import java.net.URI;
import java.net.URISyntaxException;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class PayloadHelper {
  public static String PAYLOAD_URI_8 = "{destination}/{srcDevice}/{srcDeviceID}/{srcPort}/{dstDevice}/{dstDeviceId}/{dstPort}/{protocol}";
  public static URI getURI(Payload payload) {
        try {
            return new URI(String.format("business/dest/%s/%s/%s/%s/%s/%s/%s/%s",
                    payload.getDestination().toString(),
                    payload.getSrcDevice(),
                    payload.getSrcDeviceId(),
                    payload.getSrcPort(),
                    payload.getDstDevice(),
                    payload.getDstDeviceId(),
                    payload.getDstPort(),
                    payload.getProtocol()
                    
            ));
        } catch (URISyntaxException ex) {
            throw new IllegalArgumentException("Honestly, this should never happen.", ex);
        }
    }
    
    public static <P extends Payload> P fillPayloadFromURI(P payload, String uri) {
      String[] parts = uri.split("/");
      int cnt = 1;
      payload.setSrcPort(parts[cnt++]);
      payload.setSrcDevice(parts[cnt++]);
      payload.setSrcDeviceId(parts[cnt++]);
      payload.setDstDevice(parts[cnt++]);
      payload.setDstDeviceId(parts[cnt++]);
      payload.setDstPort(parts[cnt++]);
      payload.setProtocol(parts[cnt++]);
      return payload;
    }

  public static String getDestination(String uri) {
    return uri.split("/")[0];
  }
}
