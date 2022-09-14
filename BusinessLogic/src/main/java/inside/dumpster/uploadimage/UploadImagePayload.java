/*
 * 
 */
package inside.dumpster.uploadimage;

import inside.dumpster.client.Payload;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class UploadImagePayload extends Payload {
  /**
   * What kind of processing to perform.
   */
  public enum Processing {
    Convert,None
  }
  public Processing getProcessing() {
    switch(this.getDstPort()) {
      case "443":
        return Processing.Convert;
      default:
        return Processing.None;
    }
  }
}
