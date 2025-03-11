/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package inside.dumpster.service;

import inside.dumpster.client.Payload;
import inside.dumpster.client.Result;
import inside.dumpster.outside.Settings;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jsnor
 */
public class ServiceLookupOverride<T> {
    private static final Logger logger = Logger.getLogger(ServiceLookupOverride.class.getName());
    public Class<? extends T> lookupClassForService(Payload.Destination destination, Function<Payload.Destination, Class<? extends T>> serviceLookupFunction) {
        Class<? extends T> service_ = null;
        if (Settings.SERVICE_LOOKUP.isSet()) {
            String serviceLookupSetting = Settings.SERVICE_LOOKUP.get();
            String[] lookups = serviceLookupSetting.split(";");
            logger.info("Service lookup: "+serviceLookupSetting);
            for (String lookup : lookups) {
                String[] regex_classes = lookup.split(":");
                logger.info("Service lookup: "+lookup + "r0: "+regex_classes[0]+" r1: " + regex_classes[1]);
                if (destination.name().matches(regex_classes[0])) {
                    logger.info("Service lookup: matched dest: "+destination);
                    String klass = regex_classes[1];
                    String fullKlass = "inside.dumpster."+klass.replace("Service", "").toLowerCase() + "."+klass+"Service";
                    logger.info("Loading klass: "+fullKlass);
                    try {
                        service_ = (Class<T>)Class.forName(fullKlass);
                        
                    } catch (ClassNotFoundException ex) {
                        logger.log(Level.SEVERE, ex.getMessage(), ex);
                        return null;
                    }
                    return service_;
                }
            }
        }
        if (service_ == null && serviceLookupFunction != null) {
            return serviceLookupFunction.apply(destination);
        }
        return service_;
    }
}
