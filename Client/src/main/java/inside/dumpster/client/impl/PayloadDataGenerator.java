/*
 * 
 */
package inside.dumpster.client.impl;

import inside.dumpster.client.Payload;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class PayloadDataGenerator {
  public InputStream genetarePayloadData(Payload payload) throws IOException {
    switch(payload.getDestination().toString()) {
      case "Comp7":
        ImageGenerator generator = new ImageGenerator();
        long widht_height = Math.round(Math.sqrt(payload.getDstBytes()));
        generator.setHeight((int)widht_height);
        generator.setWidth((int)widht_height);
        return generator.generateImage();
      default: return null;
    }
  }
}
