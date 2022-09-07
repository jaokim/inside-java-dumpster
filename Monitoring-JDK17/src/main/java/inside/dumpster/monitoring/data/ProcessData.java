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
@Name("inside.dumpster.ProcessData")
@Label("Data Processing")
@Category({"Business Application", "Data", "Processing"})
public class ProcessData extends Event {
  
  @Name("DataType")
  @Label("Type of Data")
  private String datatype;
  
  @Name("ProcessType")
  @Label("Type of Processing")
  private String processType;
  

  public void setProcessType(String processType) {
    this.processType = processType;
  }

  public void setDatatype(String datatype) {
    this.datatype = datatype;
  }
}