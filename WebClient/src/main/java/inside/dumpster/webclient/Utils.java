/*
 * 
 */
package inside.dumpster.webclient;

import inside.dumpster.client.Payload;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author JSNORDST
 */
public class Utils {
    
//    public static AbstractNetworkRequest parseRequestURI(String requestURI) {
//        String paths[] = requestURI.split("/");
//        AbstractNetworkRequest req = new AbstractNetworkRequest();
//        int cnt = 2;
//        req.setDstDevice(paths[cnt++]);
//        req.setDstDeviceId(paths[cnt++]);
//        req.setProtocol(paths[cnt++]);
//        req.setDstPort(paths[cnt++]);
//        req.setSrcDevice(paths[cnt++]);
//        req.setSrcDeviceId(paths[cnt++]);
//        req.setSrcPort(paths[cnt++]);
//        req.setDstBytes(Integer.parseInt(paths[cnt++]));
//        req.setSrcBytes(Integer.parseInt(paths[cnt++]));
//        return req;
//    }
    
    /**
     * 
     * @param server address on the form "http://server/app", without ending slash.
     * @param networkRequest
     * @return
     * @throws MalformedURLException 
     */
    public static URL createURL(String server, Payload networkRequest) throws MalformedURLException {
        return new URL(String.format("%s/%s/%s/%s/%s/%s/%s/%s/%d/%d", 
                    server,
                    networkRequest.getDstDevice(), 
                    networkRequest.getDstDeviceId(), 
                    networkRequest.getProtocol(), 
                    networkRequest.getDstPort(), 
                    networkRequest.getSrcDevice(), 
                    networkRequest.getSrcDeviceId(), 
                    networkRequest.getSrcPort(),
                    networkRequest.getDstBytes(),
                    networkRequest.getSrcBytes()
                    
        ));
    }
    /**
     * 
     * @param server address on the form "http://server/app", without ending slash.
     * @param networkRequest
     * @return
     * @throws MalformedURLException 
     */
    public static URL createURLToDstPort(String server, Payload networkRequest) throws MalformedURLException {
        return new URL(String.format("%s/%s/%s/%s/%s/%s/%s/%s/%d/%d", 
                    server,
                    networkRequest.getDstPort(), 
                    networkRequest.getDstDevice(), 
                    networkRequest.getDstDeviceId(), 
                    networkRequest.getProtocol(), 
                    networkRequest.getSrcDevice(), 
                    networkRequest.getSrcDeviceId(), 
                    networkRequest.getSrcPort(),
                    networkRequest.getDstBytes(),
                    networkRequest.getSrcBytes()
                    
        ));
    }
}
