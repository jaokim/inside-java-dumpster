/*
 * 
 */
package inside.dumpster.backend.repository;

import inside.dumpster.backend.repository.data.Id;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class StoredData {
  private final long length;
  private final Id id;

  StoredData(long length, Id id) {
    this.length = length;
    this.id = id;
  }
  

  public long getLength() {
    return length;
  }

  public Id getId() {
    return id;
  }

}
