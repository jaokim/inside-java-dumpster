/*
 *
 */
package inside.dumpster.energy;

import inside.dumpster.bl.BusinessLogicException;
import inside.dumpster.outside.Bug;
import inside.dumpster.outside.BugBehaviourMXBean;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import javax.management.MBeanServer;
import javax.management.ObjectName;
//import jdk.jfr.Recording;
//import jdk.jfr.consumer.EventStream;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class EnergyServiceTest {

  public EnergyServiceTest() {
    Logger logger = Logger.getLogger(EnergyDeposit.class.getName());
    ConsoleHandler ch = new ConsoleHandler();
    ch.setLevel(Level.ALL);

    ch.setFormatter(new Formatter() {
      @Override
      public String format(LogRecord record) {
        return String.format("%5s %8d %s\n", record.getLevel(), record.getThreadID(), record.getMessage());
      }
    });
    for (Handler h : logger.getHandlers()) {
      logger.removeHandler(h);
    }
    logger.addHandler(ch);
    logger.setLevel(Level.ALL);

  }

    private String threadPrint() throws Exception {
      final MBeanServer mrBean = ManagementFactory.getPlatformMBeanServer();
      final String[] signature = {"[Ljava.lang.String;"};
      final ObjectName name = ObjectName.getInstance("com.sun.management:type=DiagnosticCommand");
      final Object[] params = new Object[1];
      params[0] = new String[]{};
      Object res = mrBean.invoke(name, "threadPrint", params, signature);
      return res.toString();
    }
//  Recording rec;

  int reached=0, nonreached=0;
  @BeforeEach
  public void before(TestInfo testInfo) throws IOException {
//    rec = new Recording();
//    rec.enable("jdk.JavaMonitorEnter");
//    rec.enable("jdk.JavaMonitorWait");
//    rec.enable("jdk.ThreadStart");
//    rec.enable("jdk.ThreadDump");
    //rec.setDumpOnExit(true);
    //rec.setDestination(Path.of("D:/rec-"+testInfo.getDisplayName()+".jfr"));
//    rec.start();

  }
  @AfterEach
  public void after() throws IOException {
    System.out.println("After!");
//    rec.stop();
//    rec.dump(rec.getDestination());
  }

  /**
   * Test of invoke method, of class EnergyService.
   */
  @Test()
//  @Timeout( value=60, unit = TimeUnit.SECONDS )
  public void testInvoke() throws Exception {
    reached=0;
    nonreached=0;
    BugBehaviourMXBean bb = Bug.getMXBean();
    bb.setBuggy(EnergyDeposit.class.getName(), Boolean.FALSE);
    doTest("Comp1", "22");

    assertTrue(nonreached == 0, "Reached: "+reached + " non-reached: "+nonreached);
    assertTrue(reached != 0, "Reached: "+reached + " non-reached: "+nonreached);

  }

  @Test()
//  @Timeout( value=60, unit = TimeUnit.SECONDS )
  public void testInvokeBuggy() throws Exception {
    reached=0;
    nonreached=0;
    BugBehaviourMXBean bb = Bug.getMXBean();
    bb.setBuggy(EnergyDeposit.class.getName(), Boolean.TRUE);
    doTest("Comp0", "443");
    assertTrue(nonreached != 0, "Reached: "+reached + " non-reached: "+nonreached);

    final String threadDump = threadPrint();
    assertTrue(threadDump.contains("Java-level deadlock"));
  }


 private Collection<EnergyResult> doTest(String srcDevice, String destPort) throws InterruptedException {
   final Collection<EnergyResult> results = new ConcurrentLinkedQueue<>();
    EnergyPayload p1 = new EnergyPayload();

    p1.setSrcDevice(srcDevice);
    p1.setIngoingWattage(550);
    p1.setRequestedWattage(5000);
    p1.setDstPort(destPort);
    p1.setSrcPort("30k");
    p1.setDuration("30000");
    final AtomicInteger id = new AtomicInteger(1);
    System.out.println("Starting one big consumer");
    Thread t = new Thread(() -> {
        EnergyService service = new EnergyService(EnergyPayload.class, EnergyResult.class);
        try {
          p1.setTransactionId(String.valueOf(id.addAndGet(1)));
          EnergyResult res = service.invoke(p1);
          results.add(res);
        } catch (BusinessLogicException ex) {
          Logger.getLogger(EnergyServiceTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        Logger.getLogger(EnergyServiceTest.class.getName()).log(Level.SEVERE, "DONE");
      });
    t.setDaemon(false);
    t.start();


    final EnergyPayload p = new EnergyPayload();
    p.setSrcDevice(srcDevice);
    p.setSrcPort("1k");
    p.setDstPort(destPort);
    p.setIngoingWattage(1000);  // 1000*10 = 10000 -> -5000 -> 5000
    p.setRequestedWattage(50);  // 50*10 = 500
    p.setDuration("1000");


    for (int i = 0; i < 10; i++) {
      Thread t1 = new Thread(() -> {
        EnergyService service = new EnergyService(EnergyPayload.class, EnergyResult.class);
        try {
          p.setTransactionId(String.valueOf(id.addAndGet(1)));
          p.setSrcPort("1k");
          EnergyResult res = service.invoke(p);
          results.add(res);
        } catch (BusinessLogicException ex) {
          Logger.getLogger(EnergyServiceTest.class.getName()).log(Level.SEVERE, null, ex);
        }
      });
      t1.setDaemon(false);
      t1.start();
      //t1.join();
//      Thread t2 = new Thread(() -> {
//        EnergyService service = new EnergyService(EnergyPayload.class, EnergyResult.class);
//        try {
//          service.invoke(p1);
//        } catch (BusinessLogicException ex) {
//          Logger.getLogger(EnergyServiceTest.class.getName()).log(Level.SEVERE, null, ex);
//        }
//      });
//      t2.setDaemon(false);
//      t2.start();

    }
t.join();


//    EnergyService service2 = new EnergyService(EnergyPayload.class, EnergyResult.class);
//    service2.invoke(p);
for(EnergyResult res : results) {
  System.out.println("End RESULTS "+res);
  if (res.isReached()) {
    reached++;
  } else {
    nonreached++;
  }
}
   System.out.println("Reached: "+reached + " non-reached: "+nonreached);

  return results;

  }

}
