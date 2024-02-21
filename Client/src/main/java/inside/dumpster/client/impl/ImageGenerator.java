/*
 *
 */
package inside.dumpster.client.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import javax.imageio.ImageIO;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class ImageGenerator {
  private int width, height;


  public void setWidth(int width) {
    this.width = width;
  }

  public void setHeight(int height) {
    this.height = height;
  }
  public InputStream generateImage() throws IOException {
    if (width <= 0 || height <= 0) {
      return null;
    }
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    int color = 0;
    for (int y = 1; y < image.getHeight(); y++) {
      for (int x = 1; x < image.getWidth(); x++) {
          image.setRGB(x, y, color++);
      }
    }
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    ImageIO.write(image, "jpeg", os);
    byte bs[] = os.toByteArray();
    //System.out.println("Generated: "+bs.length);
    InputStream is = new ByteArrayInputStream(bs);
    return is;
  }
  public static void main(String[] args) throws IOException {
    ImageGenerator img = new ImageGenerator();
    img.setHeight(1);
    img.setWidth(1);
    InputStream is = img.generateImage();
     Scanner s = new Scanner(is).useDelimiter("\\A");
 String result = s.hasNext() ? s.next() : "";
    System.out.println(result);

  }
}
