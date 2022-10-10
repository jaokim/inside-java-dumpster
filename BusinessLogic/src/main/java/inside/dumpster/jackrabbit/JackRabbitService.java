/*
 * 
 */
package inside.dumpster.jackrabbit;

import inside.dumpster.bl.BusinessLogicException;
import inside.dumpster.bl.BusinessLogicService;
import inside.dumpster.outside.Bug;
import inside.dumpster.outside.Buggy;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Go jack-rabbit running through the wood
 * You had a good night and you feel real loose
 * Heard they got you going round the goosecreek shed
 * Trying to fill your belly full of buckshot lead
 * 
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
@Buggy(because="it uses a bytebuffer that isn't freed")
public class JackRabbitService extends BusinessLogicService<JackRabbitPayload, JackRabbitResult> {
    private static final Logger logger = Logger.getLogger(JackRabbitService.class.getName());
    private static final List<JackRabbitResult> jackRabbitCache = new CopyOnWriteArrayList<>();

  public JackRabbitService(Class<JackRabbitPayload> type, Class<JackRabbitResult> type1) {
    super(type, type1);
  }
    @Override
    public JackRabbitResult invoke(JackRabbitPayload payload) throws BusinessLogicException {
        final String jackRabbitName;
        if(payload.getDstDeviceId() != null) {
          jackRabbitName = payload.getDstDeviceId();
        } else {
          jackRabbitName = UUID.randomUUID().toString();
        }
        
        InputStream is = payload.getInputStream();
        final int numOfBytes = payload.getDstBytes();
        final ByteBuffer bb;
        if(Bug.isBuggy(this)) {
          bb = allocateDirectByteBuffer(numOfBytes);
        } else {
          bb = ByteBuffer.wrap(new byte[numOfBytes]);
        }
        
        String content = "";
        int index = 0;
        if(is != null) {
          do {
            try {
              bb.put(is.readNBytes(numOfBytes));
              index += content.getBytes().length;
              content =  bb.toString();
            } catch (IOException ex) {
              Logger.getLogger(JackRabbitService.class.getName()).log(Level.SEVERE, null, ex);
            }
          } while(index < bb.limit() && bb.limit() < numOfBytes);

          logger.log(Level.INFO, String.format("Adding: %25s", bb.toString()));
        }
        for(JackRabbitResult jackRabbit : jackRabbitCache) {
            if(jackRabbit.getName().equals(jackRabbitName)) {
                jackRabbit.setJackRabbitBuffer(bb);
                return jackRabbit;
            }
        }
        JackRabbitResult result = new JackRabbitResult(jackRabbitName);
        result.setJackRabbitBuffer(bb);
        jackRabbitCache.add(result);
        
        return result;
    }
    
    private ByteBuffer allocateDirectByteBuffer(int size) {
        ByteBuffer directBuffer = ByteBuffer.allocateDirect(size);
        return directBuffer;
    }

//  @Override
//  public Class<JackRabbitPayload> getPayloadClass() {
//    return JackRabbitPayload.class;
//  }
//
//  @Override
//  public Class<JackRabbitResult> getResultClass() {
//    return JackRabbitResult.class;
//  }
    
    
}
