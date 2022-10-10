/*
 * 
 */
package inside.dumpster.backend.repository;

import inside.dumpster.backend.repository.data.Data;
import inside.dumpster.backend.repository.data.Id;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 * @param <D>
 */
public abstract class AbstractRepository<D extends Data> {
  public StoredData storeData(D im) throws IOException {
    File file = File.createTempFile("data", ".blob");
    try (
      ByteArrayInputStream is = new ByteArrayInputStream(im.getBuffer())) {
      Files.copy(is, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
      return new StoredData(file.length(), new Id(file.getAbsolutePath()));
    }
  }
  
  public void removeData(Id id) {
    File f = new File(id.toString());
    f.delete();
  }
}
