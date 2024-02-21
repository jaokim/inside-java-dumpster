/*
 *
 */
package inside.dumpster.backend.database;

import inside.dumpster.backend.BackendException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class DummyDatabaseImpl implements Database {
  private static final String b64img = "/9j/4AAQSkZJRgABAQEASABIAAD/2wBDAP//////////////////////////////////////////////////////////////////////////////////////wgALCAABAAEBAREA/8QAFBABAAAAAAAAAAAAAAAAAAAAAP/aAAgBAQABPxA=";
  @Override
  public InputStream getImageData(String dstPort) throws BackendException {
    return new ByteArrayInputStream(Base64.getDecoder().decode(b64img));
  }

  @Override
  public InputStream getTextData(String srcPort) throws BackendException {
    return new ByteArrayInputStream("Helo".getBytes());
  }

  @Override
  public void insertImageData(String dstPort, InputStream iStream) throws BackendException {

  }

  @Override
  public void insertImageData(String dstPort, InputStream iStream, boolean overwrite) throws BackendException {

  }

  @Override
  public void insertTextData(String srcPort, InputStream iStream) throws BackendException {

  }

  @Override
  public void insertTextData(String srcPort, InputStream iStream, boolean overwrite) throws BackendException {
    
  }

}
