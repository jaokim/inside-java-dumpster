/*
 *
 */
package inside.dumpster.client;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class Result {
  private String result;
  private String service;
  public void setResult(String result) {
    this.result = result;
  }

  public String getResult() {
    return result;
  }

  public void setService(String service) {
    this.service = service;
  }

  public String getService() {
    return service;
  }
}
