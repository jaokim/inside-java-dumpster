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
  @Label("Type of Data")
  public String datatype;

  @Name("source")
  @Label("Source Device of Origin")
  public String srcDevice;

  @Label("Id of Uploaded Data")
  public String id;

  @Name("size")
  @Label("Size of uploaded data")
  @DataAmount
  public long size;


  @Label("Uploaded data")
  public String data;
}
