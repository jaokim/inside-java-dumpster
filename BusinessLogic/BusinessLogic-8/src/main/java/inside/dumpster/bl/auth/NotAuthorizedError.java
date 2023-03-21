/*
 * 
 */
package inside.dumpster.bl.auth;

/**
 * Thrown if no user is logged in.
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class NotAuthorizedError extends Error {

  /**
   * Creates a new instance of <code>UnauthorizedUserError</code> without detail
   * message.
   */
  public NotAuthorizedError() {
  }

  /**
   * Constructs an instance of <code>UnauthorizedUserError</code> with the
   * specified detail message.
   *
   * @param msg the detail message.
   */
  public NotAuthorizedError(String msg) {
    super(msg);
  }
}
