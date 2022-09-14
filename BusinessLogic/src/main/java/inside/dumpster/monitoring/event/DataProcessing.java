/*
 * 
 */
package inside.dumpster.monitoring.event;

import inside.dumpster.monitoring.TransactionEvent;
import jdk.jfr.Category;
import jdk.jfr.Description;
import jdk.jfr.Label;
import jdk.jfr.Name;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
@Name("inside.dumpster.ProcessData")
@Label("Data Processing")
@Category({"Business Application", "Data", "Processing"})
@Description("Statistics over data processing events")
public class DataProcessing extends TransactionEvent {
  @Name("DataType")
  @Label("Type of Data")
  @Description("What kind of data is processed. For instance if it's an Image, Video or Text")
  public String datatype;
  
  @Name("ProcessType")
  @Label("Type of Processing")
  @Description("What kind of processing is performed on the data.")
  public String processType;
}