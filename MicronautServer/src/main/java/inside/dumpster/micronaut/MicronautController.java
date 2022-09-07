/*
 * 
 */
package inside.dumpster.micronaut;

import inside.dumpster.bl.BusinessLogicException;
import inside.dumpster.bl.BusinessLogicServiceWrapper;
import inside.dumpster.client.Payload;
import inside.dumpster.client.Result;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
@Controller("/business") 
public class MicronautController {
    private static Logger logger = LoggerFactory.getLogger(MicronautController.class);
        
    private final MicronautService service;

    public MicronautController(MicronautService service) { 
        this.service = service;
    }

    @Post("/dest/{destination}") 
    public Result destination(String destination, @Body Payload payload) throws BusinessLogicException {
        BusinessLogicServiceWrapper businessLogicService = service.getService(destination);
        logger.info("POST: "+destination+ " ");
        logger.debug("POST: "+destination+ " ");
        return businessLogicService.invoke(payload);
    }
    
    @Get("/dest/{destination}") 
    public Result destination(String destination) throws BusinessLogicException {
        return service.getService(destination).invoke(null);
    }
    
    @Get(value="/hello", produces = MediaType.TEXT_PLAIN) 
    public String hello() throws BusinessLogicException {
        logger.info("POST: Everyone says hi ");
        logger.debug("POST: hi h hi hi ");
        return "Hi there!";
    }
}
