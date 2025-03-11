/*
 *
 */
package inside.dumpster.outside;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.ReflectionException;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class SettingsImpl implements SettingsMBean {

  private final Properties dumpsterProps = new Properties();
  private final static SettingsImpl settings = new SettingsImpl();
  public static SettingsImpl get() {
    return settings;
  }

  SettingsImpl() {
    for (Settings setting : Settings.values()) {
      String val = System.getenv(setting.key);
      if (val != null) {
        dumpsterProps.setProperty(setting.key, val);
      }
    }
    File dumpsterPropFile = new File("dumpster.properties");
    if (dumpsterPropFile.exists()) {
      Logger.getLogger(SettingsImpl.class.getName()).log(Level.INFO, "Loading properties from "+dumpsterPropFile.getAbsolutePath());
      try {
        dumpsterProps.load(new FileReader(dumpsterPropFile));

      } catch (IOException ex) {
        Logger.getLogger(SettingsImpl.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    for (Settings setting : Settings.values()) {
      String val = System.getProperty(setting.key);
      if (val != null) {
          System.out.println("Got property: "+setting.key + " = "+val);
        dumpsterProps.setProperty(setting.key, val);
      }
    }
    
  }

  String getProperty(String key) {
    return dumpsterProps.getProperty(key);
  }

  void setProperty(String key, String value) {
    dumpsterProps.setProperty(key, value);
  }

  @Override
  public Object getAttribute(String attribute) throws AttributeNotFoundException, MBeanException, ReflectionException {
    return dumpsterProps.getProperty(attribute);
  }

  @Override
  public void setAttribute(Attribute attribute) throws AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException {
    dumpsterProps.setProperty(attribute.getName(), attribute.getValue().toString());
  }

  @Override
  public AttributeList getAttributes(String[] attributes) {
    AttributeList list = new AttributeList();
    for (String attr : attributes) {
      try {
        list.add(new Attribute(attr, getAttribute(attr)));
      } catch (Exception ex) {
        Logger.getLogger(SettingsImpl.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    return list;
  }

  @Override
  public AttributeList setAttributes(AttributeList attributes) {
    AttributeList list = new AttributeList();
    for (Attribute attribute : attributes.asList()) {
      try {
        setAttribute(attribute);
        list.add(new Attribute(attribute.getName(), getAttribute(attribute.getName())));
      } catch (Exception ex) {
        // ignore
      }
    }
    return list;
  }

  @Override
  public Object invoke(String actionName, Object[] params, String[] signature) throws MBeanException, ReflectionException {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public MBeanInfo getMBeanInfo() {
    MBeanAttributeInfo[] attributes = new MBeanAttributeInfo[Settings.values().length];
    int i = 0;
    for (Settings set : Settings.values()) {
        attributes[i++] = new MBeanAttributeInfo(set.key, String.class.getName(), set.description, true, true, false);
    }
    MBeanInfo info = new MBeanInfo(this.getClass().getName(), "Settings for Inside the Java Dumspter Business Application",
            attributes,
            null,
            null,
            null);
    return info;
  }


}
