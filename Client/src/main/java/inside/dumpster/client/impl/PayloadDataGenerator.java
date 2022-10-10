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
      case "Comp8":
        int dst = payload.getDstBytes();
        int src = payload.getSrcBytes();
        if(dst <= 0) {
          dst = src;
        }
        if(src <= 0) {
          src = dst;
        }
        
        ImageGenerator generator = new ImageGenerator();
        generator.setHeight((int)Math.round(Math.sqrt(dst)));
        generator.setWidth((int)Math.round(Math.sqrt(src)));
        return generator.generateImage();
      case "Comp2":
      case "Comp3":
      case "Comp9":
      case "IP5":
        TextGenerator text = new TextGenerator();
        long srcpackets = Integer.parseInt(payload.getSrcPackets());
        long dstpackets = Integer.parseInt(payload.getDstPackets());
        long sent = srcpackets + dstpackets;
        if(sent == 0) {
          sent = 56635;
        }
        text.setSentences(sent);
        return text.generateText();
      default: return null;
    }
  }
}
