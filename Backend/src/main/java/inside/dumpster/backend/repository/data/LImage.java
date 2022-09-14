/*
 * 
 */
package inside.dumpster.backend.repository.data;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class LImage implements Data {
  private byte[] buffer;
  
  @Override
  public void setBuffer(byte[] buffer) {
    this.buffer = buffer;
  }

  @Override
  public byte[] getBuffer() {
    return buffer;
  }
  
  
}
