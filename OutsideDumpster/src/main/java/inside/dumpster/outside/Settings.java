/*
 *
 */
package inside.dumpster.outside;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public enum Settings {
  DATABASE_CONNECTION_URL("dumpster.databaseConnectionUrl");

  final String key;

  Settings(String key) {
    this.key = key;
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
