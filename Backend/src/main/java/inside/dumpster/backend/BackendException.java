/*
 *
 */
package inside.dumpster.backend;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class BackendException extends Exception {

  public BackendException(Throwable ex) {
    super(ex);
  }
  public BackendException(String mess, Throwable ex) {
    super(mess, ex);
  }
  public BackendException(String mess) {
    super(mess);
  }


}
