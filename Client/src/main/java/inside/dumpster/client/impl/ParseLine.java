/*
 * 
 */
package inside.dumpster.client.impl;
import inside.dumpster.client.Payload;
import java.util.function.Function;

/**
 *
 * @author JSNORDST
 * @param <P>
 */
public abstract class ParseLine<P extends Payload> implements Function<String, P> {

  
  public abstract P createPayload();
  
  
    public P parseLine(String line) {
        String[] request = line.split(",");
        int cnt = 0;
        P networkRequest = createPayload();
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
  //        request[cnt++],
          request[cnt++]
        );
        return (P)networkRequest;
    }

    @Override
    public P apply(String line) {
        return parseLine(line);
    }


    
}
