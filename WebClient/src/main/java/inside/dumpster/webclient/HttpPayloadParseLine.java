/*
 * 
 */
package inside.dumpster.webclient;

import inside.dumpster.client.impl.ParseLine;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class HttpPayloadParseLine extends ParseLine<HttpPayload> {

  @Override
  public HttpPayload createPayload() {
    return new HttpPayload();
  }
 
}
