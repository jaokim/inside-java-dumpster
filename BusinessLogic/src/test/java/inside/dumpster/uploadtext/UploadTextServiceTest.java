/*
 * 
 */
package inside.dumpster.uploadtext;

import inside.dumpster.bl.BusinessLogicException;
import inside.dumpster.client.impl.TextGenerator;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import jdk.jfr.Configuration;
import jdk.jfr.consumer.RecordedClass;
import jdk.jfr.consumer.RecordedStackTrace;
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

  @Test
  public void testInvoke() throws Exception {
    Boolean[] testCases = {true,false,true,false};
    UploadTextService instance = new UploadTextService(UploadTextPayload.class, UploadTextResult.class);
    Configuration c = Configuration.getConfiguration("default");
      
    for(int i = 0 ; i < testCases.length; i++) {  
      Boolean useBuggyCode = testCases[i];
      System.setProperty("inside.dumpster.UploadText.bug", useBuggyCode.toString());
      final AtomicInteger gcEvents = new AtomicInteger();
      try (RecordingStream rs = new RecordingStream(c)) {
        rs.onEvent("jdk.ObjectAllocationSample", (event) -> {
          RecordedClass cls = event.getValue("objectClass");
          boolean correctStackTrace = isUploadTextCall(event.getStackTrace());
          if(correctStackTrace && cls.getName().equals("java.lang.String")) {
            gcEvents.incrementAndGet();
          }
        });

        System.out.println("Starting recording stream ...");
        rs.startAsync();
  
        performTest(instance);
        
      }
      System.out.println("String allocations "+(useBuggyCode ? " with bug: " : "without bug: ")+gcEvents.get());
    }
    
//    assertTrue(gcEvents.get() < 10);
//    assertTrue(receivedEvents.stream().allMatch((event) -> event.getString("DataType").equals("Committed")));
//    assertTrue(receivedEvents.stream().noneMatch((event) -> event.getString("DataType").equals("Interrupted")));

  }

  private boolean isUploadTextCall(RecordedStackTrace stack) {
    return stack != null ? stack.getFrames().stream().anyMatch(
            frame ->
                    frame.getMethod().getDescriptor().contains("inside/dumpster/uploadtext")
    )
            : false;
  }

  private void performTest(UploadTextService instance) throws IOException, BusinessLogicException {
    for(int j=0; j<1000; j++) {
      UploadTextPayload payload = new UploadTextPayload();
      payload.setDstBytes(1_000_000);
      payload.setDstPackets("189200");
      TextGenerator generator = new TextGenerator();
      generator.setSentences(130000);
      payload.setInputStream(generator.generateText());
      
      UploadTextResult result = instance.invoke(payload);
    }
  }

}
