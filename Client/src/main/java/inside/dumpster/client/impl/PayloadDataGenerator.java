/*
 *
 */
package inside.dumpster.client.impl;

import inside.dumpster.client.Payload;
import inside.dumpster.client.Payload.Destination;
import inside.dumpster.payload.Image;
import inside.dumpster.payload.Text;
import inside.dumpster.service.Service;
import inside.dumpster.service.ServiceLookupOverride;
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
    Text, Image, Nothing
    }

  public InputStream generatePayloadData(Payload payload, Type type) throws IOException {
    switch (type) {
      case Image:
        int dst = payload.getDstBytes();
        int src = payload.getSrcBytes();
        int x,
         y;
        if (dst <= 0) {
          dst = 100;
        }
        if (src <= 0) {
          src = 100;
        }
        x = y = (int) Math.round(Math.sqrt(dst));
        return generateImage(x, y);
      case Text:
        long dstpackets = Integer.parseInt(payload.getDstPackets());
        long sent = dstpackets;
        if (sent == 0) {
          sent = 1000;
        }

        return generateText(payload.getDstPort().hashCode(), Math.max(1, sent / 100));
        default:
      return InputStream.nullInputStream();
    }
  }

  public InputStream generateImage(int x, int y) throws IOException {
    ImageGenerator generator = new ImageGenerator();
    generator.setHeight(y);
    generator.setWidth(x);
    return generator.generateImage();
  }

  public InputStream generateText(int seed, long sentences) throws IOException, NumberFormatException {
    TextGenerator text = new TextGenerator(seed);
//        long srcpackets = Integer.parseInt(payload.getSrcPackets());
    text.setSentences(sentences);
    return text.generateText();
  }

  public Path getJspPath(Payload payload) {
    return new File(payload.getDestination().toString() + File.separator + payload.getProtocol() + File.separator + payload.getDstDevice() + ".jsp").toPath();
  }

  public Type getPayloadDataType(Destination destination) throws IOException {
    ServiceLookupOverride slo = new ServiceLookupOverride();
    Class c = slo.lookupClassForService(destination, null);
    if (c.isAnnotationPresent(Image.class)) {
        return Type.Image;
    }
    if (c.isAnnotationPresent(Text.class)) {
        return Type.Text;
    }
//    switch (destination) {
//      case Comp2:
//      case Comp4:
//      case Comp7:
//      case Comp8:
//        return Type.Image;
//      case Comp3:
//      case Comp9:
//      case IP:
//      default:
//        return Type.Text;
//    }
return Type.Nothing;
  }

  public InputStream generatePayload(Payload payload, Class<? extends Service> service) throws IOException {
     
    if (service.isAnnotationPresent(Image.class)) {
        return generatePayloadData(payload, Type.Image);
    }
    if (service.isAnnotationPresent(Text.class)) {
        return generatePayloadData(payload, Type.Image);
    }
    return generatePayloadData(payload, Type.Nothing);
  }
  public InputStream genetarePayloadData(Payload payload) throws IOException {
    return generatePayloadData(payload, getPayloadDataType(payload.getDestination()));
  }
}
