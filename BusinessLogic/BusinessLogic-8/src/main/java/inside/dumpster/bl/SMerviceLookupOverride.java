/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package inside.dumpster.bl;

import inside.dumpster.client.Payload;
import inside.dumpster.client.Result;
import inside.dumpster.monitoring.event.ServiceCall;
import inside.dumpster.outside.Settings;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jsnor
 */
public class SMerviceLookupOverride {
    private static final Logger logger = Logger.getLogger(SMerviceLookupOverride.class.getName());
    static BusinessLogicService<? extends Payload, ? extends Result> overrideService(ServiceCall serviceCallEvent, Payload.Destination destination) {
        BusinessLogicService<? extends Payload, ? extends Result> service_ = null;
                    
        if (Settings.SERVICE_LOOKUP.isSet()) {
            String serviceLookupSetting = Settings.SERVICE_LOOKUP.get();
            String[] lookups = serviceLookupSetting.split(";");
            logger.info("Service lookup: "+serviceLookupSetting);
            for (String lookup : lookups) {
                String[] regex_classes = lookup.split(":");
                logger.info("Service lookup: "+lookup + "r0: "+regex_classes[0]+" r1: " + regex_classes[1]);
                if (serviceCallEvent.destination.matches(regex_classes[0])) {
                    logger.info("Service lookup: matched dest: "+serviceCallEvent.destination);
                    String klass = regex_classes[1];
                    String fullKlass = "inside.dumpster."+klass.replace("Service", "").toLowerCase() + "."+klass+"Service";
                    logger.info("Loading klass: "+fullKlass);
                    try {
                        service_ = (BusinessLogicService<? extends Payload, ? extends Result>) Class.forName(fullKlass).newInstance();
                        
                    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
                        logger.log(Level.SEVERE, ex.getMessage(), ex);
                        return null;
                    }
                    return service_;
                }
            }
        }
        return service_;
    }
}
