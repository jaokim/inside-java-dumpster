/*
 *
 */
package inside.dumpster.micronaut;

import inside.dumpster.bl.BusinessLogicException;
import inside.dumpster.bl.BusinessLogicServiceWrapper;
import inside.dumpster.client.Payload;
import inside.dumpster.client.Result;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import java.io.ByteArrayInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
@Controller("/business")
public class MicronautController {
      static final int MAX_LIMIT;// = 128; /* 0 MAX means disabled */
    static {
        int limit = 16384;
        try {
            String envLimit = System.getenv("JDK_JFR_INTERNAL_STRINGPOOL_MAX_LIMIT");
            if (envLimit != null && envLimit.matches("[0-9]+")) {
                limit = Integer.valueOf(envLimit);
            }
        } finally {
            MAX_LIMIT = limit;
        }
}
    private static Logger logger = LoggerFactory.getLogger(MicronautController.class);

    private final MicronautService service;

    public MicronautController(MicronautService service) {
        this.service = service;
        System.getenv("STRIGN");
    }

    @Post(value = "/dest/{destination}/{srcDevice}/{srcDeviceID}/{srcPort}/{dstDevice}/{dstDeviceId}/{dstPort}/{protocol}", consumes = MediaType.APPLICATION_OCTET_STREAM)
    public Result destination(@Nullable @Body byte[] payload, String destination, String srcDevice, String srcDeviceID, String srcPort, String dstDevice, String dstDeviceId, String dstPort, String protocol) throws BusinessLogicException {

//        HttpRequest<InputStream> httpRequest
      Payload payload2 = new Payload("0", destination, srcDevice, dstDevice, protocol, srcPort, dstPort, srcPort, dstPort, srcPort, dstPort);
      payload2.setInputStream(payload != null ? new ByteArrayInputStream(payload) : null);

        BusinessLogicServiceWrapper businessLogicService = service.getService(destination);
        logger.info("POST: "+destination+ " ");
        logger.debug("POST: "+destination+ " ");
        return businessLogicService.invoke(payload2);
    }

    @Get("/dest/{destination}/{srcDevice}/{srcDeviceID}/{srcPort}/{dstDevice}/{dstDeviceId}/{dstPort}/{protocol}")
    public Result destination(String destination, String srcDevice, String srcDeviceID, String srcPort, String dstDevice, String dstDeviceId, String dstPort, String protocol) throws BusinessLogicException {
      Payload payload = new Payload(null, destination, srcDevice, dstDevice, protocol, srcPort, dstPort, srcPort, dstPort, srcPort, dstPort);
      return service.getService(destination).invoke(payload);
    }

    @Get(value="/hello", produces = MediaType.TEXT_PLAIN)
    public String hello() throws BusinessLogicException {
        logger.info("POST: Everyone says hi ");
        logger.debug("POST: hi h hi hi ");
        return "Hi there!";
    }
}
