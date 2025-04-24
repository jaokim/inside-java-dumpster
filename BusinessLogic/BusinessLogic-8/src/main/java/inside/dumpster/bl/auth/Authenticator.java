/*
 *
 */
package inside.dumpster.bl.auth;

import inside.dumpster.backend.BackendException;
import inside.dumpster.outside.Bug;
import inside.dumpster.outside.Buggy;
import java.security.Principal;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
@Buggy(because = "an exception is used to control program flow", enabled = true)
public class Authenticator {
  private final ThreadLocal<User> loggedInUser = new ThreadLocal();
  public Authenticator() {
  }
  Authenticator(boolean dummy) {
  }

  public void loginUser(String authType, String sessionId, Principal principal, Object session, Map<String,String[]> params) throws BackendException {
    User user;
    user = new User();
    user.setId(getId());
    user.setSession(session);
    user.setPrincipal(principal);
    user.setParams(params);
    UserCache.getInstance().add(user);
    loggedInUser.set(user);
  }

  public User getLoggedInUser() {
    final User user = loggedInUser.get();
    if(user != null) {
      if (!user.isCookieAccepted()) {
        if (Bug.isBuggy(this)) {
          throw new MustAcceptCookiesError(user);
        } else {
          // error is also thrown when authTicket is tried w/o being autheticated
        }
      }
      return loggedInUser.get();
    } else {
      return null;//throw new NotAuthorizedError();
    }
  }

  private UUID getId() {
    return UUID.randomUUID();
  }

  public String getAuthTicket(User user) {
    return user.authTicket = user.getAuthTicket();// UUID.randomUUID().toString();
  }

  public void clearSession() {
    loggedInUser.remove();
  }

}
