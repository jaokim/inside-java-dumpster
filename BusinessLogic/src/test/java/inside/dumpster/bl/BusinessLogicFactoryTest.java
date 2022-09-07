/*
 * 
 */
package inside.dumpster.bl;

import inside.dumpster.client.Payload;
import inside.dumpster.client.Result;
import java.io.ByteArrayInputStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
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
  public void testGetServiceWrapper() throws Exception {
    System.out.println("getServiceWrapper");
    String destination = "Comp2";
    BusinessLogicFactory instance = new BusinessLogicFactory();
    BusinessLogicServiceWrapper<? extends Payload, ? extends Result> expResult = null;
    BusinessLogicServiceWrapper<? extends Payload, ? extends Result> service = instance.getServiceWrapper(new Payload.Destination(destination));
    Payload payload = new Payload();
    payload.setDstBytes(7);
    payload.setInputStream(new ByteArrayInputStream("123456".getBytes()));
    
    Result result = service.invoke(payload);
//    assertEquals(JackRabbitResult.class, result.getClass());
  }

  
}
