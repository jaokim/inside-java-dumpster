/*
 *
 */
package inside.dumpster.outside;

import javax.management.DynamicMBean;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public interface BugBehaviourMXBean {
  public String setBuggy(String clazz, Boolean buggy);
  public Boolean isBuggy(String clazz);
  public String[] getBuggyClasses();
}
