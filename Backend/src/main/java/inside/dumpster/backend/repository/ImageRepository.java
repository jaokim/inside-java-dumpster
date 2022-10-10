/*
 * 
 */
package inside.dumpster.backend.repository;

import inside.dumpster.backend.repository.data.Id;
import inside.dumpster.backend.repository.data.LImage;
import inside.dumpster.backend.repository.data.Text;
import inside.dumpster.outside.Bug;
import inside.dumpster.outside.Buggy;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
@Buggy(because="the thread local var isn't removed")
public class ImageRepository extends AbstractRepository<LImage> {
   private static ScheduledThreadPoolExecutor textUploadThreadPool = new ScheduledThreadPoolExecutor(20);

  @Override
  public StoredData storeData(LImage im) throws IOException {
    if(Bug.isBuggy(this)) {
//      inputStream = im.getInputStream();
    }
    return super.storeData(im);
  }

  static class TextUploader implements Runnable {
    private static final ThreadLocal<LImage> imageTempStore = new ThreadLocal<>();
    private final LImage image;
    private final File file;
    
    public TextUploader(File file, LImage image) {
      this.file = file;
      this.image = image;
    }
    @Override
    public void run() {
      imageTempStore.set(image);
      storeData();
    }
    
    public void storeData() {
      LImage im = imageTempStore.get();
      try {
        ImageIO.write(im.getRenderedImage(), "jpg", file);
      } catch (IOException ex) {
        Logger.getLogger(ImageRepository.class.getName()).log(Level.SEVERE, null, ex);
      }
      
      if(!Bug.isBuggy(this)) {
        imageTempStore.remove();
      }
    }
  }

}
