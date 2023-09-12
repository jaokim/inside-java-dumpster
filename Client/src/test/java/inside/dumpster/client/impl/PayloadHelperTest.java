/*
 *
 */
package inside.dumpster.client.impl;

import inside.dumpster.client.Payload;
import java.net.URI;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class PayloadHelperTest {

  public PayloadHelperTest() {
  }

  /**
   * Test of getURI method, of class PayloadHelper.
   */
  @Test
  public void testGetURI() {
    String time = "1234";
    String duration = "1099";
    String srcDevice = "Comp821204";//Payload.Destination.EnterpriseAppServer.name();
    String dstDevice = "Comp14898";
    String srcPort = "5060";
    String dstPort = "443";
    String srcPackets = "543678";
    String dstPackets = "12344";
    String srcBytes = "100000";
    String dstBytes = "8";
    String protocol = "17";

    Payload payload = new Payload(time, duration, srcDevice, dstDevice, protocol, srcPort, dstPort, srcPackets, dstPackets, srcBytes, dstBytes);

    URI uri = PayloadHelper.getURI(payload);
    System.out.println("URI: "+uri.toASCIIString());
    String uriAfterServlets = uri.toASCIIString().substring("business/dest/".length());
    System.out.println("URI: "+uriAfterServlets);
    Payload result = new Payload();

    PayloadHelper.fillPayloadFromURI(result, uriAfterServlets);
    String stringRes = PayloadHelper.getURI(result).toASCIIString();
    System.out.println("Expected: "+payload);
    System.out.println("StrRes  : "+stringRes);
    System.out.println("Result  : "+result);
    assertEquals(payload.getDstPackets(), result.getDstPackets());
    assertEquals(payload.getSrcPackets(), result.getSrcPackets());
    assertEquals(payload.getProtocol(), result.getProtocol());
    assertEquals(payload.getSrcPort(), result.getSrcPort());
    assertEquals(payload.getDstPort(), result.getDstPort());
    assertEquals(payload.getSrcBytes(), result.getSrcBytes());
    assertEquals(payload.getDstBytes(), result.getDstBytes());
    assertEquals(payload.getDstDevice(), result.getDstDevice());
    assertEquals(payload.getSrcDevice(), result.getSrcDevice());
    assertEquals(payload.getDestination(), result.getDestination());


  }

  /**
   * Test of fillPayloadFromURI method, of class PayloadHelper.
   */
  @Test
  public void testFillPayloadFromURI() {
      String time = "1234";
    String duration = "1099";
    String srcDevice = "Comp821204";//Payload.Destination.EnterpriseAppServer.name();
    String dstDevice = "Comp14898";
    String srcPort = "5060";
    String dstPort = "443";
    String srcPackets = "543678";
    String dstPackets = "12344";
    String srcBytes = "100000";
    String dstBytes = "8";
    String protocol = "17";

    Payload payload = new Payload(time, duration, srcDevice, dstDevice, protocol, srcPort, dstPort, srcPackets, dstPackets, srcBytes, dstBytes);
    Payload result = new Payload(time, duration, srcDevice, dstDevice, protocol, srcPort, dstPort, srcPackets, dstPackets, srcBytes, dstBytes);

    URI uri = PayloadHelper.getURI(payload);
    System.out.println("URI: "+uri.toASCIIString());
    String uriAfterServlets = uri.toASCIIString().substring("business/dest/".length());
    System.out.println("URI: "+uriAfterServlets);

    PayloadHelper.fillPayloadFromURI(result, uriAfterServlets);
    String stringRes = PayloadHelper.getURI(result).toASCIIString();
    System.out.println("Expected: "+payload);
    System.out.println("StrRes  : "+stringRes);
    System.out.println("Result  : "+result);
    assertEquals(payload.getDstPackets(), result.getDstPackets());
    assertEquals(payload.getSrcPackets(), result.getSrcPackets());
    assertEquals(payload.getProtocol(), result.getProtocol());
    assertEquals(payload.getSrcPort(), result.getSrcPort());
    assertEquals(payload.getDstPort(), result.getDstPort());
    assertEquals(payload.getSrcBytes(), result.getSrcBytes());
    assertEquals(payload.getDstBytes(), result.getDstBytes());
    assertEquals(payload.getDstDevice(), result.getDstDevice());
    assertEquals(payload.getSrcDevice(), result.getSrcDevice());
    assertEquals(payload.getDestination(), result.getDestination());

  }

  /**
   * Test of getDestination method, of class PayloadHelper.
   */
  @Test
  public void testGetDestination() {
  }

}
