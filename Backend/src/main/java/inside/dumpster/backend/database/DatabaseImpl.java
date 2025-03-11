/*
 *
 */
package inside.dumpster.backend.database;

import inside.dumpster.backend.BackendException;
import inside.dumpster.backend.utils.Utils;
import inside.dumpster.outside.Bug;
import inside.dumpster.outside.Buggy;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.derby.jdbc.ClientConnectionPoolDataSource;
//import org.apache.derby.iapi.jdbc.EmbeddedDriver;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
@Buggy(because = "it doesn't use a connection pool")
public class DatabaseImpl implements Database {
  private final String connectionUrl;
  private final DataSource dataSource;
  /**
   * All data blobs reside in "buckets".
   */
  public static final int MAX_DATA_ID = 1000;
  public enum DataType {
    Image, Text, UserImage
  }

  public DatabaseImpl(String connectionUrl) {
    this.connectionUrl = connectionUrl;
    ClientConnectionPoolDataSource cpds = new ClientConnectionPoolDataSource();
    cpds.setDataSourceName(connectionUrl);
    cpds.setDatabaseName("dumpster");//connectionUrl.replace("jdbc:derby:", ""));
    cpds.setTraceFile("derby-pool-tracing.log");
    cpds.setTraceLevel(100);
    dataSource = cpds;

  }

//  private enum Type {
//    Long,
//    String,
//    Blob,
//    PrimaryKey,
//    ForeignKey;
//  }
//  private enum Column {
//    Id(Type.PrimaryKey),
//    Value(Type.String),
//    Data(Type.Blob);
//    private final Type type;
//    public final Table forTable;
//    private Column(Type type) {
//      this.type = type;
//      this.forTable = null;
//    }
//    private Column(Type type, Table forTable) {
//      this.type = type;
//      this.forTable = forTable;
//    }
//
//  }
//  private enum Table {
//    dstportdata(Column.Id, Column.Value),
//    srcportdata(Column.Id, Column.Value),
//    textdata(Column.Id, Column.Data),
//    imagedata(Column.Id, Column.Id);
//    private final Column cols[];
//    Table(Column ... cols) {
//      this.cols = cols;
//    };
//    public String fkey() {
//      return toString()+"_"+pkey();
//    }
//    public String pkey() {
//      return "id";
//    }
//    public String create() {
//      return "CREATE TABLE "+Table.textdata+"(PRIMARY KEY "+pkey()+", data blob NOT NULL)";
//    }
//  }

  public DatabaseImpl createIfNotExists() throws SQLException {
    Connection conn = null;
    try {
      conn = DriverManager.getConnection(connectionUrl);
    } catch (SQLException ex) {
      if (ex.getMessage().contains("not found")) {
        return create();
      } else {
        throw ex;
      }
    } finally {
      if (conn != null) {
        conn.close();
      }
    }
    return this;
  }

  public DatabaseImpl create() throws SQLException {
    try (Connection conn2 = DriverManager.getConnection(connectionUrl + ";create=true")) {
      try {
        conn2.createStatement().execute("DROP TABLE db_payload");
        System.out.println("Dropped table db_payload");
      } catch(Throwable s) {
        System.out.println("Error Dropping db_payload: "+s.getMessage());
      }
      try {
        conn2.createStatement().execute("DROP TABLE db_data");
        System.out.println("Dropped table db_data");
      } catch(Throwable s) {
        System.out.println("Error Dropping db_data: "+s.getMessage());
      }
      try {
//      conn2.createStatement().execute("CREATE TABLE db_data( data_id INT GENERATED ALWAYS AS IDENTITY, data blob, PRIMARY KEY (data_id) )");
      conn2.createStatement().execute("CREATE TABLE db_data( data_id INT GENERATED ALWAYS AS IDENTITY, payload_id INT NOT NULL, data_type varchar(25) NOT NULL, data blob)");
      conn2.createStatement().execute("ALTER TABLE db_data ADD CONSTRAINT data_constraint UNIQUE(payload_id, data_type)");
        System.out.println("Created db_data");
      } catch(Throwable s) {
        System.out.println("Error creating db_data: "+s.getMessage());
      }
//      try {
//      conn2.createStatement().execute("CREATE TABLE db_payload(plkey varchar(25), value varchar(255), data_id INT, FOREIGN KEY (data_id) REFERENCES db_data(data_id) )");
//  System.out.println("Created table db_payload");
//      } catch(Throwable s) {
//        System.out.println("Error creating db_payload: "+s.getMessage());
//      }

//
//                    + "PRIMARY KEY id, "
//                    + "dstPort varchar(25), "
//                    + "destination varchar(25), "
//                    + "dstPort varchar(25), "
//                    + "dstDevice varchar(25), "
//                    + "srcPort varchar(25), "
//                    + "srcDevice varchar(25), "

//                    "INSERT INTO textdata(destination, dstPort, dstDevice, srcPort, srcDevice, data) VALUES(?,?,?,?,?,?)");
//          conn2.createStatement().execute("CREATE TABLE srcPortData(srcPort varchar(25), FOREIGN KEY ("+Table.imagedata.fkey()+") REFERENCES "+Table.imagedata+"(id))");
//          conn2.createStatement().execute("CREATE TABLE "+Table.textdata+"(PRIMARY KEY id, data blob NOT NULL)");

return this;
    }
  }

