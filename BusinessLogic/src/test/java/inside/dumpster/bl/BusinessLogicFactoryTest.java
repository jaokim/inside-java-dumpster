/*
 * 
 */
package inside.dumpster.bl;

import inside.dumpster.client.Payload;
import inside.dumpster.client.Result;
import inside.dumpster.monitoring.event.ServiceCall;
import inside.dumpster.monitoring.event.UnhandledServiceCall;
import java.util.ArrayList;
import java.util.List;
import jdk.jfr.Configuration;
import jdk.jfr.FlightRecorder;
import jdk.jfr.consumer.RecordedEvent;
import jdk.jfr.consumer.RecordingStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class BusinessLogicFactoryTest {
  
  public BusinessLogicFactoryTest() {
  }
  
  @BeforeAll
  public static void setUpClass() {
    FlightRecorder rec = FlightRecorder.getFlightRecorder();
//        assertFalse(rec.getRecordings().isEmpty());
        assertTrue(FlightRecorder.isAvailable());
        assertTrue(FlightRecorder.isInitialized());
  }
  
  @AfterAll
  public static void tearDownClass() {
  }
  
  @BeforeEach
  public void setUp() {
  }
  
  @AfterEach
  public void tearDown() {
  }



  /**
   * Test of lookupService method, of class BusinessLogicFactory.
   */
  @Test
  public void testLookupServiceShouldRegisterUnhandledDestinations() throws Exception {
    Configuration c = Configuration.getConfiguration("default");
    c.getSettings().put("disk", "false");
    final List<RecordedEvent> handledServices = new ArrayList<>();
    final List<RecordedEvent> unhandledServices = new ArrayList<>();
    final List<RecordedEvent> allEvents = new ArrayList<>();
      
    Payload.Destination existingdest = new Payload.Destination("Comp1");
    Payload.Destination nonexistingdest = new Payload.Destination("nonexistingdest");
    BusinessLogicFactory instance = new BusinessLogicFactory();
    
    try (RecordingStream rs = new RecordingStream(c)) {
      rs.enable(ServiceCall.class);
      rs.enable(UnhandledServiceCall.class);
      rs.onEvent(event -> allEvents.add( event));
      rs.onEvent("inside.dumpster.ServiceCall", (event) -> handledServices.add(event));
      rs.onEvent("inside.dumpster.UnhandledServiceCall", (event) -> unhandledServices.add(event));
      rs.setReuse(false);
      rs.startAsync();            
              
      BusinessLogicServiceWrapper<? extends Payload, ? extends Result> service = instance.lookupService(nonexistingdest);
      assertNotNull(service);  // we should get a default service
      service.invoke(new Payload());
      service = instance.lookupService(existingdest);
      
      assertNotNull(service);  // we should get a default service
      service.invoke(new Payload());

    Thread.sleep(2000);
      
    }
    
      
    System.out.println("All eventns: "+allEvents.size());        
    for(RecordedEvent event : handledServices) {
      System.out.println(event);
    }
    for(RecordedEvent event : unhandledServices) {
      System.out.println(event);
    }
    assertFalse(unhandledServices.isEmpty(), "No services registered as unhandled");
    assertFalse(handledServices.isEmpty(), "No services registered as handled");
    assertTrue(handledServices.stream().allMatch((event) -> event.getString("Destination").equals(existingdest.toString())));
    assertTrue(unhandledServices.stream().allMatch((event) -> event.getString("Destination").equals(nonexistingdest.toString())));
    
      
  }

  public static void main(String[] args) throws Exception {
    new BusinessLogicFactoryTest().testLookupServiceShouldRegisterUnhandledDestinations();
  }
}
