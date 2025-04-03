/*
 *
 */
package inside.dumpster.bl;

import inside.dumpster.client.Payload;
import inside.dumpster.client.Result;
import inside.dumpster.credits.CreditsPayload;
import inside.dumpster.credits.CreditsResult;
import inside.dumpster.credits.CreditsService;
import inside.dumpster.eldorado.ElDoradoPayload;
import inside.dumpster.eldorado.ElDoradoService;
import inside.dumpster.energy.EnergyPayload;
import inside.dumpster.energy.EnergyResult;
import inside.dumpster.energy.EnergyService;
import inside.dumpster.enhanceimage.EnhanceImagePayload;
import inside.dumpster.enhanceimage.EnhanceImageResult;
import inside.dumpster.enhanceimage.EnhanceImageService;
import inside.dumpster.monitoring.event.ServiceCall;
import inside.dumpster.monitoring.event.UnhandledServiceCall;
import inside.dumpster.uploadimage.UploadImagePayload;
import inside.dumpster.uploadimage.UploadImageResult;
import inside.dumpster.uploadimage.UploadImageService;
import inside.dumpster.uploadtext.UploadTextPayload;
import inside.dumpster.uploadtext.UploadTextResult;
import inside.dumpster.uploadtext.UploadTextService;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class ServiceLookup implements ServiceLookupInterface {

  public static void main(String[] args) {
    System.out.println("Java11");
  }

  @Override
  public BusinessLogicService<? extends Payload, ? extends Result> lookupServiceWrapper(Payload.Destination destination) throws BusinessLogicException {
    BusinessLogicService<? extends Payload, ? extends Result> service;
    ServiceCall serviceCallEvent = new ServiceCall();
    serviceCallEvent.destination = destination.name();
    if ((service = SMerviceLookupOverride.overrideService(serviceCallEvent, destination)) != null) {
        
    } else {
      switch(destination) {
          case IP:
              service = new EnergyService();//EnergyPayload.class, EnergyResult.class);
              break;
          case Comp1:
              service = new ElDoradoService();
              break;
          case Comp3:
          case Comp0:
          case Comp2:
              service = new EnhanceImageService();
              break;
          case ActiveDirectory:
              service = new CreditsService();
              break;
          case Comp4:
          case Comp7:
          case Comp8:
              service = new UploadImageService();
              break;
          case Comp9:
              service = new UploadTextService();
              break;
          case Unknown:
          default:
              UnhandledServiceCall unhandledServiceEvent = new UnhandledServiceCall();
              unhandledServiceEvent.destination = destination.name();
              unhandledServiceEvent.commit();

              service = new DefaultBusinessLogicService(Payload.class, Result.class);
              return service;
      }
    }
      serviceCallEvent.serviceClass = service.getClass();
      serviceCallEvent.commit();

      return service;
  }
}
