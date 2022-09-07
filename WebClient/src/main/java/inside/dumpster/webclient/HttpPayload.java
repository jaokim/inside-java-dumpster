/*
 * 
 */
package inside.dumpster.webclient;

import inside.dumpster.client.Payload;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class HttpPayload extends Payload {

  String baseURI;

  public void setBaseURI(String baseURI) {
    this.baseURI = baseURI;
  }
    public HttpPayload() {
    }
    public HttpPayload(String time, String duration, String srcDevice, String dstDevice, String protocol, String srcPort, String dstPort, String srcPackets, String dstPackets, String srcBytes, String dstBytes) {
        super(time, duration, srcDevice, dstDevice, protocol, srcPort, dstPort, srcPackets, dstPackets, srcBytes, dstBytes);
    }
    public URI getURI() {
        try {
            return new URI(String.format("business/dest/%s",
                    getDestination().toString()
                    /*),
                    getSrcPort(),
                    getDstDevice(),
                    getDstDeviceId(),
                    getDstPort(),
                    getProtocol()*/
                    
            ));
        } catch (URISyntaxException ex) {
            throw new IllegalArgumentException("Honestly, this should never happen.", ex);
        }
    }
    
    
    @Override
    public String toString() {
        return getURI().toASCIIString();
    }

}
