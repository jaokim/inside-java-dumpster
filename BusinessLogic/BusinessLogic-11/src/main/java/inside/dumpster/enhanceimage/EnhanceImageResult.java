/*
 *
 */
package inside.dumpster.enhanceimage;

import inside.dumpster.client.Result;
import java.nio.ByteBuffer;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class EnhanceImageResult extends Result {
    private final String name;
    private transient ByteBuffer imageBuffer;

    public EnhanceImageResult() {
        this.name = null;
    }

    public EnhanceImageResult(String name) {
        this.name = name;
    }

    public void setImageBuffer(ByteBuffer imageBuffer) {
        this.imageBuffer = imageBuffer;
    }

    public ByteBuffer getImageBuffer() {
        return imageBuffer;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }


    @Override
    public String toString() {
        return this.getResult();
    }

  @Override
  protected void finalize() throws Throwable {
    if(imageBuffer != null) {
      imageBuffer.clear(); // Surely, this will free it. (No, it won't)
    }
  }


}
