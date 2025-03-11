/*
 *
 */
package inside.dumpster.backend.utils;

import inside.dumpster.backend.BackendException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class Utils {

  public static ByteArrayOutputStream inputStreamToOutputStream(InputStream bis) throws BackendException {
    try {
      ByteArrayOutputStream buffer = new ByteArrayOutputStream();
      int nRead;
      byte[] data = new byte[4];
      while ((nRead = bis.read(data, 0, data.length)) != -1) {
        buffer.write(data, 0, nRead);
      }
      buffer.flush();
      return buffer;
    } catch (IOException ex) {
      throw new BackendException("Error reading byte data", ex);
    }
  }
}
