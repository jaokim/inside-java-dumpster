/*
 * 
 */
package inside.dumpster.outside;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public interface BugBehaviourMXBean {
  public Boolean setBuggy(String clazz, Boolean buggy);
  public Boolean isBuggy(String clazz);
  public String[] getBuggyClasses();
}
