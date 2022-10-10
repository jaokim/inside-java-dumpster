/*
 * 
 */
package inside.dumpster.backend.repository.data;

import java.io.InputStream;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class Text extends AbstractData {
  private InputStream inputStream;
  public Text(StringBuilder builder) {
    this.buffer = String.valueOf(builder).getBytes();
  }
  
  public Text(InputStream inputStream) {
    this.inputStream = inputStream;
  }

  public InputStream getInputStream() {
    return inputStream;
  }

  public boolean hasInputStream() {
    return inputStream != null;
  }
  
}
