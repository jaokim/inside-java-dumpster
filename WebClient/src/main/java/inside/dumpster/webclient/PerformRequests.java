/*
 * 
 */
package inside.dumpster.webclient;

import inside.dumpster.client.impl.NetFlowData;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author JSNORDST
 */
public class PerformRequests {

  public static void main(String[] args) throws FileNotFoundException, InterruptedException, IOException {
    new PerformRequests().main();
  }

  public void main() throws FileNotFoundException, InterruptedException, IOException {
    System.out.println("Starting");

    NetFlowData<HttpPayload> data = new NetFlowData<>(new HttpPayloadParseLine());

    Scheduler scheduler = new Scheduler("http://localhost:8888/", data.getFirsttime(), false);
    data.getStream()
            .forEach(scheduler::scheduleNetworkRequest);

  }

}
