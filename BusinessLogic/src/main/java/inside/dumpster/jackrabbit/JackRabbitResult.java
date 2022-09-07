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
    
    public void setJackRabbitBuffer(ByteBuffer jackRabbitBuffer) {
        this.jackRabbitBuffer = jackRabbitBuffer;
    }

    public ByteBuffer getJackRabbitBuffer() {
        return jackRabbitBuffer;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    
}
