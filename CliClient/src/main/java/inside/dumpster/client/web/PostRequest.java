/*
 *
 */
package inside.dumpster.client.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import inside.dumpster.client.Payload;
import inside.dumpster.client.event.RequestEvent;
import inside.dumpster.client.impl.PayloadDataGenerator;
import inside.dumpster.client.impl.PayloadHelper;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
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
    final RequestEvent reqEvent = new RequestEvent();
    try {
      byte[] bytes = new byte[networkRequest.getSrcBytes()];
      byte theByte = 'a';
      Arrays.fill(bytes, theByte);
      ObjectMapper mapper = new ObjectMapper();
      mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
      HttpClient.Builder httpClientBuilder = HttpClient.newBuilder();
      String proxy;
      if (((proxy = System.getenv("http_proxy")) != null) && (System.getProperty("no_proxy") != null)) {
        URI proxyUri = URI.create(proxy);
        httpClientBuilder = httpClientBuilder.proxy(ProxySelector.of(new InetSocketAddress(proxyUri.getHost(), proxyUri.getPort())));
      }
      HttpClient client = httpClientBuilder
              .version(HttpClient.Version.HTTP_1_1)
              .connectTimeout(Duration.ofSeconds(10))
              .build();
      URI uri = URI.create(baseURI + PayloadHelper.getURI(networkRequest).toASCIIString());

      reqEvent.uri = uri.toString();
      reqEvent.type = RequestEvent.Type.Network.name();
      reqEvent.destination = networkRequest.getDestination().toString();
      final PayloadDataGenerator pdg = new PayloadDataGenerator();

      reqEvent.begin();

      Builder builder = HttpRequest.newBuilder();
      InputStream is = InputStream.nullInputStream();//pdg.genetarePayloadData(networkRequest);
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
              .thenAccept((HttpResponse<String> response) -> {
                reqEvent.result = response.body();
                reqEvent.status = response.statusCode();
                logger.info(String.format("%s: %d", uri, reqEvent.status));
                if (reqEvent.status != 200) {
                  logger.info(String.format("%s: %s", uri, reqEvent.result));
                }
                reqEvent.end();
                reqEvent.commit();

              } )
//              .thenApply(HttpResponse::body)
//              .thenAccept((String s) -> {
//                reqEvent.result = s;
//                reqEvent.end();
//                reqEvent.commit();
//
//              })
              .join();

    } catch (Exception ex) {
      reqEvent.result = ex.getMessage();
      reqEvent.end();
      reqEvent.commit();
      Logger.getLogger(PostRequest.class.getName()).log(Level.SEVERE, null, ex);
    }

    return null;
  }
}
