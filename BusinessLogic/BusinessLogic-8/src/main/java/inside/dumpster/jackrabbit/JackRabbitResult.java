/*
 *
 */
package inside.dumpster.jackrabbit;

import inside.dumpster.client.Result;
import java.nio.ByteBuffer;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class JackRabbitResult extends Result {
    private final String name;
    private ByteBuffer jackRabbitBuffer;

    public JackRabbitResult(String name) {
        this.name = name;
    }

    public void setImageBuffer(ByteBuffer jackRabbitBuffer) {
        this.jackRabbitBuffer = jackRabbitBuffer;
    }

    public ByteBuffer getImageBuffer() {
        return jackRabbitBuffer;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

  @Override
  protected void finalize() throws Throwable {
    if(jackRabbitBuffer != null) {
      jackRabbitBuffer.clear(); // Surely, this will free it. (No, it won't)
    }
  }


}
