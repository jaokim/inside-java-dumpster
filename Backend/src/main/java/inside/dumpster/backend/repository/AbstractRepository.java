/*
 *
 */
package inside.dumpster.backend.repository;

import inside.dumpster.backend.repository.data.Data;
import inside.dumpster.backend.repository.data.Id;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 * @param <D>
 */
public abstract class AbstractRepository<D extends Data> {
  private static File MAIN_DIR;
  static {
    try {
      Properties prop = new Properties();
      prop.load(new FileReader("dumpster.properties"));
      MAIN_DIR = new File(prop.getProperty("datadir"));
    } catch (Exception ex) {
      Logger.getLogger(AbstractRepository.class.getName()).log(Level.SEVERE, null, ex);
      MAIN_DIR = new File(System.getProperty("java.io.tmpdir"));
    }
  }
  public StoredData storeData(D im) throws IOException {
    File file = File.createTempFile("data", ".blob");
    try (
      ByteArrayInputStream is = new ByteArrayInputStream(im.getBuffer())) {
      Files.copy(is, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
      return new StoredData(file.length(), new Id(file.getAbsolutePath()));
    }
  }

  protected String generateRandomString() {
    int leftLimit = 97; // letter 'a'
    int rightLimit = 122; // letter 'z'
    int targetStringLength = 10;
    Random random = new Random(System.nanoTime());

    String generatedString = random.ints(leftLimit, rightLimit + 1)
            .limit(targetStringLength)
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            .toString();
    return generatedString;
  }
  private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HH");
  protected File createFile(final String name) throws IOException {
    File file;
    File dateDir = new File(MAIN_DIR, sdf.format(new Date()));
    if (!dateDir.exists()) {
      dateDir.mkdir();
    }

    do {
      File dir = new File(dateDir, generateRandomString());
      if (!dir.exists()) {
        dir.mkdir();
      }
      file = new File(dir, name);
    } while (file.exists());

//    if (file.createNewFile()) {
      return file;
//    }
//    throw new IOException("Couldn't create file: "+file.getAbsolutePath());
  }
  public void removeData(Id id) {
    File f = new File(id.toString());
    f.delete();
  }
}
