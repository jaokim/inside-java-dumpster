/*
 * 
 */
package inside.dumpster.backend.repository;

import inside.dumpster.backend.repository.data.Data;
import inside.dumpster.backend.repository.data.Id;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 * @param <D>
 */
public abstract class Repository<D extends Data> {
  public Id storeData(RenderedImage im) throws IOException {
    File file = File.createTempFile("data", ".jpg");
    ImageIO.write(im, "jpg", file);
//    Files.copy(is, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
    return new Id(file.getName());
  }
  
  public void removeData(Id id) {
    
  }
}
