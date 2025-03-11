/*
 *
 */
package inside.dumpster.outside;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public enum Settings {
  DATABASE_CONNECTION_URL(
          "dumpster.databaseConnectionUrl", 
          "URL for database connection. Example: \"jdbc:derby://localhost:1527/dumpster\""
  ),
  SERVICE_LOOKUP(
          "service_lookup", 
          "Service lookup mapping. Example: \"foo.*:FooLogic;bar:BarLogic\", will resolve any destination starting with foo \"foo\" to inside.dumpster.FooLogic.FooLogicService."
  );

  final String key;
  final String description;
  Settings(String key, String description) {
    this.key = key;
    this.description = description;
  }

  public String getKey() {
    return key;
  }

  public String get() {
    return SettingsImpl.get().getProperty(key);
  }
  public void set(String value) {
    SettingsImpl.get().setProperty(key, value);
  }

  /**
   * Check if value is non-null, and non-empty
   * @return
   */
  public boolean isSet() {
    return get() != null && !get().isEmpty();
  }
}
