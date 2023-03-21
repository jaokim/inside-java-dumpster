package inside.dumpster.monitoring.event;

import inside.dumpster.monitoring.TransactionEvent;

public class DataUpload extends TransactionEvent {
  public String datatype;

  public String srcDevice;

  public String id;

  public long size;
}
