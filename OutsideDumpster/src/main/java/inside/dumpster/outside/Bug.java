/*
 *
 */
package inside.dumpster.outside;

import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;
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
  private static final Logger logger = Logger.getLogger(Bug.class.getName());

  public static boolean isBuggy(Object object) {
    final Class clazz = object.getClass();
    return isBuggy(clazz);
  }
  public static boolean isBuggy(final Class clazz) {
    if (BUG_BEHAVIOUR.get() != null) {
      Boolean overridden = BUG_BEHAVIOUR.get().possiblyBuggyClasses.get(clazz.getName());
      if(overridden != null) {
        logger.log(Level.FINE, "Returning isBuggy={0} for {1}", new Object[]{overridden, clazz.getName()});
        return overridden;
      }
    }
    if (clazz.isAnnotationPresent(Buggy.class)) {
      if (System.getProperty("NOBUGS") != null) {
        logger.info(String.format("Property NOBUGS set, returning false for %s", clazz.getName()));
        return false;
      }
      Buggy buggy = (Buggy)clazz.getAnnotation(Buggy.class);
      final Boolean enabled = buggy.enabled();
      logger.log(Level.FINE, "Returning isBuggy/enabled={0} for {1}", new Object[]{enabled, clazz.getName()});
      return enabled;
    } else {
      logger.log(Level.FINE, "Returning isBuggy false for {1} (not a class with @Buggy annotation)", new Object[]{clazz.getName()});
      return false;
    }
  }

  public static void registerMXBean() {
    try {
      MBeanServer mbs
              = ManagementFactory.getPlatformMBeanServer();

      ObjectName mxbeanName = new ObjectName("inside.dumpster.outside:type=BugBehaviour");
      if(!mbs.isRegistered(mxbeanName)) {
        final BugBehaviour bugBehaviour = new BugBehaviour();
        BUG_BEHAVIOUR.set(bugBehaviour);
        logger.log(Level.CONFIG, "Registering Bug MXBean handler");
        bugBehaviour.loadFromProperties();
        mbs.registerMBean(bugBehaviour, mxbeanName);
      }

      ObjectName mxbeanSettsingsName = new ObjectName("inside.dumpster.outside:type=Settings");
      if(!mbs.isRegistered(mxbeanSettsingsName)) {
        final SettingsImpl settings = new SettingsImpl();
        mbs.registerMBean(settings, mxbeanSettsingsName);
      }
    } catch (MalformedObjectNameException | InstanceAlreadyExistsException | MBeanRegistrationException | NotCompliantMBeanException ex) {
      throw new RuntimeException(ex);
    }

  }

  public static BugBehaviourMXBean getMXBean() {
    registerMXBean();
    return BUG_BEHAVIOUR.get();
  }

}
