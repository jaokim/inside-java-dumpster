/*
 * 
 */
package inside.dumpster.bl;

import inside.dumpster.client.Payload;
import inside.dumpster.client.Payload.Destination;
import inside.dumpster.client.Result;
import inside.dumpster.eldorado.ElDoradoPayload;
import inside.dumpster.eldorado.ElDoradoService;
import inside.dumpster.jackrabbit.JackRabbitPayload;
import inside.dumpster.jackrabbit.JackRabbitResult;
import inside.dumpster.jackrabbit.JackRabbitService;
import inside.dumpster.monitoring.event.ServiceCall;
import inside.dumpster.monitoring.event.UnhandledServiceCall;
import inside.dumpster.uploadimage.UploadImagePayload;
import inside.dumpster.uploadimage.UploadImageResult;
import inside.dumpster.uploadimage.UploadImageService;
import inside.dumpster.uploadtext.UploadTextPayload;
import inside.dumpster.uploadtext.UploadTextResult;
import inside.dumpster.uploadtext.UploadTextService;
import java.lang.reflect.InvocationTargetException;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class BusinessLogicFactory {
    public BusinessLogicService getService(Class<BusinessLogicService> serviceClass) throws BusinessLogicException {
        try {
            return serviceClass.getDeclaredConstructor().newInstance();
        } catch (InvocationTargetException | IllegalAccessException | InstantiationException | NoSuchMethodException | SecurityException ex) {
            throw new BusinessLogicException();
        }
    }
    
    /**
     * Lookup and returns service for destination.
     * @param destination
     * @return
     * @throws BusinessLogicException 
     */
    public BusinessLogicServiceWrapper<? extends Payload, ? extends Result> lookupService(Destination destination) throws BusinessLogicException {
      return new BusinessLogicServiceWrapper<>(BusinessLogicFactory.this.lookupService(destination.toString()));
    }
    
    /**
     * Lookup service for destination.
     * @param destination
     * @return
     * @throws BusinessLogicException 
     */
    private BusinessLogicService<? extends Payload, ? extends Result> lookupService(String destination) throws BusinessLogicException {
      final BusinessLogicService<? extends Payload, ? extends Result> service;

      ServiceCall serviceCallEvent = new ServiceCall();
      serviceCallEvent.destination = destination;
      switch(destination) {
          case "Comp0":
              service = new UploadTextService(UploadTextPayload.class, UploadTextResult.class);
              break;
          case "Comp1":
              service = new ElDoradoService(ElDoradoPayload.class, Result.class);
              break;
          case "Comp2":
              service = new JackRabbitService(JackRabbitPayload.class, JackRabbitResult.class);
              break;
          case "Comp7":
              service = new UploadImageService(UploadImagePayload.class, UploadImageResult.class);
              break;
          default:
              UnhandledServiceCall unhandledServiceEvent = new UnhandledServiceCall();
              unhandledServiceEvent.destination = destination;
              unhandledServiceEvent.commit();
              
              service = new DefaultBusinessLogicService(Payload.class, Result.class);
              return service;
      }
      serviceCallEvent.serviceClass = service.getClass();
      serviceCallEvent.commit();
      
      return service;
    }
}
