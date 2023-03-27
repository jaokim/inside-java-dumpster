/*
 *
 */
package inside.dumpster.outside;

import static java.util.Arrays.binarySearch;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class BugBehaviourTest {

  public BugBehaviourTest() {
  }

  /**
   * Test of setBuggy method, of class BugBehaviour.
   */
  @Test
  public void testSetBuggy() {
  }

  /**
   * Test of isBuggy method, of class BugBehaviour.
   */
  @Test
  public void testIsBuggy() {
  }

  /**
   * Test of getBuggyClasses method, of class BugBehaviour.
   */
  @Test
  public void testGetBuggyClasses() {
    BugBehaviour bugBehaviour = new BugBehaviour();
    String cls[] = bugBehaviour.getBuggyClasses();
    boolean found = false;
    for (String c : cls) {
      System.out.println("Buggy class found: " + c);
      if (c.equals(BuggyClassTest.class.getName())) {
        found = true;
      }
    }
    assertTrue(cls.length > 0, "No buggy classes found, should be atleast one");
    assertTrue(found, "Couldn't find class: " + BuggyClassTest.class.getName());
  }

}
