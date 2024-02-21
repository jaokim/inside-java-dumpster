/*
 *
 */
package inside.dumpster.uploadtext;

import inside.dumpster.backend.Backend;
import inside.dumpster.backend.BackendException;
import inside.dumpster.backend.database.Database;
import inside.dumpster.client.Payload;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class UploadTextPayload extends Payload {

  @Override
  public InputStream getInputStream() {
    if (super.getInputStream() == null) {
      Database db = Backend.builder().build().getDatabase();
      InputStream stream;
      try {
        stream = db.getTextData(getSrcPort());
      } catch (BackendException ex) {
        Logger.getLogger(UploadTextPayload.class.getName()).log(Level.SEVERE, null, ex);
        return null;
      }
      return stream;
    } else {
      return super.getInputStream();
    }
  }

}
