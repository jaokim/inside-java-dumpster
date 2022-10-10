/*
 * 
 */
package inside.dumpster.outside;

import java.lang.management.ManagementFactory;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.ReflectionException;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class BuggyTestHelper {
  public static void addBuggyClass(String clazz, Boolean enabled) throws MalformedObjectNameException, InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException, InstanceNotFoundException, MBeanException, ReflectionException {
    try {
      MBeanServer mbs
              = ManagementFactory.getPlatformMBeanServer();

      ObjectName mxbeanName = new ObjectName("inside.dumpster.outside:type=BugBehaviour");
      if(mbs.isRegistered(mxbeanName)) {
        String[] a = new String[]{""};
        mbs.invoke(mxbeanName, "setBuggy", new Object[]{clazz, enabled}, new String[]{java.lang.String.class.getName(), Boolean.class.getName()});
      }
    } catch (MalformedObjectNameException | MBeanRegistrationException ex) {
      throw new RuntimeException(ex);
    }
  }
}
