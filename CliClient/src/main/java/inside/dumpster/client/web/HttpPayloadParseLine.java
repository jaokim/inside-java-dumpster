/*
 *
 */
package inside.dumpster.client.web;

import inside.dumpster.client.impl.ParseLine;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
@Deprecated
public class HttpPayloadParseLine extends ParseLine<HttpPayload> {

  @Override
  public HttpPayload createPayload() {
    return new HttpPayload();
  }

}
