/*
 *
 */
package inside.dumpster.outside;

import static inside.dumpster.outside.BuggyClassesProcessor.BUGGY_CLASSES_RESOURCENAME;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
@Buggy(because = "this test should return it")
public class BugBehaviour implements BugBehaviourMXBean {
  private static final Logger logger = Logger.getLogger(BugBehaviour.class.getName());
  private final Map<String, Boolean> possiblyBuggyClasses = new HashMap<>();

  public BugBehaviour() {

  }

  @Override
  public Boolean setBuggy(String clazz, Boolean buggy) {
    return possiblyBuggyClasses.put(clazz, buggy);
  }

  @Override
  public Boolean isBuggy(String clazz) {
    return possiblyBuggyClasses.get(clazz);

  }

  @Override
  public String[] getBuggyClasses() {
    Set<String> buggyClasses = new HashSet<>();

    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    findBuggyClasses(buggyClasses, classLoader);
    buggyClasses.addAll(possiblyBuggyClasses.keySet());

    return buggyClasses.toArray(new String[buggyClasses.size()]);
  }

  private void findBuggyClasses(Set<String> buggyClasses, ClassLoader classLoader) {
    logger.log(Level.FINE, "ClassLoader: {0}", classLoader.toString());
    try {
      Enumeration<URL> r = classLoader.getResources(BUGGY_CLASSES_RESOURCENAME);
      URL u;
      while (r.hasMoreElements() && (u = r.nextElement()) != null) {
        logger.log(Level.FINE, "Reading bug classes from: {0}", u.toString());
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(u.openStream()))) {
          reader.lines().forEach((cls) -> buggyClasses.add(cls));
        } catch (Exception ex) {
          logger.log(Level.WARNING, ex, ()->("Exception while reading bug classes"));
        }
      }
    } catch (IOException ex) {
      Logger.getLogger(BugBehaviour.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
}
