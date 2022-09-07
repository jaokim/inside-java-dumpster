/*
 * 
 */
package inside.dumpster.backend.repository.data;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 * @param <D>
 */
public class DataUtils<D extends Data> {

  public D convertToData(InputStream inputStream) {
    try {

      byte[] buffer = new byte[inputStream.available()];
      inputStream.read(buffer);
      return (D)new Data(buffer);
    } catch (IOException ex) {
      ex.printStackTrace();
      return null;
    }
  }
}

