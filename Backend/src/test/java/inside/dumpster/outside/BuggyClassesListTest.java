package inside.dumpster.outside;

/*
 * 
 */

import inside.dumpster.backend.repository.TextRepository;
import static java.util.Arrays.binarySearch;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class BuggyClassesListTest {
  @Test
  public void testListTest() {
    Bug.registerMXBean();
    BugBehaviour bb = new BugBehaviour();
//    assertFalse(bb.isBuggy(BuggyClassDefaultFalseTest.class.getName()));
//    assertTrue(bb.isBuggy(BuggyClassTest.class.getName()));
    String[] cls = bb.getBuggyClasses();
//    assertTrue(binarySearch(cls, TextRepository.class.getName()) >= 0);
  }
}
