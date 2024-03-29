/*
 *
 */
package inside.dumpster.client.impl;

import inside.dumpster.client.Payload;
import inside.dumpster.client.Payload.Destination;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class PayloadDataGenerator {
  public enum Type {
    Text,Image
  }
  public InputStream generatePayloadData(Payload payload, Type type) throws IOException {
    switch (type) {
      case Image:
        int dst = payload.getDstBytes();
        int src = payload.getSrcBytes();
        int x, y;
        if(dst <= 0) {
          dst = 100;
        }
        if(src <= 0) {
          src = 100;
        }
        x = y = (int)Math.round(Math.sqrt(dst));
        ImageGenerator generator = new ImageGenerator();
//        System.out.println("Img: "+(int)Math.round(Math.sqrt(dst))/10 + " x "+ (int)Math.round(Math.sqrt(src))/10);
//        generator.setHeight((int)Math.round(Math.sqrt(dst))/10);
//        generator.setWidth((int)Math.round(Math.sqrt(src))/10);
        System.out.println("IOmg: "+x+"x"+y);
        generator.setHeight(y);
        generator.setWidth(x);
        return generator.generateImage();
      case Text:
      default:
        TextGenerator text = new TextGenerator(payload.getDstPort().hashCode());
//        long srcpackets = Integer.parseInt(payload.getSrcPackets());
        long dstpackets = Integer.parseInt(payload.getDstPackets());
        long sent = dstpackets;
        if(sent == 0) {
          sent = 1000;
        }
        text.setSentences(Math.max(1, sent/100));
        return text.generateText();
    }
  }

  public Path getJspPath(Payload payload) {
    return new File(payload.getDestination().toString() + File.separator + payload.getProtocol() + File.separator + payload.getDstDevice()+".jsp").toPath();
  }

  public Type getPayloadDataType(Destination destination) throws IOException {
    switch(destination) {
      case Comp2:
      case Comp4:
      case Comp7:
      case Comp8:
        return Type.Image;
      case Comp3:
      case Comp9:
      case IP:
      default:
        return Type.Text;
    }
  }
  public InputStream genetarePayloadData(Payload payload) throws IOException {
    return generatePayloadData(payload, getPayloadDataType(payload.getDestination()));
  }
}
