/*
 *
 */
package inside.dumpster.bl;

import inside.dumpster.client.Payload;
import inside.dumpster.client.Result;
import inside.dumpster.credits.EarnCreditsPayload;
import inside.dumpster.credits.EarnCreditsResult;
import inside.dumpster.credits.EarnCreditsService;
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
    final BusinessLogicService<? extends Payload, ? extends Result> service;
    System.out.println("Java11 lookup");
      ServiceCall serviceCallEvent = new ServiceCall();
      serviceCallEvent.destination = destination.name();
      switch(destination) {
          case IP:
              service = new EnergyService(EnergyPayload.class, EnergyResult.class);
              break;
          case Comp1:
              service = new ElDoradoService(ElDoradoPayload.class, Result.class);
              break;
          case Comp3:
          case Comp0:
          case Comp2:
              service = new EnhanceImageService(EnhanceImagePayload.class, EnhanceImageResult.class);
              break;
          case ActiveDirectory:
              service = new EarnCreditsService(EarnCreditsPayload.class, EarnCreditsResult.class);
              break;
          case Comp4:
          case Comp7:
          case Comp8:
              service = new UploadImageService(UploadImagePayload.class, UploadImageResult.class);
              break;
          case Comp9:
              service = new UploadTextService(UploadTextPayload.class, UploadTextResult.class);
              break;
          case Unknown:
          default:
              UnhandledServiceCall unhandledServiceEvent = new UnhandledServiceCall();
              unhandledServiceEvent.destination = destination.name();
              unhandledServiceEvent.commit();

              service = new DefaultBusinessLogicService(Payload.class, Result.class);
              return service;
      }
      serviceCallEvent.serviceClass = service.getClass();
      serviceCallEvent.commit();

      return service;
  }
}
