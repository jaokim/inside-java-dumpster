/*
 *
 */
package inside.dumpster.util;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
class ThreadHelperImpl implements ThreadHelper{

  public ThreadHelperImpl() {
  }

  @Override
  public Thread newThread(Runnable runnable, String key) {
    return new Thread(runnable, key);
  }

}
