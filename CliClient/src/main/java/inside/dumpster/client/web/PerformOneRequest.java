/*
 * 
 */
package inside.dumpster.client.web;

import inside.dumpster.client.impl.NetFlowData;
import inside.dumpster.client.impl.ParseLine;
import java.io.IOException;
import java.util.stream.Stream;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class PerformOneRequest {

  public static void main(String[] args) throws Exception {
    NetFlowData<HttpPayload> data = new NetFlowData<>(new HttpPayloadParseLine());

    PostRequest pr = new PostRequest("http://localhost:8081/");

    data.getStream().filter((t) -> {
      System.out.println("Press enter for new req.");
      try {
        System.in.read();
      } catch (IOException ex) {
      }
      return true;
    }
    ).forEach(pr::doRequest);

  }
}
