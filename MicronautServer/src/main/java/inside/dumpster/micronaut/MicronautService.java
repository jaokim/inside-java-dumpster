/*
 * 
 */
package inside.dumpster.micronaut;

import inside.dumpster.bl.BusinessLogicException;
import inside.dumpster.bl.BusinessLogicFactory;
import inside.dumpster.bl.BusinessLogicServiceWrapper;
import inside.dumpster.client.Payload;
import jakarta.inject.Singleton;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
@Singleton
public class MicronautService {
    private final BusinessLogicFactory factory = new BusinessLogicFactory();
    public BusinessLogicServiceWrapper getService(String destination) throws BusinessLogicException {
        return factory.getServiceWrapper(new Payload.Destination(destination));
    }
    
}
