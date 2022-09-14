/*
 * 
 */
package inside.dumpster.backend.repository.data;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public interface Data {
  public void setBuffer(byte[] buffer);

  public byte[] getBuffer();
}
