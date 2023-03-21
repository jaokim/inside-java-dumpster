/*
 * 
 */
package inside.dumpster.bl.auth;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class Authenticator {
  private final ThreadLocal<User> loggedInUser = new ThreadLocal<>();
  
  public User authenticateUser(String authType, String sessionId, Principal principal, Object session, Map<String,String[]> params) throws UnauthorizedException {
    if(loggedInUser.get() != null) {
      return loggedInUser.get();
    }
    User user = new User();
    user.setId(getId());
    user.setSession(session);
    user.setPrincipal(principal);
    user.setParams(params);
    
    loggedInUser.set(user);
    return user;
  }
  
  
  public User getLoggedInUser() {
    if(loggedInUser.get() != null) {
      return loggedInUser.get();
    } else {
      return null;
//      throw new NotAuthorizedError();
    }
  }
  
  private UUID getId() {
    return UUID.randomUUID();
  }

}
