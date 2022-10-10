/*
 * 
 */
package inside.dumpster.outside;

import static inside.dumpster.outside.Bug.isBuggy;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */

@Buggy(because="the minus can remove too much", enabled = false)
public class BuggyClassDefaultFalseTest {
  @BeforeAll
  public static void beforeTest() {
    Bug.registerMXBean();
  }
  public int minus(int a, int b) {
    if(isBuggy(this)) {
      return a - b - 3;
    } else {
      return a - b;
    }
  }
  
  public BuggyClassDefaultFalseTest() {
  }

  @Test
  public void testSomeMethod() throws Exception {
    BuggyClassDefaultFalseTest example = new BuggyClassDefaultFalseTest();
    
    assertEquals(3, example.minus(9, 6));
    
    BuggyTestHelper.addBuggyClass("inside.dumpster.outside.BuggyClassDefaultFalseTest", Boolean.TRUE);
    
    assertNotEquals(3, example.minus(9, 6));
  }
}
