/*
 *
 */
package inside.dumpster.backend.repository.data;

import java.awt.image.RenderedImage;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class LImage extends AbstractData {
  protected RenderedImage img;
  public LImage(RenderedImage img) {
    this.img = img;
  }


  public RenderedImage getRenderedImage() {
    return img;
  }
}
