/*
 *
 */
package inside.dumpster.backend;

import inside.dumpster.backend.database.Database;
import inside.dumpster.backend.database.DatabaseImpl;
import inside.dumpster.backend.database.DummyDatabaseImpl;
import inside.dumpster.backend.repository.ImageRepository;
import inside.dumpster.outside.Settings;
import java.util.logging.Logger;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class Backend {
  private static final Logger logger = Logger.getLogger(Backend.class.getName());
  private final Database database;
  private final ImageRepository imageRepository;

  public Backend(Database database, ImageRepository imageRepository) {
    this.database = database;
    this.imageRepository = imageRepository;
  }

  public static Backend getInstance() {
    if (testB != null) return testB;
    return new BackendBuilder().build();
  }
  private static Backend testB;
  public static void useThis(Backend backend) {
    testB = backend;
  }

  public Database getDatabase() {
    return database;
  }

  public ImageRepository getImageRepository() {
    return imageRepository;
  }

  public static BackendBuilder builder() {
    return new BackendBuilder();
  }

  public static class BackendBuilder {
    private Database database = null;
    private ImageRepository imageRepository = null;
    public BackendBuilder setDatabase(Database database) {
      this.database = database;
      return this;
    }
    public BackendBuilder setImageRepository(ImageRepository repo) {
      this.imageRepository = repo;
      return this;
    }
    public Backend build() {
      if (database == null) {
        if (Settings.DATABASE_CONNECTION_URL.isSet()) {
          setDatabase(new DatabaseImpl(Settings.DATABASE_CONNECTION_URL.get()));
        }
      }
      return new Backend(
              database != null ? database : new DummyDatabaseImpl(),
              imageRepository != null ? imageRepository : new ImageRepository());
    }
  }
}
