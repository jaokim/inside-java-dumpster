/*
 * 
 */
package inside.dumpster.backend.repository.data;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class Data {
  protected final byte[] buffer;
  Data(byte[] buffer) {
    this.buffer = buffer;
  }  

  public byte[] getBuffer() {
    return buffer;
  }
}
