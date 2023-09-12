/*
 *
 */
package inside.dumpster.bl.auth;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class MustAcceptCookiesError extends Error {
  private final User user;
  MustAcceptCookiesError(User user) {
    this.user = user;
  }

  public User getUser() {
    return user;
  }
}
