/*
 * 
 */
package inside.dumpster.uploadtext;

import inside.dumpster.bl.BusinessLogicException;
import inside.dumpster.client.impl.TextGenerator;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
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

//  @Test()
  public void dontestInvoke() throws Exception {
    Boolean[] testCases = {true,false,true,false,true,false,true,false,true,false,true,false};
    UploadTextService instance = new UploadTextService(UploadTextPayload.class, UploadTextResult.class);
    Configuration c = Configuration.getConfiguration("default");
      
    for(int i = 0 ; i < testCases.length; i++) {
      Boolean useBuggyCode = testCases[i];
      System.setProperty("inside.dumpster.UploadText.bug", useBuggyCode.toString());
      final AtomicInteger stringAllocations = new AtomicInteger();
      final AtomicLong weight = new AtomicLong();
      
      try (RecordingStream rs = new RecordingStream(c)) {
        rs.onEvent("jdk.ObjectAllocationSample", (event) -> {
          RecordedClass cls = event.getValue("objectClass");
          long d = event.getValue("weight");
          boolean correctStackTrace = isUploadTextCall(event.getStackTrace());
          if(correctStackTrace) { // && (cls.getName().equals("java.lang.String") || cls.getName().equals("java.lang.StringBuilder"))) {
            stringAllocations.incrementAndGet();
            weight.addAndGet((d));
//            System.out.println(""+event);
          }
        });

        rs.startAsync();
  
        int res = performTest(instance);
        System.out.println(String.format("%s %8d %14d %8d", (useBuggyCode ? "BUG" : "   "), stringAllocations.get(), weight.get(), res));
      }
      
//      System.out.println("String allocations "+(useBuggyCode ? " with bug: " : "without bug: ")+stringAllocations.get());
//      System.out.println("Weight allocations "+(useBuggyCode ? " with bug: " : "without bug: ")+weight.get());
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

  private int performTest(UploadTextService instance) throws IOException, BusinessLogicException {
    int cnt = 0;
    for(int j=0; j<1000; j++) {
      UploadTextPayload payload = new UploadTextPayload();
      payload.setDstBytes(1_000);
      payload.setDstPackets("1890");
      TextGenerator generator = new TextGenerator();
      generator.setSentences(13000);
      payload.setInputStream(generator.generateText());
      
      UploadTextResult result = instance.invoke(payload);
      cnt += Integer.parseInt(result.getResult());
    }
    return cnt;
  }

}
//BUG      440     2181622344
//         455     3046963528
//BUG      447     2867008568
//         406     2916944376
//BUG      406     2742747520
//         411     2610697304
//BUG      381     2599108552
//         356     2419847896
//BUG      337     2365487008
//         334     2291586024
//BUG      327     2142844032
//         448     3112794384