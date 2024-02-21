/*
 *
 */
package inside.dumpster.bl.auth;

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
  private final ThreadLocal<User> loggedInUser = new ThreadLocal<>();
  private final boolean dummy;
  public Authenticator() {
    this.dummy = false;
  }
  Authenticator(boolean dummy) {
    this.dummy = dummy;
  }

  public User authenticateUser(String authType, String sessionId, Principal principal, Object session, Map<String,String[]> params) throws MustAcceptCookiesError {
    User user;
    if (loggedInUser.get() != null) {
      user = loggedInUser.get();
    } else {
      user = new User();
      user.setId(getId());
      user.setSession(session);
      user.setPrincipal(principal);
      user.setParams(params);
    }
    loggedInUser.set(user);

    if (!user.isCookieAccepted()) {
      if (Bug.isBuggy(this)) {
        throw new MustAcceptCookiesError(user);
      } else {
        // error is also thrown when authTicket is tried w/o being autheticated
      }
    }

    return user;
  }


  public User getLoggedInUser() {
    if(loggedInUser.get() != null) {
      return loggedInUser.get();
    } else {
      return null;
      //throw new NotAuthorizedError();
    }
  }

  private UUID getId() {
    return UUID.randomUUID();
  }

  public void reauthenticate(User user) {
    user.authTicket = UUID.randomUUID().toString();
  }

  public void clearSession() {
    loggedInUser.remove();
  }

}
