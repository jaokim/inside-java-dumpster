/*
 *
 */
package inside.dumpster.bl.auth;

import inside.dumpster.backend.Backend;
import inside.dumpster.backend.BackendException;
import inside.dumpster.backend.database.DatabaseImpl;
import inside.dumpster.backend.utils.Utils;
import inside.dumpster.outside.Bug;
import inside.dumpster.outside.Buggy;
import java.io.ByteArrayOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
@Buggy(because = "is it", enabled = true)
public class CachedUser {
  private static final Logger logger = Logger.getLogger(CachedUser.class.getName());
  final User user;
  private String base64UserImage;
  private char[] userInfo;

  public CachedUser(User user) {
    this.user = user;
    user.setTimeout(System.currentTimeMillis() + (20 * 60 * 1000));
    if (Bug.isBuggy(CachedUser.class))  {
      preloadUserData();
    }
}

  private void preloadUserData() {
    try {
      base64UserImage = new String(Utils.inputStreamToOutputStream(Backend.getInstance().getDatabase().getPayloadData(DatabaseImpl.DataType.UserImage, user.getId().toString())).toString());
      ByteArrayOutputStream baos = Utils.inputStreamToOutputStream(Backend.getInstance().getDatabase().getPayloadData(DatabaseImpl.DataType.Text, user.getId().toString()));
      userInfo = new char[baos.size()];
      int cnt = 0;
      for (byte b : baos.toByteArray()) {
        userInfo[cnt++] = (char)b;
      }
      logger.fine("Preloaded Cached User data");
    } catch (BackendException ex) {
      Logger.getLogger(CachedUser.class.getName()).log(Level.INFO, null, ex);
      Logger.getLogger(CachedUser.class.getName()).log(Level.SEVERE, null, ex);
    }

  }


  public String getBase64UserImage() {
    if (base64UserImage != null) {
      return base64UserImage;
    } else {
      try {
        return Utils.inputStreamToOutputStream(Backend.getInstance().getDatabase().getPayloadData(DatabaseImpl.DataType.UserImage, user.getId().toString())).toString();
      } catch (BackendException ex) {
        Logger.getLogger(CachedUser.class.getName()).log(Level.SEVERE, null, ex);
        return null;
      }
    }

  }
}
