/*
 * 
 */
package inside.dumpster.client.impl;

import com.github.javafaker.Faker;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
    Faker faker = new Faker();
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    int color = 0;
    for (int y = 0; y < image.getHeight(); y++) {
      for (int x = 0; x < image.getWidth(); x++) {
          image.setRGB(x, y, color++);
      }
    }
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    ImageIO.write(image, "jpeg", os);
    InputStream is = new ByteArrayInputStream(os.toByteArray());
    return is;
  }
}
