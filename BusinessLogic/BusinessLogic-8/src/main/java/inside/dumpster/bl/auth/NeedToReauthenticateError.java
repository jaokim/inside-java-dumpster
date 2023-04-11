/*
 *
 */
package inside.dumpster.bl.auth;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class NeedToReauthenticateError extends Error {
  private final User user;
  NeedToReauthenticateError(User user) {
    this.user = user;
  }

  public User getUser() {
    return user;
  }
}
