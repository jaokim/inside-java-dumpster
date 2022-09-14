/*
 * 
 */
package inside.dumpster.monitoring.event;

import inside.dumpster.bl.BusinessLogicService;
import inside.dumpster.client.Payload;
import inside.dumpster.monitoring.TransactionEvent;
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
  @Name("Protocol")
  public String protocol;
  @Name("DestPort")
  public String dstPort;
  @Name("SrcDevice")
  public String srcDevice;
  @Name("SrcDeviceId")
  public String srcDeviceId;
  @Name("ServiceClass")
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
    this.srcDevice = payload.getSrcDevice();
    this.srcDeviceId = payload.getSrcDeviceId();
    
  }
}
