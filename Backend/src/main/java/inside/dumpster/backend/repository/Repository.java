/*
 * 
 */
package inside.dumpster.backend.repository;

import inside.dumpster.backend.repository.data.Data;
import inside.dumpster.backend.repository.data.Id;
import java.io.IOException;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 * @param <D>
 */
public interface Repository<D extends Data> {
  public StoredData storeData(D im) throws IOException;
  
  public void removeData(Id id);
}
