/*
 *
 */
package inside.dumpster.bl.auth;

import java.io.OutputStream;
import java.security.Principal;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class User {
  private static final Logger logger = Logger.getLogger(Authenticator.class.getName());
  private UUID id;
  private Object session;
  private Principal principal;
  private Object params;
  private boolean cookieAccepted;
  private OutputStream userData;
  private long timeout;
  String authTicket;

  public User() {
    authTicket = null;
  }

  void setId(UUID id) {
    this.id = id;
  }

  public void setAuthTicket(String authTicket) {
    this.authTicket = authTicket;
  }

  public String getAuthTicket() throws MustAcceptCookiesError {
    return authTicket;
  }

  public boolean isCookieAccepted() {
    return cookieAccepted;
  }

  public void setCookieAccepted(boolean cookieAccepted) {
    this.cookieAccepted = cookieAccepted;
  }
  public UUID getId() {
    return id;
  }

  void setSession(Object session) {
    this.session = session;
  }

  void setParams(Map<String, String[]> params) {
    this.params = params;
  }

  void setPrincipal(Principal principal) {
    this.principal = principal;
  }

  public void setUserData(OutputStream is) {
    this.userData = is;
  }

  public void setTimeout(long timeout) {
    this.timeout = timeout;
  }

  public long getTimeout() {
    return timeout;
  }

  public OutputStream getUserData() {
    return userData;
  }

  public Principal getPrincipal() {
    return principal;
  }

  public Object getParams() {
    return params;
  }

  public Object getSession() {
    return session;
  }

}
