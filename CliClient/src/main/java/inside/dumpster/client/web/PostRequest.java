/*
 *
 */
package inside.dumpster.client.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import inside.dumpster.client.Payload;
import inside.dumpster.client.impl.PayloadDataGenerator;
import inside.dumpster.client.impl.PayloadHelper;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.Builder;
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

  private static final Logger logger = Logger.getLogger("WebClient");
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
  public HttpResult doRequest(Payload networkRequest) {
    try {
      byte[] bytes = new byte[networkRequest.getSrcBytes()];
      byte theByte = 'a';
      Arrays.fill(bytes, theByte);
      ObjectMapper mapper = new ObjectMapper();
      mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
      HttpClient client = HttpClient.newBuilder()
              .version(HttpClient.Version.HTTP_1_1)
              .build();
      URI uri = URI.create(baseURI + PayloadHelper.getURI(networkRequest).toASCIIString());
//      logger.warning(String.format("URI: %s",uri.toString()));
      final PayloadDataGenerator pdg = new PayloadDataGenerator();

      Builder builder = HttpRequest.newBuilder();
      InputStream is = pdg.genetarePayloadData(networkRequest);
      final String body;
      if (is != null) {
        builder.POST(HttpRequest.BodyPublishers.ofInputStream(() -> is));
        body = "with body";
      } else {
        builder.POST(HttpRequest.BodyPublishers.noBody());
        body = "w/o body";
      }
      HttpRequest request = builder
              .uri(uri)
              .header("Content-Type", "application/octet-stream")
              .header("TransactionId", networkRequest.getTransactionId())
              .build();
      client.sendAsync(request, BodyHandlers.ofString())
              .thenApply(HttpResponse::body)
              .thenAccept((String s) -> {
                logger.info(String.format("%s: %s, %s", uri, body, s));
              })
              .join();

    } catch (Exception ex) {
      Logger.getLogger(PostRequest.class.getName()).log(Level.SEVERE, null, ex);
    }

    return null;
  }
}
