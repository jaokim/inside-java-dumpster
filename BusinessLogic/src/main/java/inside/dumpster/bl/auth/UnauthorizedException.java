/*
 * 
 */
package inside.dumpster.bl.auth;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class UnauthorizedException extends Exception {

  /**
   * Creates a new instance of <code>UnauthorizedUserException</code> without
   * detail message.
   */
  public UnauthorizedException() {
  }

  /**
   * Constructs an instance of <code>UnauthorizedUserException</code> with the
   * specified detail message.
   *
   * @param msg the detail message.
   */
  public UnauthorizedException(String msg) {
    super(msg);
  }
}
