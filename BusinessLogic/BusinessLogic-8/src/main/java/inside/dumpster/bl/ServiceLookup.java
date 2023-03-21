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
import inside.dumpster.monitoring.event.ServiceCall;
import inside.dumpster.monitoring.event.UnhandledServiceCall;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class ServiceLookup implements ServiceLookupInterface {
  @Override
  public BusinessLogicService<? extends Payload, ? extends Result> lookupServiceWrapper(Payload.Destination destination) throws BusinessLogicException {
      final BusinessLogicService<? extends Payload, ? extends Result> service;
    System.out.println("Java 8 lookup");
      ServiceCall serviceCallEvent = new ServiceCall();
      serviceCallEvent.destination = destination.name();
      switch(destination) {
          case Comp0:
              service = new EnergyService(EnergyPayload.class, EnergyResult.class);
              break;
          case Comp1:
          case Comp3:
              service = new ElDoradoService(ElDoradoPayload.class, Result.class);
              break;
//          case Comp2:
//              service = new EnhanceImageService(EnhanceImagePayload.class, EnhanceImageResult.class);
//              break;
          case IP:
              service = new EarnCreditsService(EarnCreditsPayload.class, EarnCreditsResult.class);
              break;

//          case Comp4:
//          case Comp7:
//          case Comp8:
//              service = new UploadImageService(UploadImagePayload.class, UploadImageResult.class);
//              break;
//          case Comp9:
//              service = new UploadTextService(UploadTextPayload.class, UploadTextResult.class);
//              break;
//          case Unknown:
          default:
              UnhandledServiceCall unhandledServiceEvent = new UnhandledServiceCall();
              unhandledServiceEvent.destination = destination.name();
              unhandledServiceEvent.commit();

              service = new DefaultBusinessLogicService(Payload.class, Result.class);
              return service;
      }
//      serviceCallEvent.serviceClass = service.getClass();
//      serviceCallEvent.commit();

      return service;
    }
  public static void main(String[] args) {
    System.out.println("Java8");
  }
}
