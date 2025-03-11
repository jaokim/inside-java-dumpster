/*
 *
 */
package inside.dumpster.util;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
class ThreadHelperImpl implements ThreadFactory {
  private final Supplier<String> keysupplier;
  private final AtomicInteger counter;
  public ThreadHelperImpl() {
      counter = new AtomicInteger();
      this.keysupplier = () -> "Thread #" + counter.incrementAndGet();
  }

  public ThreadHelperImpl(Supplier<String> keysupplier) {
    counter = null;
    this.keysupplier = keysupplier;
  }
  
  @Override
  public Thread newThread(Runnable r) {
    Thread t = new Thread(r);
    t.setName(keysupplier.get());
    return t;
  }

}
