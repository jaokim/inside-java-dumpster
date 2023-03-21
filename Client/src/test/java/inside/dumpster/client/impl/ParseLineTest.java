/*
 * 
 */
package inside.dumpster.client.impl;

import inside.dumpster.client.Payload;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class ParseLineTest {
  
  public ParseLineTest() {
  }

  /**
   * Test of createPayload method, of class ParseLine.
   */
  @Test
  public void testThatTransactionIdIsConsistent() {
    ParseLine<Payload> payload = new ParseLine<Payload>() {
      @Override
      public Payload createPayload() {
        return new Payload();
      }
    };
    String line = "119025,16358,Comp955877,Comp443086,17,Port74289,Port82513,2704,14343,227136,803208";
    Payload p1 = payload.parseLine(line);
    Payload p2 = payload.parseLine(line);
    System.out.println(p1.getTransactionId());
    assertEquals(p1.getTransactionId(), p2.getTransactionId());
    
  }
  

  /**
   * Test of parseLine method, of class ParseLine.
   */
  @Test
  public void testParseLine() {
  }

  /**
   * Test of apply method, of class ParseLine.
   */
  @Test
  public void testApply() {
  }

}
