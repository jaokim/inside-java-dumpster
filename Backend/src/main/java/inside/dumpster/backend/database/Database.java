/*
 *
 */
package inside.dumpster.backend.database;

import inside.dumpster.backend.BackendException;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public interface Database {
  public InputStream getPayloadData(DatabaseImpl.DataType dataType, String payload) throws BackendException;

  public InputStream getImageData(String dstPort) throws BackendException;

  public InputStream getTextData(String srcPort) throws BackendException;

  public void insertImageData(String dstPort, InputStream iStream) throws BackendException;

  public void insertImageData(String dstPort, InputStream iStream, boolean overwrite) throws BackendException;

  public void insertTextData(String srcPort, InputStream iStream) throws BackendException;

  public void insertTextData(String srcPort, InputStream iStream, boolean overwrite) throws BackendException;

}
