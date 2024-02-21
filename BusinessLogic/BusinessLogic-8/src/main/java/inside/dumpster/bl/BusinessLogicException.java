/*
 *
 */
package inside.dumpster.bl;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class BusinessLogicException extends Exception {

    public BusinessLogicException() {
      super();
    }
    public BusinessLogicException(String message, Exception e) {
      super(message, e);
    }
    public BusinessLogicException(Exception e) {
      super(e);
    }

}
