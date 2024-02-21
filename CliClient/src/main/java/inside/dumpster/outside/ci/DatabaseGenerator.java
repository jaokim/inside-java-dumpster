/*
 *
 */
package inside.dumpster.outside.ci;

import inside.dumpster.backend.database.DatabaseImpl;
import inside.dumpster.client.Payload;
import inside.dumpster.client.impl.NetFlowData;
import inside.dumpster.client.impl.ParseLine;
import inside.dumpster.client.impl.PayloadDataGenerator;
import java.io.InputStream;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import static java.sql.Types.BLOB;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;
import jdk.jfr.Configuration;
import jdk.jfr.Recording;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class DatabaseGenerator {
  public static void main(String[] args) throws Exception {
    Recording rec = new Recording(Configuration.getConfiguration("default"));
    rec.setDestination(Path.of("E:/derby-run.jfr"));
    rec.setToDisk(true);
    rec.setDumpOnExit(true);
    rec.setDuration(Duration.ofMinutes(5));
    rec.start();
    final PayloadDataGenerator generator = new PayloadDataGenerator();
    NetFlowData<Payload> data = new NetFlowData<>(new ParseLine<Payload>() {
        @Override
        public Payload createPayload() {
          return new Payload();
        }
      });

    try (
            final Connection conn = new DatabaseImpl().getConnection();
            final PreparedStatement textInsert = conn.prepareStatement(
                    "INSERT INTO textdata(destination, dstPort, dstDevice, srcPort, srcDevice, data) VALUES(?,?,?,?,?,?)");
            final PreparedStatement imageInsert = conn.prepareStatement(
                    "INSERT INTO imagedata(destination, dstPort, dstDevice, srcPort, srcDevice, data) VALUES(?,?,?,?,?,?)");
            ) {
      conn.createStatement().execute("DELETE FROM textdata");
      conn.createStatement().execute("DELETE FROM imagedata");
      final AtomicInteger img = new AtomicInteger(), txt = new AtomicInteger();
      data.getStream().forEach(payload -> {
        try {
          InputStream iStream;
          switch(generator.getPayloadDataType(payload.getDestination())) {
            case Image:
              iStream = generator.generatePayloadData(payload, PayloadDataGenerator.Type.Image);
              int cnt = 1;
              imageInsert.setString(cnt++, payload.getDestination().toString());
              imageInsert.setString(cnt++, payload.getDstPort());
              imageInsert.setString(cnt++, payload.getDstDevice());
              imageInsert.setString(cnt++, payload.getSrcPort());
              imageInsert.setString(cnt++, payload.getSrcDevice());
              imageInsert.setBinaryStream(cnt++, iStream);
              imageInsert.addBatch();
              if (img.incrementAndGet() == 10) {
                System.out.println("Insering 10 images");
                imageInsert.executeBatch();
                img.set(0);
              }
            case Text:
              cnt = 1;
              iStream = generator.generatePayloadData(payload, PayloadDataGenerator.Type.Text);
              textInsert.setString(cnt++, payload.getDestination().toString());
              textInsert.setString(cnt++, payload.getDstPort());
              textInsert.setString(cnt++, payload.getDstDevice());
              textInsert.setString(cnt++, payload.getSrcPort());
              textInsert.setString(cnt++, payload.getSrcDevice());
              if (iStream == null) {
                textInsert.setNull(cnt++, BLOB);
              } else {
                textInsert.setBinaryStream(cnt++, iStream);
              }
              textInsert.addBatch();
              if (txt.incrementAndGet() == 100) {
                System.out.println("Insering 100 texts");
                textInsert.executeBatch();
                txt.set(0);
              }
          }
        } catch(Exception e) {
          System.out.println("Exception for payload: "+payload.toString());
          e.printStackTrace();
        }
      });

    }
  }
}
