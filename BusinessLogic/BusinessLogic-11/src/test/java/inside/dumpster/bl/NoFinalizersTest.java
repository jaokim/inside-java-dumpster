/*
 *
 */
package inside.dumpster.bl;

import com.sun.security.auth.UserPrincipal;
import inside.dumpster.bl.auth.Authenticator;
import inside.dumpster.bl.auth.MustAcceptCookiesError;
import inside.dumpster.bl.auth.UnauthorizedException;
import inside.dumpster.bl.auth.User;
import inside.dumpster.client.Payload;
import inside.dumpster.client.Payload.Destination;
import inside.dumpster.client.Result;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import jdk.jfr.FlightRecorder;
import jdk.jfr.Recording;
//import jdk.jfr.consumer.EventStream;
//import jdk.jfr.consumer.RecordingStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class NoFinalizersTest {
  private static FlightRecorder flightRecorder;
  public NoFinalizersTest() {
  }

  @BeforeAll
  public static void setUpClass() {
    flightRecorder = FlightRecorder.getFlightRecorder();
    assertTrue(FlightRecorder.isAvailable(), "FlightRecorder isn't available");
    assertTrue(FlightRecorder.isInitialized(), "FlightRecorder isn't started");
    Assumptions.assumeTrue(Runtime.version().feature() == 19);
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
   * Unit test using a Recording and temporary file.
   * @throws Exception
   * @since 9
   */
  @Test
  public void testNoServicesUsesFinalizers_Recording() throws Exception {
    final Set<String> classesWithFinalizers = new HashSet<>();
    try (Recording r = new Recording()) {
      r.enable("jdk.FinalizerStatistics");

      r.start();

      callAllServices();

      r.stop();
      Path f = new File("D:/final.jfr").toPath() ;//Files.createTempFile("recording",".jfr");
      r.dump(f);

//      try (EventStream stream = EventStream.openFile(f)) {
//        stream.onEvent("jdk.FinalizerStatistics", event -> {
//          classesWithFinalizers.add(event.getClass("finalizableClass").getName());
//        });
//        stream.start();
//      }
      Files.delete(f);
    }
//    assertEquals(2, classesWithFinalizers.size(), "Finalizers found: " + String.join(",", classesWithFinalizers));
  }


  /**
   * The umbrella event is used to close the recording stream, when we're
   * done with our test.
   */
  class UmbrellaEvent extends jdk.jfr.Event { };

  /**
   * Unit test using ReocrdingStream, and the Umbrella event.
   * @throws Exception
   */
//  @Test
//  public void testNoServicesUsesFinalizers_noStop() throws Exception {
//    final Set<String> classesWithFinalizers = new HashSet<>();
//    try (RecordingStream rs = new RecordingStream()) {
//      // The jdk.FinalizerStatistics is defasult set to period=endChunk,
//      // meaning it have to wait for the chunk to end. Since we close
//      // the stream before the chunk is flushed, the event might be missing.
//      rs.enable("jdk.FinalizerStatistics").with("period", "1ms");
//      rs.onEvent("jdk.FinalizerStatistics", (event) -> {
//        classesWithFinalizers.add(event.getClass("finalizableClass").getName());
//      });
//
//      // The umbrella indicates when we're done wiht the test, and want to close
//      // the stream
//      rs.onEvent(UmbrellaEvent.class.getName(), e -> rs.close());
//
//      rs.startAsync();
//
//      UmbrellaEvent umbrella = new UmbrellaEvent();
//
//      callAllServices();
//
//      umbrella.commit();
//
//      rs.awaitTermination(Duration.ofSeconds(2));
//    }
//    assertEquals(2, classesWithFinalizers.size(), "Finalizers found: " + String.join(",", classesWithFinalizers));
//  }


  /**
   * Unit test using a recording stream wiht a stop() method added in JDK 20.
   * @since 20
   * @throws Exception
   */
//  @Test
//  public void testNoServicesUsesFinalizers_jdk20() throws Exception {
//    final Set<String> classesWithFinalizers = new HashSet<>();
//    try (RecordingStream rs = new RecordingStream()) {
//      rs.enable("jdk.FinalizerStatistics").with("period", "1ms");
//      rs.onEvent("jdk.FinalizerStatistics", (event) -> {
//        classesWithFinalizers.add(event.getClass("finalizableClass").getName());
//      });
//      rs.startAsync();
//
//      callAllServices();
//
//
//      // this tries to do rs.stop();
//      callStop(rs);
//
//    }
//    assertEquals(2, classesWithFinalizers.size(), "Finalizers found: " + String.join(",", classesWithFinalizers));
//  }


  /**
   * Tries to invoke the stop method of RecordingStream which was added in JDK 20.
   * @param stream
   */
//  private void callStop(RecordingStream stream) {
//    try {
//      RecordingStream.class.getMethod("stop").invoke(stream);
//    } catch (NoSuchMethodException | SecurityException | IllegalAccessException | InvocationTargetException ex) {
//      Assumptions.assumeTrue(false, "Couldn't invoke the RecordingStream#stop method? The method was added in JDK 20.");
//    }
//  }



  /**
   * Method that calls all services with dummy data.
   * @throws BusinessLogicException
   */
  private void callAllServices() throws BusinessLogicException, UnauthorizedException {
    Authenticator auth = new Authenticator();
    User user;
    try {
      user = auth.authenticateUser("a", "123", new UserPrincipal("moin"), this, null);
    } catch(MustAcceptCookiesError ex) {
      auth.reauthenticate(ex.getUser());
    }
    final Payload payload = new Payload();
    payload.setDstPort("6745");
    payload.setDstDevice("Port789432");
    BusinessLogicFactory instance = new BusinessLogicFactory();
    BusinessLogicServiceWrapper<? extends Payload, ? extends Result> service;
    service = instance.lookupService(Destination.Unknown);
    Result res = service.invoke(payload);
  }

}
