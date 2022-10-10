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

@Buggy(because="the add can add too much")
public class BuggyClassTest {
  @BeforeAll
  public static void beforeTest() {
    Bug.registerMXBean();
  }

  public int add(int a, int b) {
    if(isBuggy(this)) {
      return a + b + 3;
    } else {
      return a + b;
    }
  }
  
  public BuggyClassTest() {
  }

  @Test
  public void testSomeMethod() throws Exception {
    BuggyClassTest example = new BuggyClassTest();
    
    assertEquals(12, example.add(4, 5));
    
    BuggyTestHelper.addBuggyClass("inside.dumpster.outside.BuggyClassTest", Boolean.TRUE);
    
    assertNotEquals(9, example.add(4, 5));
  }
}
