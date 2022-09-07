/*
 * 
 */
package inside.dumpster.backend.repository;

import inside.dumpster.backend.repository.data.Data;
import inside.dumpster.backend.repository.data.Id;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 * @param <D>
 */
public abstract class Repository<D extends Data> {
  public Id storeData(D data) {
    return null;
  }
  
  public void removeData(Id id) {
    
  }
}
