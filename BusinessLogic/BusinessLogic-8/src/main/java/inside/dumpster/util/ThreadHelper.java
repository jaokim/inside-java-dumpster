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
    return new ThreadHelperImpl().newThread(runnable, key);
  }

  public Thread newThread(Runnable runnable, String key);
}
