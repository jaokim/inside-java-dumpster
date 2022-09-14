/*
 * 
 */
package inside.dumpster.backend.repository.data;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 * @param <D>
 */
public class DataUtils<D extends Data> {
  ThreadLocal<byte[]> threadLocalValue = new ThreadLocal<>();
      
  private final D data;
  public DataUtils(D data) {
    this.data = data;
  }
  
  public D convertToData(InputStream inputStream) {
    try {
      threadLocalValue.set(new byte[inputStream.available()]);
      byte[] buffer = threadLocalValue.get();
      inputStream.read(buffer);
      
      data.setBuffer(buffer);
      return data;
    } catch (IOException ex) {
      ex.printStackTrace();
      return data;
    }
  }
}

