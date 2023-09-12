/*
 * 
 */
package inside.dumpster.backend.databas;

import jdk.jfr.Category;
import jdk.jfr.Event;
import jdk.jfr.Label;
import jdk.jfr.Name;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
@Name("inside.dumpster.backend.Database")
@Label("Database Query")
@Category({"Backend", "Database", "Query"})
public class DatabaseQueryEvent extends Event {
  @Name("Query")
  @Label("Query to execute")
  public String query;
}
