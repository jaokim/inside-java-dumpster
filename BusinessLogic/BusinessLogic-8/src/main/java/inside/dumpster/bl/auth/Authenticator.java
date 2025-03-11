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
  public Authenticator() {
  }
  Authenticator(boolean dummy) {
  }

  public User authenticateUser(String authType, String sessionId, Principal principal, Object session, Map<String,String[]> params) throws MustAcceptCookiesError, BackendException {
    User user;
    user = new User();
    user.setId(getId());
    user.setSession(session);
    user.setPrincipal(principal);
    user.setParams(params);
    if (!user.isCookieAccepted()) {
      if (Bug.isBuggy(this)) {
        throw new MustAcceptCookiesError(user);
      } else {
        // error is also thrown when authTicket is tried w/o being autheticated
      }
    }
    UserCache.getInstance().add(user);
    return user;
  }

  public User getLoggedInUser() {
//    if(loggedInUser.get() != null) {
//      return loggedInUser.get();
//    } else {
//      return null;
//      //throw new NotAuthorizedError();
//    }
    return null;
  }

  private UUID getId() {
    return UUID.randomUUID();
  }

  public void reauthenticate(User user) {
    user.authTicket = UUID.randomUUID().toString();
  }

  public void clearSession() {
  }

}
