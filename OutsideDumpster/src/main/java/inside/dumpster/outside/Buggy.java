/*
 * 
 */
package inside.dumpster.outside;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a buggy method.
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Buggy {
  /** Reason why this code is buggy. */
  public String because();
  /** Is the Buggy code enabled by default?  */
  public boolean enabled() default true;
}
