/*
 *
 */
package inside.dumpster.util;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public interface ThreadHelper {
    
  public static Thread createThread(Runnable runnable, String key) {
      return new ThreadHelperImpl(() -> key).newThread(runnable);
  }
}