  public static void main(String[] args) throws Exception {
DatabaseImpl database = new DatabaseImpl(args.length > 0 ? args[0] : "jdbc:derby://localhost:1527/dumpster");
    database.create();

  }

  public Connection getConnection() throws SQLException {
    if (Bug.isBuggy(this)) {
      return getRawConnection();
    } else {
      return dataSource.getConnection();
    }
  }

  public Connection getRawConnection() throws SQLException {
    Connection conn;
    conn = DriverManager.getConnection(connectionUrl);
    return conn;
  }

  @Override
  public InputStream getImageData(String payload) throws BackendException {
    return getPayloadData(DataType.Image, payload);
  }

  @Override
  public InputStream getTextData(String payload) throws BackendException {
    return getPayloadData(DataType.Text, payload);
  }


//  public InputStream getPayloadData(String dataType, String value) throws BackendException {
//    ByteArrayInputStream bais = new ByteArrayInputStream(getPayloadDataBytes(dataType, value));
//    return bais;
//  }

//  public byte[] getPayloadData(DataType dataType, String payload) throws BackendException {
//    return getPayloadDataBytes(dataType.name(), payload).toByteArray();
//  }
  public InputStream getPayloadData(DataType dataType, String payload) throws BackendException {
    return getPayloadDataBytes(dataType.name(), payload);
  }
  public InputStream getPayloadDataBytes(String dataType, String value) throws BackendException {
    try (Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(
                    "SELECT data FROM db_data WHERE payload_id=? AND data_type=?");) {
      pstmt.setInt(1, calculateDataBucket(value));
      pstmt.setString(2, dataType);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        ByteArrayOutputStream bos = Utils.inputStreamToOutputStream(rs.getBinaryStream(1));
        ByteArrayInputStream bbis = new ByteArrayInputStream(bos.toByteArray());
        return bbis;
      }
    } catch (SQLException ex) {
      throw new BackendException(ex);
    }

    return null;
  }

  @Override
  public void insertImageData(String dstPort, InputStream iStream) throws BackendException {
    insertData(DataType.Image.name(), calculateDataBucket(dstPort), iStream);
  }

  @Override
  public void insertTextData(String srcPort, InputStream iStream) throws BackendException {
    insertData(DataType.Text.name(), calculateDataBucket(srcPort), iStream);
  }

  public long insertData(String datatype, int value, InputStream iStream) throws BackendException {
    try (
            final Connection conn = getConnection();
            final PreparedStatement textInsert = conn.prepareStatement(
                    "INSERT INTO db_data(payload_id, data_type, data) VALUES(?,?,?)", new String[] { "data_id"});) {
      int cnt = 1;
      textInsert.setInt(cnt++, value);
      textInsert.setString(cnt++, datatype);
      textInsert.setBinaryStream(cnt++, iStream);
      textInsert.execute();
      ResultSet rs = textInsert.getGeneratedKeys();
      if (rs.next()) {
        return rs.getLong(1);
      } else {
        throw new BackendException("No id");
      }
    } catch (SQLException ex) {
      throw new BackendException(ex);
    }
  }

  @Override
  public void insertImageData(String dstPort, InputStream iStream, boolean overwrite) throws BackendException {
    if (getImageData(dstPort) == null) {
      insertImageData(dstPort, iStream);
    }
  }

  @Override
  public void insertTextData(String srcPort, InputStream iStream, boolean overwrite) throws BackendException {
    if (getTextData(srcPort) == null) {
      insertTextData(srcPort, iStream);
    }
  }

  private int calculateDataBucket(String identifier) {
    return Math.abs(identifier.hashCode() % MAX_DATA_ID);
  }



}
