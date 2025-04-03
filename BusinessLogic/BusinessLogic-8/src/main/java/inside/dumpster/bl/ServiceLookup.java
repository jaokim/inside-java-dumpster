/*
 *
 */
package inside.dumpster.bl;

import inside.dumpster.client.Payload;
import static inside.dumpster.client.Payload.Destination.Comp0;
import static inside.dumpster.client.Payload.Destination.Comp1;
import static inside.dumpster.client.Payload.Destination.Comp3;
import static inside.dumpster.client.Payload.Destination.IP;
import inside.dumpster.client.Result;
import inside.dumpster.credits.CreditsService;
import inside.dumpster.eldorado.ElDoradoService;
import inside.dumpster.energy.EnergyService;
import inside.dumpster.monitoring.event.ServiceCall;
import inside.dumpster.monitoring.event.UnhandledServiceCall;
import inside.dumpster.service.ServiceLookupOverride;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class ServiceLookup implements ServiceLookupInterface {
  private static final Logger logger = Logger.getLogger(ServiceLookup.class.getName());

//  class A<T extends Object> {
//  }
//  
//  class Aa {
//    
//}
//  class B extends  A {
//      
//  }
//  
//  Class<? extends A> doIt() {
//      Class<? extends A> a;
//      if (true) {
//        a=B.class;
//      }
//      return a;
//  }
  
  @Override
  public BusinessLogicService<? extends Payload, ? extends Result> lookupServiceWrapper(Payload.Destination destination) throws BusinessLogicException {
      BusinessLogicService<? extends Payload, ? extends Result> service;
      ServiceCall serviceCallEvent = new ServiceCall();
      serviceCallEvent.destination = destination.name();
      
      Function<Payload.Destination, Class> d = (dest) -> {
       Class serviceClass;
        switch(dest) {
            case Comp0:
                serviceClass = EnergyService.class;//();//EnergyPayload.class, EnergyResult.class);
                break;
            case Comp1:
            case Comp3:
                serviceClass = ElDoradoService.class;//(ElDoradoPayload.class, Result.class);
                break;
            case IP:
                serviceClass = CreditsService.class;//(EarnCreditsPayload.class, EarnCreditsResult.class);
                break;
            default:
                UnhandledServiceCall unhandledServiceEvent = new UnhandledServiceCall();
                unhandledServiceEvent.destination = destination.name();
                unhandledServiceEvent.commit();

                serviceClass = DefaultBusinessLogicService.class;//(Payload.class, Result.class);
                //return service;
        }
        return serviceClass;
          //return (Class<? extends BusinessLogicService>)serviceClass;
        };
      
      
      ServiceLookupOverride sl = new ServiceLookupOverride<BusinessLogicService>();
      Class<BusinessLogicService<? extends Payload, ? extends Result>> l = sl.lookupClassForService(destination, d);
      
      try {
          service = l.newInstance();
      } catch (InstantiationException | IllegalAccessException ex) {
          Logger.getLogger(ServiceLookup.class.getName()).log(Level.SEVERE, null, ex);
          service = new DefaultBusinessLogicService();
      }
      return service;
//      serviceCallEvent.serviceClass = service.getClass();
//      serviceCallEvent.commit();
    }

    
  public static void main(String[] args) {
    System.out.println("Java8");
  }
  static {
    main(null);
  }
}
