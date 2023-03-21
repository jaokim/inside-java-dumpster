/*
 *
 */
package inside.dumpster.client.web;

import inside.dumpster.client.Payload;
import inside.dumpster.client.impl.PayloadHelper;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
@Deprecated
public class HttpPayload extends Payload {

  public String baseURI;

  public void setBaseURI(String baseURI) {
    this.baseURI = baseURI;
  }
    public HttpPayload() {
    }
    public HttpPayload(String time, String duration, String srcDevice, String dstDevice, String protocol, String srcPort, String dstPort, String srcPackets, String dstPackets, String srcBytes, String dstBytes) {
        super(time, duration, srcDevice, dstDevice, protocol, srcPort, dstPort, srcPackets, dstPackets, srcBytes, dstBytes);
    }


    @Override
    public String toString() {
        return PayloadHelper.getURI(this).toASCIIString();
    }

}
