/*
 * 
 */
package inside.dumpster.backend.auth;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class UserInfo {
  private String name;
  private String subscriberNumber;
  private final Map<String, String> userInfo = new HashMap<>();
  

  public Map<String, String> getUserInfo() {
    return userInfo;
  }

  

  void setSubscriberNumber(String subscriberNumber) {
    this.subscriberNumber = subscriberNumber;
  }

  public String getSubscriberNumber() {
    return subscriberNumber;
  }
  void setName(String fullName) {
    this.name = fullName;
  }

  public String getName() {
    return name;
  }

}
