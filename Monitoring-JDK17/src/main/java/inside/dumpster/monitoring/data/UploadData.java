/*
 * 
 */
package inside.dumpster.monitoring.data;

import jdk.jfr.Category;
import jdk.jfr.Event;
import jdk.jfr.Label;
import jdk.jfr.Name;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
@Name("inside.dumpster.UploadData")
@Label("Data Upload")
@Category({"Business Application", "Data", "Upload"})
public class UploadData extends Event {
  @Name("DataType")
  @Label("Type of Data")
  private String datatype;
  
  @Name("Source")
  @Label("Source Device of Origin")
  private String srcDevice;
  
  @Name("Id")
  @Label("Id of Uploaded Data")
  private String id;
  
  public void setSrcDevice(String srcDevice) {
    this.srcDevice = srcDevice;
  }

  public void setDatatype(String datatype) {
    this.datatype = datatype;
  }


  public void setId(String id) {
    this.id = id;
  }

}
