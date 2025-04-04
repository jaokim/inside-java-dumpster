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
import java.io.OutputStream;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
@Buggy(because = "it stores large user data in a long living session map")
public class UserCache extends ConcurrentHashMap<String, CachedUser> {
  private static final Logger logger = Logger.getLogger(UserCache.class.getName());
  private static final UserCache USER_CACHE = new UserCache();
  private final Thread t;
  static UserCache getInstance() {
    return USER_CACHE;
  }

  private UserCache() {
    t = new Thread(() -> {
      while (true) {
        int usersRemoved = 0;
        final long now = System.currentTimeMillis();
        for (String sessionId : this.keySet()) {
          if (this.get(sessionId) != null) {
            User usr = this.get(sessionId).user;
            if (usr.getTimeout() < now) {
              this.remove(sessionId);
              usersRemoved++;
            }
          }
        }
        if (usersRemoved > 0) {
          logger.info(String.format("Removed %d users, %d users still logged in", usersRemoved, this.size()));
        }
        try {
          Thread.sleep(10_000);
          logger.info(String.format("%d users still logged in", this.size()));
        } catch (InterruptedException ex) {
          Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    });
    t.setName("Dumpster user cache cleaner");
    t.setDaemon(true);
    t.start();
  }

  public void add(User user) throws BackendException {
    //if (Bug.isBuggy(UserCache.class))  {
      try {
        user.setTimeout(System.currentTimeMillis() + (20 * 60 * 1000));
        CachedUser cachedUser  = new CachedUser(user);
        this.put(user.getId().toString(), cachedUser);

      } catch(OutOfMemoryError oome) {
        logger.severe("OutOfMemory: trying to clean up some users");
        String session = this.keys().nextElement();
        this.remove(session);
        session = this.keys().nextElement();
        this.remove(session);
        // -XX:+CrashOnOutOfMemoryError
        throw oome;
      }
    //} else {
    //))  this.put(user.getId().toString(), new CachedUser(user, null));
    //}
  }
}
