/*
 *
 */
package inside.dumpster.monitoring.event;

import inside.dumpster.bl.BusinessLogicService;
import inside.dumpster.client.Payload;
import inside.dumpster.monitoring.TransactionEvent;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import jdk.jfr.Category;
import jdk.jfr.Label;
import jdk.jfr.Name;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
@Name("inside.dumpster.ServiceInvocation")
@Label("Service Invoked")
@Category({"Business Application", "Services", "Invocation"})
public class ServiceInvocation extends TransactionEvent {
  @Label("Protocol")
  public String protocol;
  @Label("DestPort")
  public String dstPort;
  @Label("Dst Packets")
  public String dstPackets;
  @Label("Dst Bytes")
  public int dstBytes;
  @Label("Src Packets")
  public String srcPackets;
  @Label("Data")
  public String data;
  @Label("Src Bytes")
  public int srcBytes;
  @Label("Service Implementation Class")
  @Name("service")
  public Class<? extends BusinessLogicService> serviceClass;

  /**
   * Extract info from payload and set on this event.
   * @param payload
   */
  @Override
  public void registerPayloadData(Payload payload) {
    super.registerPayloadData(payload);
    this.protocol = payload.getProtocol();
    this.dstPort = payload.getDstPort();
    this.dstPackets = payload.getDstPackets();
    this.dstBytes = payload.getDstBytes();
    this.srcPackets = payload.getSrcPackets();
    this.srcBytes = payload.getSrcBytes();

  }
}
