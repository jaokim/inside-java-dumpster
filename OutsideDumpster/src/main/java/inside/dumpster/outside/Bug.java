/*
 * 
 */
package inside.dumpster.outside;

import java.lang.management.ManagementFactory;
import java.util.concurrent.atomic.AtomicReference;
import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class Bug {
  private static final AtomicReference<BugBehaviour> BUG_BEHAVIOUR = new AtomicReference<>();
  public static boolean isBuggy(Object object) {
    final Class clazz = object.getClass();
    if(BUG_BEHAVIOUR.get() != null) {
      System.out.println("We have an overriden behavour: "+clazz.getName());
      Boolean overridden = BUG_BEHAVIOUR.get().isBuggy(clazz.getName());
      if(overridden != null) {
        return overridden;
      }
    }
    System.out.println("Checking for annotation for: "+clazz.getName());
    if (clazz.isAnnotationPresent(Buggy.class)) {
      Buggy buggy = (Buggy)clazz.getAnnotation(Buggy.class);
      final Boolean enabled = buggy.enabled();
      System.out.println("Annotation found: "+clazz.getName()+  "is enabled: "+(enabled));
      return enabled;
    } else {
      return false;
    }
  }
  
  public static void registerMXBean() {
    try {
      MBeanServer mbs
              = ManagementFactory.getPlatformMBeanServer();

      ObjectName mxbeanName = new ObjectName("inside.dumpster.outside:type=BugBehaviour");
      if(!mbs.isRegistered(mxbeanName)) {
        BUG_BEHAVIOUR.set(new BugBehaviour());
        System.out.println("Settging bug behav: "+BUG_BEHAVIOUR.get());
        mbs.registerMBean(BUG_BEHAVIOUR.get(), mxbeanName);
      }
    } catch (MalformedObjectNameException | InstanceAlreadyExistsException | MBeanRegistrationException | NotCompliantMBeanException ex) {
      throw new RuntimeException(ex);
    }
  }

  
}
