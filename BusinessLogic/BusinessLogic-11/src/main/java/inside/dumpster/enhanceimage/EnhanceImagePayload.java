/*
 *
 */
package inside.dumpster.enhanceimage;

import inside.dumpster.client.Payload;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class EnhanceImagePayload extends Payload {
  public int getImageSize() {
    return this.getDstBytes();
  }

  public void setImageSize(int i) {
    this.setDstBytes(i);
  }
}
