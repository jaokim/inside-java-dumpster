/*
 * 
 */
package inside.dumpster.bl.auth;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class User {
  private UUID id;
  private Object session;
  private Principal principal;
  private Object params;
  void setId(UUID id) {
    this.id = id;
  }

  public UUID getId() {
    return id;
  }

  void setSession(Object session) {
    this.session = session;
  }

  void setParams(Map<String, String[]> params) {
    this.params = params;
  }

  void setPrincipal(Principal principal) {
    this.principal = principal;
  }

}
