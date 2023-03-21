/*
 *
 */
package inside.dumpster.backend.repository;

import com.github.javafaker.Dune;
import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import inside.dumpster.backend.repository.data.Text;
import inside.dumpster.monitoring.Monitoring;
import inside.dumpster.monitoring.MonitoringEvent;
import inside.dumpster.monitoring.MonitoringFactory;
import java.io.IOException;
import java.util.Locale;
import java.util.Random;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class TextStorage {
  private static final Monitoring monitoring = MonitoringFactory.getMonitoring();
  public void storeText(final Text text) throws IOException {
    TextRepository repo = new TextRepository();
    MonitoringEvent measure = monitoring.createEvent();
    measure.set("name", "measure");
    MonitoringEvent event = monitoring.createEvent();
    event.set("name", "storeText");
    StringBuilder builder = text.getBuilder();
    String outputText = builder.toString();

    //repo.storeData(text);


    event.set("data", outputText);
//    System.out.println("Commiting: "+outputText.length()+ " fds: ");
//    System.out.println(builder);
//    System.out.println(builder.toString());
    try {
      measure.doBegin();
      event.doCommit();
      measure.doEnd();
    } catch(InternalError e) { e.printStackTrace();}
    measure.doCommit();
  }
  public static void main(String[] args) throws IOException {
    System.out.println("v2");
    TextStorage storage = new TextStorage();
    if (args.length <= 0) {
      for (int i = 0; i < 3000; i++) {
        int dune = 0;
        doStore(dune, storage, 1);
        doStore(dune++, storage, 4);
        doStore(dune++, storage, 3);
        doStore(dune++, storage, 5);
        doStore(dune++, storage, 80);
        doStore(dune++, storage, 5);
        doStore(dune++, storage, 15);
        doStore(dune++, storage, 3);
        doStore(dune++, storage, 1);
        doStore(dune++, storage, 5);
        doStore(dune++, storage, 10);
        doStore(dune++, storage, 1);
        doStore(dune++, storage, 4);
        doStore(dune++, storage, 3);
        doStore(dune++, storage, 5);
        doStore(dune++, storage, 120);
        doStore(dune++, storage, 5);
        doStore(dune++, storage, 1);
        doStore(dune++, storage, 1);
        doStore(dune++, storage, 5);
        doStore(dune++, storage, 4);
        doStore(dune++, storage, 5);
        doStore(dune++, storage, 200);
        doStore(dune++, storage, 200);
        doStore(dune++, storage, 1);
        doStore(dune++, storage, 5);
      }
    } else if ("short".equals(args[0]) || "long".equals(args[0]) || "custom".equals(args[0])) {
      int stringLen;
      int loops;
      if ("short".equals(args[0])) {
        stringLen = 120;
        loops = 20000;
      } else if ("long".equals(args[0])) {
        // these should not be pooled
        stringLen = 240;
        loops = 10000;
      } else {
        stringLen = Integer.parseInt(args[1]);
        loops = Integer.parseInt(args[2]);
      }


      for (int i = 0; i < loops; i++) {
//        Random r = new Random();
//        Dune dune = Faker.instance(r).dune();
        StringBuilder builder = new StringBuilder();
        builder.append("Alanis_");
        while(builder.length() < stringLen) {
          builder.append("TheseAreTheReasonsIDrink");
        }
        StringBuilder rest = new StringBuilder();
        String part = builder.toString().substring(0, stringLen);
        rest.append(part);
        Text text = new Text(rest);
        storage.storeText(text);
      }
    } else if ("jfr".equals(args[0])) {
//      File f = new File(args[1]) {
//
//      }
    }
  }

  static void doStore(int rand, TextStorage storage, int loops) throws IOException {
    Random r = new Random(rand);
    Dune dune = Faker.instance(r).dune();

    StringBuilder builder;
    Text text;
    builder = new StringBuilder();
    builder.append("EL_10");
    for (int j = 0; j < loops; j++) {
      builder.append(dune.quote());
      builder.append("\n");
    }
    text = new Text(builder);
    storage.storeText(text);
  }
}
