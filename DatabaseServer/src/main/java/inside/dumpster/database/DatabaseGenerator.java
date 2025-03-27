/*
 *
 */
package inside.dumpster.database;

import com.github.jaokim.arguably.Arguments;
import inside.dumpster.backend.database.DatabaseImpl;
import inside.dumpster.client.Payload;
import inside.dumpster.client.impl.NetFlowData;
import inside.dumpster.client.impl.ParseLine;
import inside.dumpster.client.impl.PayloadDataGenerator;
import inside.dumpster.outside.Bug;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class DatabaseGenerator extends Arguments<DatabaseGenerator> {
   public final Arg ConnectionString = new Arguments.Arg("-connectionstring", "connection", String.class, Arguments.Arg.Is.Optional, "JDBC connection string, f.i. jdbc:derby://localhost:1527/dumpster", "jdbc:derby:dumpster", Arg.Askable.Yes);
//    public final Arg Duration = new Arguments.Arg("-duration", "duration", Integer.class, Arguments.Arg.Is.Optional, "Set how long the client will run. In seconds.", null, Arg.Askable.Yes);
//    public final Arg Limit = new Arguments.Arg("-limit", "limit", Integer.class, Arguments.Arg.Is.Optional, "Sets a limit to number of requests made.", null, Arg.Askable.Yes);
//    public final Arg Filter = new Arguments.Arg("-filter", "filter", String.class, Arguments.Arg.Is.Optional, "Filter destinations.", null, Arg.Askable.Yes);
//    public final Arg Interactive = new Arguments.Arg("-i", "interactive", Boolean.class, Arguments.Arg.Is.Optional, "Decide interactively when each reqauest will be sent.", "false");
//    public final Arg DelayThreshold = new Arguments.Arg("-delay", "delay", Integer.class, Arguments.Arg.Is.Optional, "Delay threshold for how often requests are sent. The original delay is divided by this value.", "100");
//    public final static CliArguments Instance;
//    static {
//      CliArguments inst = null;
//      try {
//        inst = new CliArguments(new String[0]);
//      } catch (Exception ex) {
//      } finally {
//        Instance = inst;
//      }
//    }
//    public CliArguments (String [] args) throws Exception {
//        super.parseArgs(args);
//    }

  public Connection getConnection(String dbLocation) throws SQLException {
    Connection conn;
    String connectionUrl = "jdbc:derby:" + dbLocation;
    try {
      System.out.println("Gettting connection for :"+connectionUrl);
//    Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
      conn = DriverManager.getConnection(connectionUrl);
    } catch (SQLException ex) {
      if (ex.getMessage().contains("not found")) {
        System.out.println("onnection for :"+connectionUrl+ " not found, adding ;create=true \"" +connectionUrl + ";create=true"+ "\"");
        conn = DriverManager.getConnection(connectionUrl + ";create=true");

        //conn.createStatement().execute("CREATE TABLE textdata(destination varchar(255), dstPort varchar(25), dstDevice varchar(25), srcPort varchar(25), srcDevice varchar(25), data blob)");
        conn.createStatement().execute("CREATE TABLE textdata(srcPort varchar(25) NOT NULL UNIQUE, data blob NOT NULL)");
        conn.createStatement().execute("CREATE TABLE imagedata(dstPort varchar(25) NOT NULL UNIQUE, data blob NOT NULL)");
        return conn;
      } else {
        throw ex;
      }
    }
    return conn;
  }

  public static void main(String[] args) throws Exception {
      System.out.println("Args: "+args);
    new DatabaseGenerator().generateDatabase(args);
    //new DatabaseGenerator().generateDatabaseOld(args);
  }
  public void generateDatabase(String[] args) throws Exception {
    new DatabaseGenerator().parseArgs(args);
    final PayloadDataGenerator generator = new PayloadDataGenerator();
    Bug.getMXBean().setBuggy(DatabaseImpl.class.getName(), Boolean.FALSE);
    String connectionString = ConnectionString.getValue();//args.length > 0 ? args[0] : "jdbc:derby://localhost:1527/dumpster";
    System.out.println("Using connection string: "+connectionString);
    DatabaseImpl database = new DatabaseImpl(connectionString, true);
    database.create();
    Random rand = new Random();
    for (int i=0; i < DatabaseImpl.MAX_DATA_ID ; i++) {
      InputStream iStream;
      int x = rand.nextInt(500, 700);
      int y = rand.nextInt(300, 600);
      iStream = generator.generateImage(x, y);
      database.insertData(DatabaseImpl.DataType.Image.name(), i, iStream);

      int sentences = rand.nextInt(30, 1000);
      int seed = rand.nextInt();
      iStream = generator.generateText(seed, sentences);
      database.insertData(DatabaseImpl.DataType.Text.name(), i, iStream);

      iStream = generator.generateImage(800, 800);
      database.insertData(DatabaseImpl.DataType.UserImage.name(), i, iStream);

    }
  }

  public void generateDatabaseOld(String[] args) throws Exception {
//    Recording rec = new Recording(Configuration.getConfiguration("default"));
//    rec.setDestination(Path.of("E:/derby-run.jfr"));
//    rec.setToDisk(true);
//    rec.setDumpOnExit(true);
//    rec.setDuration(Duration.ofMinutes(5));
//    rec.start();
    final PayloadDataGenerator generator = new PayloadDataGenerator();
    NetFlowData<Payload> data = new NetFlowData<>(new ParseLine<Payload>() {
      @Override
      public Payload createPayload() {
        return new Payload();
      }
    });

    try (
            final Connection conn = getConnection(args.length > 0 ? args[0] : "dumpster");
            final PreparedStatement textFind = conn.prepareStatement(
                    "SELECT COUNT(srcPort) FROM textdata WHERE srcPort=?");
            final PreparedStatement imageFind = conn.prepareStatement(
                    "SELECT COUNT(dstPort) FROM imagedata WHERE dstPort=?");
            final PreparedStatement textInsert = conn.prepareStatement(
                    "INSERT INTO textdata(srcPort, data) VALUES(?,?)");
            final PreparedStatement imageInsert = conn.prepareStatement(
                    "INSERT INTO imagedata(dstPort, data) VALUES(?,?)");) {
      conn.createStatement().execute("DELETE FROM textdata");
      conn.createStatement().execute("DELETE FROM imagedata");
      final AtomicInteger img = new AtomicInteger(), txt = new AtomicInteger();
      data.getStream().forEach(payload -> {
        try {
          InputStream iStream;
          switch (generator.getPayloadDataType(payload.getDestination())) {
            case Image:
              imageFind.setString(1, payload.getDstPort());
              ResultSet rs = imageFind.executeQuery();
              if (rs.next()) {
                if (rs.getInt(1) > 0) {
                  return;
                }
              }
              iStream = generator.generatePayloadData(payload, PayloadDataGenerator.Type.Image);
              int cnt = 1;
              imageInsert.setString(cnt++, payload.getDstPort());
              imageInsert.setBinaryStream(cnt++, iStream);
              imageInsert.execute();
//              imageInsert.addBatch();
              if (img.incrementAndGet() == 10) {
                System.out.println("Inserted 10 images");
//                imageInsert.executeBatch();
                img.set(0);
              }
              break;
            case Text:
              cnt = 1;
              textFind.setString(1, payload.getSrcPort());
              rs = textFind.executeQuery();
              if (rs.next()) {
                if (rs.getInt(1) > 0) {
                  return;
                }
              }

              iStream = generator.generatePayloadData(payload, PayloadDataGenerator.Type.Text);
              textInsert.setString(cnt++, payload.getSrcPort());
//              if (iStream == null) {
//                textInsert.setNull(cnt++, BLOB);
//              } else {
                textInsert.setBinaryStream(cnt++, iStream);
//              }
              textInsert.execute();
              if (txt.incrementAndGet() == 100) {
                System.out.println("Insering 100 texts");
//                textInsert.executeBatch();
                txt.set(0);
              }
          }
        } catch (Exception e) {
          System.out.println("Exception for payload: " + payload.toString());
          e.printStackTrace();
        }
      });

    }
  }
}
