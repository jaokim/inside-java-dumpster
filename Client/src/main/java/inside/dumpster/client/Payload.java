/*
 * 
 */
package inside.dumpster.client;

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
  public static class Destination {
    private final String destination;

    public Destination(String destination) {
      this.destination = destination;
    }

    @Override
    public String toString() {
      return destination;
    }

  };

  /**
   * The destination defines which service should handle this payload.
   *
   * @return a destination
   */
  public Destination getDestination() {
    return new Destination(String.format("%s%s", getSrcDevice(), getSrcDeviceId() != null ?getSrcDeviceId().substring(0, 0):"0"));
  }
  
  
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
  private String srcDeviceId;
  /**
   * The receiving device.
   */
  private String dstDevice;
  private String dstDeviceId;
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
    Matcher m = p.matcher(srcDevice);
    if (m.find()) {
      this.srcDevice = m.group(1);
      this.srcDeviceId = m.group(2);
    } else {
      this.srcDevice = dstDevice;
      this.srcDeviceId = "0";
    }
    /*03*/ m = p.matcher(dstDevice);
    if (m.find()) {
      this.dstDevice = m.group(1);
      this.dstDeviceId = m.group(2);
    } else {
      this.dstDevice = dstDevice;
      this.dstDeviceId = "0";
    }
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
   * F.i. "Comp"
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
   * "Mail"
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
   * F.i. 789344
   *
   * @return the srcDeviceId
   */
  public String getSrcDeviceId() {
    return srcDeviceId;
  }

  /**
   * @param srcDeviceId the srcDeviceId to set
   */
  public void setSrcDeviceId(String srcDeviceId) {
    this.srcDeviceId = srcDeviceId;
  }

  /**
   * @return the dstDeviceId
   */
  public String getDstDeviceId() {
    return dstDeviceId;
  }

  /**
   * @param dstDeviceId the dstDeviceId to set
   */
  public void setDstDeviceId(String dstDeviceId) {
    this.dstDeviceId = dstDeviceId;
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

}
