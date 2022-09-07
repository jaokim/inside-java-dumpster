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
import inside.dumpster.monitoring.service.ServiceCall;
import inside.dumpster.monitoring.service.UnhandledServiceCall;
import inside.dumpster.uploadimage.UploadImagePayload;
import inside.dumpster.uploadimage.UploadImageResult;
import inside.dumpster.uploadimage.UploadImageService;
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
    
    public BusinessLogicServiceWrapper<? extends Payload, ? extends Result> getServiceWrapper(Destination destination) throws BusinessLogicException {
      return new BusinessLogicServiceWrapper<>(getService(destination.toString()));
    }
    private BusinessLogicService<? extends Payload, ? extends Result> getService(String destination) throws BusinessLogicException {
      ServiceCall serviceCallEvent = new ServiceCall();
      serviceCallEvent.setDestination(destination);
      
      final BusinessLogicService<? extends Payload, ? extends Result> service;
        
      switch(destination) {
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
              UnhandledServiceCall unhandled = new UnhandledServiceCall();
              unhandled.setDestination(destination);
              unhandled.commit();
              service = new DefaultBusinessLogicService(Payload.class, Result.class);
              return service;
      }
      serviceCallEvent.setClass(service.getClass());
      serviceCallEvent.commit();
      return service;


    }
}
