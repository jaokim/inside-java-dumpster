/*
 *
 */
package inside.dumpster.client;

import inside.dumpster.bl.BusinessLogicException;
import inside.dumpster.bl.BusinessLogicFactory;
import inside.dumpster.bl.BusinessLogicServiceWrapper;
import inside.dumpster.client.Payload;
import inside.dumpster.client.impl.NetFlowData;
import inside.dumpster.client.impl.ParseLine;
import inside.dumpster.client.impl.PayloadDataGenerator;
import inside.dumpster.client.impl.PayloadScheduler;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import jdk.jfr.consumer.RecordingStream;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class OLDCli {

  public static void main(String[] args) throws Exception {
    new OLDCli().main();
  }

  public void main() throws FileNotFoundException, InterruptedException, IOException, ParseException {
    System.out.println("Starting");
    Enumeration<URL> en=getClass().getClassLoader().getResources("META-INF");
while (en.hasMoreElements()) {
//      try {
        URL metaInf=en.nextElement();
        System.out.println("m"+metaInf.toString());
//        File fileMetaInf=new File(metaInf.toURI());
//
//        File[] files=fileMetaInf.listFiles();
//        //or
//        String[] filenames=fileMetaInf.list();
//        System.out.println("FI: "+Arrays.toString(filenames));
//      } catch (URISyntaxExceSption ex) {
//        Logger.getLogger(Cli.class.getName()).log(Level.SEVERE, null, ex);
//      }C
}
ClassLoader cl = this.getClass().getClassLoader();
do {
  System.out.println("CL: "+cl.getName());
}while((cl = cl.getParent())!=null);
if(true)return;
//    Configuration c = Configuration.getConfiguration("profile");
    try (RecordingStream rs = new RecordingStream()) {
      rs.onEvent("jdk.OSInformation", System.out::println);
      rs.onEvent("inside.dumpster.ProcessData", System.out::println);
      rs.onEvent("inside.dumpster.UploadData", System.out::println);
      rs.onEvent("inside.dumpster.ServiceCall", System.out::println);
      System.out.println("Starting recording stream ...");
      rs.startAsync();
    }

    NetFlowData<Payload> data = new NetFlowData<>(new ParseLine<Payload>() {
      public Payload createPayload() {
        return new Payload();
      }
    });
    PayloadScheduler<Payload> scheduler = new PayloadScheduler(data.getFirsttime(), (payload) -> {
      try {
        BusinessLogicServiceWrapper wrapper = new BusinessLogicFactory().lookupService(payload.getDestination());
        PayloadDataGenerator generator = new PayloadDataGenerator();
        payload.setInputStream(generator.genetarePayloadData(payload));

        wrapper.invoke(payload);

      } catch (IOException | BusinessLogicException ex) {
        Logger.getLogger(OLDCli.class.getName()).log(Level.SEVERE, null, ex);
      }
    });
    data.getStream()
            .forEach(scheduler::scheduleRequest);

  }

}
