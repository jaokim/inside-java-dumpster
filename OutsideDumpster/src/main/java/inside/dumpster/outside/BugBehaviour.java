/*
 * 
 */
package inside.dumpster.outside;

import static inside.dumpster.outside.BuggyClassesProcessor.BUGGY_CLASSES_RESOURCENAME;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
public class BugBehaviour implements BugBehaviourMXBean {
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
    System.out.println("BuggyClassas:"+buggyClasses.size());
    System.out.println("Reading fdrom: "+this.getClass().getResource(BUGGY_CLASSES_RESOURCENAME));
    try(BufferedReader reader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(BUGGY_CLASSES_RESOURCENAME)))) {
      reader.lines().forEach( (cls) -> buggyClasses.add(cls));
    } catch (Exception ex) {
      System.out.println("Exception reading buggyclasses props: "+ex.getMessage());
    }
    buggyClasses.addAll(possiblyBuggyClasses.keySet());
    System.out.println("BuggyClassas:"+buggyClasses.size());
    return buggyClasses.toArray(new String[buggyClasses.size()]);
  }
  
}
