package inside.dumpster.monitoring.event;

import inside.dumpster.monitoring.TransactionEvent;
import jdk.jfr.Category;
import jdk.jfr.DataAmount;
import jdk.jfr.Label;
import jdk.jfr.Name;

@Name("inside.dumpster.UploadData")
@Label("Data Upload")
@Category({"Business Application", "Data", "Upload"})
public class DataUpload extends TransactionEvent {
  @Name("DataType")
  @Label("Type of Data")
  public String datatype;
  
  @Name("Source")
  @Label("Source Device of Origin")
  public String srcDevice;
  
  @Name("Id")
  @Label("Id of Uploaded Data")
  public String id;

  @Name("Size")
  @Label("Size of uploaded data")
  @DataAmount
  public long size;
}
