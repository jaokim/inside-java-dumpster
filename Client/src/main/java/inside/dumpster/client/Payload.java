/*
 *
 */
package inside.dumpster.client;

import inside.dumpster.client.impl.PayloadHelper;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class maps the NetFlowData log file.
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class Payload {
  /**
   * The destination defines which service should handle a payload.
   */
  public enum Destination {
      /**
       * Around ~1600 requests.
       */
      Comp0,
      /**
       * Most requests. Around ~5900 requests.
       */
      Comp1,
      /**
       * Around ~1000 requests.
       */
      Comp2,
      /**
       * Around ~1200 requests.
       */
      Comp3,
      /**
       * Around ~1400 requests.
       */
      Comp4,
      /**
       * Around ~1000 requests.
       */
      Comp5,
      /**
       * Around ~1000 requests.
       */
      Comp6,
      /**
       * Around ~850 requests.
       */
      Comp7,
      /**
       * Around ~1500 requests.
       */
      Comp8,
      /**
       * Around ~2000 requests.
       */
      Comp9,
      /**
       * Only very few requests. In the order of 10.
       */
      ActiveDirectory,
      /**
       * Relatively few requests, but a burst of requests. Total ~1200 request,
       * 1000 coming at the same time.
       */
      EnterpriseAppServer,
      /**
       * Few requests. Like 20.
       */
      IP,
      /**
       * Few requests. Like 20.
       */
      VPN,
      Unknown;

      public static Destination fromString(String dest) {
        if(dest == null) return Unknown;
        for(Destination d : Destination.values()) {
          if(dest.startsWith(d.name())) return d;
        }
        return Unknown;
      }
  }

  private Destination destination;

  /**
   * The start time of the event in epoch time format
   */
  private long time;
  /**
   * The duration of the event in seconds.
   */
  private String duration;
  /**
   * The device that likely initiated the event.
   */
  private String srcDevice;

  /**
   * The receiving device.
   */
  private String dstDevice;
  /**
   * The protocol number.
   */
  private String protocol;
  /**
   * The port used by the SrcDevice.
   */
  private String srcPort;
  /**
   * The port used by the DstDevice.
   */
  private String dstPort;
  /**
   * The number of packets the SrcDevice sent during the event.
   */
  private String srcPackets;
  /**
   * The number of packets the DstDevice sent during the event.
   */
  private String dstPackets;
  /**
   * The number of bytes the SrcDevice sent during the event.
   */
  private int srcBytes;
  /**
   * The number of bytes the DstDevice sent during the event.
   */
  private int dstBytes;

  private InputStream inputStream;

  private String transactionId;

  /**
   * Create an empty payload.
   */
  public Payload() {
  }

  /**
   * Constructor for all payload values, as returned by the NetFlow data source. The order
   * of the string is the same as in the standard log file.
   * @param time
   * @param duration
   * @param srcDevice
   * @param dstDevice
   * @param protocol
   * @param srcPort
   * @param dstPort
   * @param srcPackets
   * @param dstPackets
   * @param srcBytes
   * @param dstBytes
   */
  public Payload(String time, String duration, String srcDevice, String dstDevice, String protocol, String srcPort, String dstPort, String srcPackets, String dstPackets, String srcBytes, String dstBytes) {
    this.setPayloadValues(time, duration, srcDevice, dstDevice, protocol, srcPort, dstPort, srcPackets, dstPackets, srcBytes, dstBytes);
  }


  /**
   * Set all payload values, as returned by the NetFlow data source. The order
   * of the string is the same as in the standard log file.
   * @see inside.dumpster.NetFlowData
   * @param time
   * @param duration
   * @param srcDevice
   * @param dstDevice
   * @param protocol
   * @param srcPort
   * @param dstPort
   * @param srcPackets
   * @param dstPackets
   * @param srcBytes
   * @param dstBytes
   */
  public final void setPayloadValues(String time, String duration, String srcDevice, String dstDevice, String protocol, String srcPort, String dstPort, String srcPackets, String dstPackets, String srcBytes, String dstBytes) {
    Pattern p = Pattern.compile("([A-Za-z]+[0-9]?)([0-9]+)");
    /*0*/ this.time = Long.parseLong(time);
    /*1*/ this.duration = duration;
    /*2*/ this.srcDevice = srcDevice;
    /*03*/ this.dstDevice = dstDevice;
    /*4*/ this.protocol = protocol;
    /*5*/ this.srcPort = srcPort;
    /*6*/ this.dstPort = dstPort;
    /*7*/ this.srcPackets = srcPackets;
    /*8*/ this.dstPackets = dstPackets;
    try {
      /*9*/ this.srcBytes = Integer.parseInt(srcBytes);
    } catch (NumberFormatException nfe) {
    }
    try {
      /*10*/ this.dstBytes = Integer.parseInt(dstBytes);
    } catch (NumberFormatException nfe) {

    }

  }


  /**
   * The destination defines which service should handle this payload.
   *
   * @return a destination
   */
  public Destination getDestination() {
    return destination != null ? destination : (destination = Destination.fromString(srcDevice));
  }

  /**
   * @return the time
   */
  public long getTime() {
    return time;
  }

  /**
   * @param time the time to set
   */
  public void setTime(long time) {
    this.time = time;
  }

  /**
   * The duration, f.i. 20870
   * @return the duration
   */
  public String getDuration() {
    return duration;
  }

  /**
   * @param duration the duration to set
   */
  public void setDuration(String duration) {
    this.duration = duration;
  }

  /**
   * F.i. "Comp429341"
   *
   * @return the srcDevice
   */
  public String getSrcDevice() {
    return srcDevice;
  }

  /**
   * @param srcDevice the srcDevice to set
   */
  public void setSrcDevice(String srcDevice) {
    this.srcDevice = srcDevice;
  }

  /**
   * "ActiveDirecotry", "Comp34988"
   *
   * @return the dstDevice
   */
  public String getDstDevice() {
    return dstDevice;
  }

  /**
   * @param dstDevice the dstDevice to set
   */
  public void setDstDevice(String dstDevice) {
    this.dstDevice = dstDevice;
  }

  /**
   * F.i. 17, 6
   * @return the protocol
   */
  public String getProtocol() {
    return protocol;
  }

  /**
   * @param protocol the protocol to set
   */
  public void setProtocol(String protocol) {
    this.protocol = protocol;
  }

  /**
   * F.i. Port06522
   * @return the srcPort
   */
  public String getSrcPort() {
    return srcPort;
  }

  /**
   * @param srcPort the srcPort to set
   */
  public void setSrcPort(String srcPort) {
    this.srcPort = srcPort;
  }

  /**
   * 161, 514, 443, 22, 5061, Port6890
   * @return the dstPort
   */
  public String getDstPort() {
    return dstPort;
  }

  /**
   * @param dstPort the dstPort to set
   */
  public void setDstPort(String dstPort) {
    this.dstPort = dstPort;
  }

  /**
   * 277, 1461922, etc.
   * @return the srcPackets
   */
  public String getSrcPackets() {
    return srcPackets;
  }

  /**
   * @param srcPackets the srcPackets to set
   */
  public void setSrcPackets(String srcPackets) {
    this.srcPackets = srcPackets;
  }

  /**
   * 277, 1461922, etc.
   * @return the dstPackets
   */
  public String getDstPackets() {
    return dstPackets;
  }

  /**
   * @param dstPackets the dstPackets to set
   */
  public void setDstPackets(String dstPackets) {
    this.dstPackets = dstPackets;
  }

  /**
   * 277, 14619228908
   * @return the srcBytes
   */
  public int getSrcBytes() {
    return srcBytes;
  }

  /**
   * @param srcBytes the srcBytes to set
   */
  public void setSrcBytes(int srcBytes) {
    this.srcBytes = srcBytes;
  }

  /**
   * 0, 1543890, and so on.
   * @return the dstBytes
   */
  public int getDstBytes() {
    return dstBytes;
  }

  /**
   * @param dstBytes the dstBytes to set
   */
  public void setDstBytes(int dstBytes) {
    this.dstBytes = dstBytes;
  }


  /**
   * If not null, this contains the data sent with the payload.
   * @return an input stream with data, can be null
   */
  public InputStream getInputStream() {
    return inputStream;
  }


  /**
   * This payload's data stream.
   * @param inputStream
   */
  public void setInputStream(InputStream inputStream) {
    this.inputStream = inputStream;
  }



  public String getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(String transactionId) {
    this.transactionId = transactionId;
  }


    @Override
    public String toString() {
        return PayloadHelper.getURI(this).toASCIIString();
    }

}
