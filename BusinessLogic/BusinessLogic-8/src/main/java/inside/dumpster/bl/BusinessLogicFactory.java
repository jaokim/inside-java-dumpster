/*
 *
 */
package inside.dumpster.bl;

import inside.dumpster.client.Payload;
import inside.dumpster.client.Payload.Destination;
import inside.dumpster.client.Result;
import java.lang.reflect.InvocationTargetException;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class BusinessLogicFactory {
    public BusinessLogicService getService(Class<? extends BusinessLogicService> serviceClass) throws BusinessLogicException {
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
      return new BusinessLogicServiceWrapper<>(new ServiceLookup().lookupServiceWrapper(destination));
    }

    /**
     * Lookup service for destination.
     * @param destination
     * @return
     * @throws BusinessLogicException
     */
//    private BusinessLogicService<? extends Payload, ? extends Result> lookupServiceWrapper(Destination destination) throws BusinessLogicException {
//      final BusinessLogicService<? extends Payload, ? extends Result> service;
//
//      ServiceCall serviceCallEvent = new ServiceCall();
//      serviceCallEvent.destination = destination.name();
//      switch(destination) {
//          case IP:
//          case Comp0:
//              service = new EnergyService(EnergyPayload.class, EnergyResult.class);
//              break;
//          case Comp1:
//              service = new ElDoradoService(ElDoradoPayload.class, Result.class);
//              break;
//          case Comp2:
//              service = new EnhanceImageService(EnhanceImagePayload.class, EnhanceImageResult.class);
//              break;
//          case Comp3:
//              service = new EarnCreditsService(EarnCreditsPayload.class, EarnCreditsResult.class);
//              break;
//          case Comp4:
//          case Comp7:
//          case Comp8:
//              service = new UploadImageService(UploadImagePayload.class, UploadImageResult.class);
//              break;
//          case Comp9:
//              service = new UploadTextService(UploadTextPayload.class, UploadTextResult.class);
//              break;
//          case Unknown:
//          default:
//              UnhandledServiceCall unhandledServiceEvent = new UnhandledServiceCall();
//              unhandledServiceEvent.destination = destination.name();
//              unhandledServiceEvent.commit();
//
//              service = new DefaultBusinessLogicService(Payload.class, Result.class);
//              return service;
//      }
//      serviceCallEvent.serviceClass = service.getClass();
//      serviceCallEvent.commit();
//
//      return service;
//    }
}
