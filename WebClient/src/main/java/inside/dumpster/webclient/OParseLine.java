/*
 * 
 */
package inside.dumpster.webclient;
import java.util.function.Function;

/**
 *
 * @author JSNORDST
 */
public class OParseLine implements Function<String, HttpPayload> {
    public HttpPayload parseLine(String line) {
        String[] request = line.split(",");
        int cnt = 0;
        HttpPayload networkRequest = new HttpPayload(
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
        return networkRequest;
    }

    @Override
    public HttpPayload apply(String line) {
        return parseLine(line);
    }


    
}
