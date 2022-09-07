/*
 * 
 */
package inside.dumpster.webclient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author JSNORDST
 */
public class PostRequest {

  private static final Logger logger = Logger.getLogger(PostRequest.class.getName());
  private String baseURI = "http://localhost:8081/";

  public PostRequest(String baseURI) {
    this.baseURI = baseURI;
  }

  /**
   * Do a request.This is the source
   *
   * @param networkRequest
   * @return
   * @throws IOException
   */
  public HttpResult doRequest(HttpPayload networkRequest) {
    try {
      byte[] bytes = new byte[networkRequest.getSrcBytes()];
      byte theByte = 'a';
      Arrays.fill(bytes, theByte);
      ObjectMapper mapper = new ObjectMapper();
      
      HttpClient client = HttpClient.newBuilder()
              .version(HttpClient.Version.HTTP_1_1)
              .build();
      URI uri = URI.create(baseURI + networkRequest.getURI().toASCIIString());
      logger.warning(String.format("URI: %s",uri.toString()));
      HttpRequest request = HttpRequest.newBuilder()
              .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(networkRequest)))
              .uri(uri)
              .header("Content-Type", "application/json")
              .build();
      client.sendAsync(request, BodyHandlers.ofString())
              .thenApply(HttpResponse::body)
              .thenAccept((String s) -> {
                logger.info(String.format("inside.dumpster.webclient.PostRequest.doRequest(): %s", s));
              })
              .join();
      
    } catch (JsonProcessingException ex) {
      Logger.getLogger(PostRequest.class.getName()).log(Level.SEVERE, null, ex);
    }
    
      return null;
  }
}
