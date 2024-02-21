/*
 *
 */
package inside.dumpster.backend.database;

import inside.dumpster.backend.BackendException;
import inside.dumpster.outside.Bug;
import inside.dumpster.outside.Buggy;
import inside.dumpster.outside.Settings;
import java.io.InputStream;
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

  final static DataSource DATA_SOURCE;

  static {
    ClientConnectionPoolDataSource cpds = new ClientConnectionPoolDataSource();
    if (Settings.DATABASE_CONNECTION_URL.isSet()) {
      cpds.setDataSourceName(Settings.DATABASE_CONNECTION_URL.get());
      cpds.setDatabaseName(Settings.DATABASE_CONNECTION_URL.get().replace("jdbc:derby:", ""));
      cpds.setTraceFile("derby-pool-tracing.log");
      cpds.setTraceLevel(100);
      DATA_SOURCE = cpds;
    } else {
      DATA_SOURCE = null;
    }
  }

  public DatabaseImpl() {
  }

  public DatabaseImpl createIfNotExists() throws SQLException {
    String connectionUrl = Settings.DATABASE_CONNECTION_URL.get();
    Connection conn = null;
    try {
      conn = DriverManager.getConnection(connectionUrl);
    } catch (SQLException ex) {
      if (ex.getMessage().contains("not found")) {
        Connection conn2 = null;
        try {
          conn2 = DriverManager.getConnection(connectionUrl + ";create=true");

          //conn.createStatement().execute("CREATE TABLE textdata(destination varchar(255), dstPort varchar(25), dstDevice varchar(25), srcPort varchar(25), srcDevice varchar(25), data blob)");
          conn2.createStatement().execute("CREATE TABLE textdata(srcPort varchar(25) NOT NULL UNIQUE, data blob NOT NULL)");
          conn2.createStatement().execute("CREATE TABLE imagedata(dstPort varchar(25) NOT NULL UNIQUE, data blob NOT NULL)");
          return this;
        } finally {
          if (conn2 != null) {
            conn2.close();
          }
        }
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

  public static void main(String[] args) throws Exception {

  }

  public Connection getConnection() throws SQLException {
    if (Bug.isBuggy(this)) {
      return getRawConnection();
    } else {
      return DATA_SOURCE.getConnection();
    }
  }

  public Connection getRawConnection() throws SQLException {
    Connection conn;
    String connectionUrl = Settings.DATABASE_CONNECTION_URL.get();

//    Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
    conn = DriverManager.getConnection(connectionUrl);
    return conn;
  }

  public InputStream getImageData(String dstPort) throws BackendException {
    try (Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(
                    "SELECT data FROM imagedata WHERE dstPort=?");) {
      pstmt.setString(1, dstPort);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        return rs.getBinaryStream(1);
      }
    } catch (SQLException ex) {
      throw new BackendException(ex);
    }

    return null;
  }

  public InputStream getTextData(String srcPort) throws BackendException {
    try (Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(
                    "SELECT data FROM textdata WHERE srcPort=?");) {
      pstmt.setString(1, srcPort);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        return rs.getBinaryStream(1);
      }
    } catch (SQLException ex) {
      throw new BackendException(ex);
    }

    return null;
  }

  public void insertImageData(String dstPort, InputStream iStream) throws BackendException {
    insertImageData(dstPort, iStream, true);
  }

  public void insertImageData(String dstPort, InputStream iStream, boolean overwrite) throws BackendException {
    try (
            final Connection conn = getConnection();
            final PreparedStatement imageFind = conn.prepareStatement(
                    "SELECT COUNT(dstPort) FROM imagedata WHERE dstPort=?");
            final PreparedStatement imageInsert = conn.prepareStatement(
                    "INSERT INTO imagedata(dstPort, data) VALUES(?,?)");) {
      if (overwrite) {
        PreparedStatement pstm = conn.prepareStatement("DELETE FROM imagedata WHERE dstPort=?");
        pstm.setString(1, dstPort);
        pstm.execute();
      } else {
        imageFind.setString(1, dstPort);
        ResultSet rs = imageFind.executeQuery();
        if (rs.next()) {
          if (rs.getInt(1) > 0) {
            return;
          }
        }
      }
      int cnt = 1;
      imageInsert.setString(cnt++, dstPort);
      imageInsert.setBinaryStream(cnt++, iStream);
      imageInsert.execute();
    } catch (SQLException ex) {
      throw new BackendException(ex);
    }

  }

  public void insertTextData(String srcPort, InputStream iStream) throws BackendException {
    insertTextData(srcPort, iStream, true);
  }

  public void insertTextData(String srcPort, InputStream iStream, boolean overwrite) throws BackendException {
    try (
            final Connection conn = getConnection();
            final PreparedStatement imageFind = conn.prepareStatement(
                    "SELECT COUNT(dstPort) FROM imagedata WHERE dstPort=?");
            final PreparedStatement textInsert = conn.prepareStatement(
                    "INSERT INTO textdata(srcPort, data) VALUES(?,?)");) {
      if (overwrite) {
        PreparedStatement pstm = conn.prepareStatement("DELETE FROM textdata WHERE srcPort=?");
        pstm.setString(1, srcPort);
        pstm.execute();
      } else {
        imageFind.setString(1, srcPort);
        ResultSet rs = imageFind.executeQuery();
        if (rs.next()) {
          if (rs.getInt(1) > 0) {
            return;
          }
        }
      }
      int cnt = 1;
      textInsert.setString(cnt++, srcPort);
      textInsert.setBinaryStream(cnt++, iStream);
      textInsert.execute();
    } catch (SQLException ex) {
      throw new BackendException(ex);
    }

  }

}
