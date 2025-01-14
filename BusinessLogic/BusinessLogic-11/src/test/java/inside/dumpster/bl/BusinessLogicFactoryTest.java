/*
 *
 */
package inside.dumpster.bl;

import inside.dumpster.client.Payload;
import inside.dumpster.client.Payload.Destination;
import inside.dumpster.client.Result;
import inside.dumpster.monitoring.event.ServiceCall;
import inside.dumpster.monitoring.event.UnhandledServiceCall;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import jdk.jfr.FlightRecorder;
import jdk.jfr.Recording;
import jdk.jfr.consumer.RecordedEvent;
import jdk.jfr.consumer.RecordingFile;
//import jdk.jfr.consumer.RecordingStream;
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
  private static FlightRecorder flightRecorder;
  public BusinessLogicFactoryTest() {
  }

  @BeforeAll
  public static void setUpClass() {
    flightRecorder = FlightRecorder.getFlightRecorder();
    assertTrue(FlightRecorder.isAvailable(), "FlightRecorder isn't available");
    assertTrue(FlightRecorder.isInitialized(), "FlightRecorder isn't started");
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



  private void performTest() throws BusinessLogicException {
    final Payload payload = new Payload();
    BusinessLogicFactory instance = new BusinessLogicFactory();
    BusinessLogicServiceWrapper<? extends Payload, ? extends Result> service;

    service = instance.lookupService(Destination.fromString("nonexisting"));
    assertNotNull(service);  // we should get a default service
    service.invoke(payload);

    service = instance.lookupService(Destination.Comp1);
    assertNotNull(service);  // we should get a default service
    service.invoke(payload);
  }


  /**
   * Test of lookupService method, of class BusinessLogicFactory.
   */
  @Test
  public void testLookupServiceShouldRegisterUnhandledDestinations() throws Exception {
    final List<RecordedEvent> handledServices = new ArrayList<>();
    final List<RecordedEvent> unhandledServices = new ArrayList<>();
    final List<RecordedEvent> allEvents = new ArrayList<>();

    long startTime = System.nanoTime();
    try (Recording r = new Recording()) {
      r.enable(ServiceCall.class);
      r.enable(UnhandledServiceCall.class);
      r.start();

      performTest();

      r.stop();
      Path f = Files.createTempFile("recording",".jfr");
      r.dump(f);


      allEvents.addAll(RecordingFile.readAllEvents(f));
      for(RecordedEvent event : allEvents) {
        if(event.getEventType().getName().equals("inside.dumpster.ServiceCall")) {
          handledServices.add(event);
        }
        if(event.getEventType().getName().equals("inside.dumpster.UnhandledServiceCall")) {
          unhandledServices.add(event);
        }
        System.out.println("event: "+event.toString());
      }
//      try (EventStream stream = EventStream.openFile(f)) {
//        stream.onEvent("jdk.FinalizerStatistics", event -> {
//          classesWithFinalizers.add(event.getClass("finalizableClass").getName());
//        });
//        stream.start();
//      }


    }
        long elapsedTime = System.nanoTime() - startTime;

        System.out.println("Test 1 took: "
                + elapsedTime/1000000);


    System.out.println("All eventns: "+allEvents.size());
    System.out.println("All eventns: "+handledServices.size());
    System.out.println("All eventns: "+unhandledServices.size());
    for(RecordedEvent event : handledServices) {
      System.out.println(event);
    }
    for(RecordedEvent event : unhandledServices) {
      System.out.println(event);
    }

    assertFalse(unhandledServices.isEmpty(), "Services registered as unhandled: "+unhandledServices.size());
    assertFalse(handledServices.isEmpty(), "No services registered as handled: "+handledServices.size());
//    assertTrue(handledServices.stream().allMatch((event) -> event.getString("destination").equals(existingdest.toString())));
//    assertTrue(unhandledServices.stream().allMatch((event) -> event.getString("destination").equals(nonexistingdest.toString())));
//

  }


  public static void main(String[] args) throws Exception {
    new BusinessLogicFactoryTest().testLookupServiceShouldRegisterUnhandledDestinations();
  }
}
