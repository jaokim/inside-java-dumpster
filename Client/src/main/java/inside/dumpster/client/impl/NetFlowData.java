/*
 * 
 */
package inside.dumpster.client.impl;

import inside.dumpster.client.Payload;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * 
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 * @param <P>
 */
public class NetFlowData<P extends Payload> {
  private final long firsttime;
  private final Function<String, P> parse;
  
  public NetFlowData(ParseLine<P> parseLine) throws IOException {
    try (BufferedReader reader = openNetFlowLogFile()) {
      parse = parseLine;
      String line = reader.readLine();
      // get firsttime in log to get the offset
      firsttime = parse.apply(line).getTime();
    }
  }

  private static BufferedReader openNetFlowLogFile() throws FileNotFoundException {
    return new BufferedReader(new FileReader("D:\\jsnordst\\projekt\\InsideDumpster\\netflow_day-02"));
  }

  public long getFirsttime() {
    return firsttime;
  }
  
  public Stream<P> getStream() throws IOException {
    BufferedReader reader = openNetFlowLogFile();
    return reader.lines().map(parse);
  }
}
