/*
 *
 */
package inside.dumpster.backend.repository.data;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class Text extends AbstractData {
  private final InputStream inputStream;
  private StringBuilder builder;
  public Text(StringBuilder builder) {
    this.inputStream = new ByteArrayInputStream(builder.toString().getBytes());
    this.builder = builder;
  }

  public StringBuilder getBuilder() {
    return builder;
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
