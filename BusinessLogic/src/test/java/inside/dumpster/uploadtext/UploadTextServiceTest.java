/*
 * 
 */
package inside.dumpster.uploadtext;

import inside.dumpster.client.impl.TextGenerator;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import jdk.jfr.Configuration;
import jdk.jfr.consumer.RecordedClass;
import jdk.jfr.consumer.RecordedEvent;
import jdk.jfr.consumer.RecordingStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class UploadTextServiceTest {

  public UploadTextServiceTest() {
  }

  @BeforeAll
  public static void setUpClass() {
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
   * Test of invoke method, of class UploadTextService.
   */
  @Test
  public void testInvoke() throws Exception {
    
    System.setProperty("inside.dumpster.UploadText.bug", "true");
    for(int j = 0; j < 2; j++)
    {  
      UploadTextService instance = new UploadTextService(UploadTextPayload.class, UploadTextResult.class);
      Configuration c = Configuration.getConfiguration("default");
      final List<RecordedEvent> receivedEvents = new ArrayList<>();
      final AtomicInteger gcEvents = new AtomicInteger();
      try (RecordingStream rs = new RecordingStream(c)) {
  //      rs.onEvent(System.out::println);
        rs.onEvent("jdk.ObjectAllocationSample", (event) -> {
          RecordedClass cls = event.getClass("objectClass");
          boolean correctStackTrace = event.getStackTrace().getFrames().stream().anyMatch(frame -> frame.getMethod().getDescriptor().contains("inside.dumpster.uploadtext"));
          if(correctStackTrace && cls.getName().equals("java.lang.String")) {
              System.out.println(event);
        
//            System.out.println(" this was a class");
            gcEvents.incrementAndGet();
          } else {
//          System.out.println(" this was NOT a class "+cls.get);
            
          }
          
        });
  //      rs.onEvent("inside.dumpster.UploadData", (event) -> {
  //        System.out.println(event);
  //        receivedEvents.add(event);
  //      });

        System.out.println("Starting recording stream ...");
        rs.startAsync();
        for(int i=0; i<1000; i++) {
  //        System.out.println("Doing text");
          UploadTextPayload payload = new UploadTextPayload();
          payload.setDstBytes(1_000_000);
          payload.setDstPackets("189200");
          TextGenerator generator = new TextGenerator();
          generator.setSentences(130000);
          payload.setInputStream(generator.generateText());

          UploadTextResult result = instance.invoke(payload);
        }
      }
      System.out.println("GCs: "+gcEvents.get());
      System.setProperty("inside.dumpster.UploadText.bug", "false");
    }
    
//    assertTrue(gcEvents.get() < 10);
//    assertTrue(receivedEvents.stream().allMatch((event) -> event.getString("DataType").equals("Committed")));
//    assertTrue(receivedEvents.stream().noneMatch((event) -> event.getString("DataType").equals("Interrupted")));

  }

}
