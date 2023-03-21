/*
 *
 */
package inside.dumpster.client.impl;

import inside.dumpster.client.Payload;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author JSNORDST
 * @param <P>
 */
public abstract class ParseLine<P extends Payload> implements Function<String, P> {
P d;

  private static final String HOST;
  static {
    String host = "unknown";
    try {
      host = InetAddress.getLocalHost().getHostName();
    } catch (UnknownHostException ex) {
    } finally {
      HOST = host;
    }
  }
  public abstract P createPayload();

  /**
   * Parse a netflow log line and create a Payload.
   * @param line
   * @return
   */
  public P parseLine(String line) {
    String[] request = line.split(",");
    int cnt = 0;
    P networkRequest = createPayload();
    networkRequest.setTransactionId(String.format("%s-%s", HOST, line.hashCode()));
    networkRequest.setPayloadValues(
            request[cnt++],
            request[cnt++],
            request[cnt++],
            request[cnt++],
            request[cnt++],
            request[cnt++],
            request[cnt++],
            request[cnt++],
            request[cnt++],
            request[cnt++],
            request[cnt++]
    );
    return (P) networkRequest;
  }

  @Override
  public P apply(String line) {
    return parseLine(line);
  }

}
