/*
 *
 */
package inside.dumpster.outside;

import static inside.dumpster.outside.BuggyClassesProcessor.BUGGY_CLASSES_RESOURCENAME;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.ReflectionException;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
@Buggy(because = "this test should return it")
public class BugBehaviour implements BugBehaviourMXBean {

  private static final String FOUNDBUGSPROPERTIES = "foundbugs.properties";
  private static final Logger logger = Logger.getLogger(BugBehaviour.class.getName());
  final Map<String, Boolean> possiblyBuggyClasses = new HashMap<>();

  public BugBehaviour() {

  }

  @Override
  public String setBuggy(String clazz, Boolean isBuggy) {
    String result;
    possiblyBuggyClasses.put(clazz, isBuggy);
    saveToProperties(clazz, isBuggy);
    try {
      Class clazzClass = Class.forName(clazz);
      if (clazzClass == null) {
        result = "Class: \"" + clazz + "\" not found.";
      } else if (clazzClass.isAnnotationPresent(Buggy.class)) {
        Buggy buggy = (Buggy) clazzClass.getAnnotation(Buggy.class);
        result = "Buggy because " + buggy.because();
      } else {
        result = ".";
      }
    } catch (ClassNotFoundException ex) {
      result = "Class: \"" + clazz + "\" not found. Make sure you entered the class name, and not a method f.i.";
    }

    return result;
  }

  @Override
  public Boolean isBuggy(String clazz) {
    return possiblyBuggyClasses.get(clazz);

  }

  @Override
  public String[] getBuggyClasses() {
    final Set<String> buggyClasses = new HashSet<>();

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
          logger.log(Level.WARNING, ex, () -> ("Exception while reading bug classes"));
        }
      }
    } catch (IOException ex) {
      Logger.getLogger(BugBehaviour.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  private void saveToProperties(String clazz, Boolean isBuggy) {
    final File props = new File(FOUNDBUGSPROPERTIES);
    final Properties properties = new Properties();

    if (props.exists() && props.canRead() && props.canWrite()) {
      try (InputStream bugs = new BufferedInputStream(new FileInputStream(props))) {
        properties.load(bugs);

        properties.setProperty(clazz, isBuggy.toString());
      } catch (IOException ex) {
        System.out.println("Couldn't load foundbugs properties file: " + props.getAbsolutePath());
      }
      try (OutputStream stream = new FileOutputStream(FOUNDBUGSPROPERTIES)) {
        properties.store(stream, new Date().toLocaleString());
      } catch (FileNotFoundException ex) {
        Logger.getLogger(BugBehaviour.class.getName()).log(Level.SEVERE, null, ex);
      } catch (IOException ex) {
        Logger.getLogger(BugBehaviour.class.getName()).log(Level.SEVERE, null, ex);
      }

    } else {
      System.out.println("Couldn't find/read/write foundbugs properties file: " + props.getAbsolutePath());
    }
  }

  void loadFromProperties() {
    final File props = new File(FOUNDBUGSPROPERTIES);
    final Properties properties = new Properties();

    if (props.exists() && props.canRead()) {
      try (InputStream bugs = new BufferedInputStream(new FileInputStream(props))) {
        System.out.println("Loading bugs from " + FOUNDBUGSPROPERTIES);
        properties.load(bugs);
        properties.store(System.out, "Current identifed bugs");
        for (String clazz : properties.stringPropertyNames()) {
          possiblyBuggyClasses.put(clazz, Boolean.getBoolean(properties.getProperty(clazz)));
        }
      } catch (IOException ex) {
        System.out.println("Couldn't fing found bugs properties file: " + props.getAbsolutePath());
      }
    } else {
      System.out.println("Couldn't find foundbugs properties file: " + props.getAbsolutePath());
    }
  }

}
