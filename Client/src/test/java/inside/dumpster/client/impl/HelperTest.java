/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package inside.dumpster.client.impl;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author jsnor
 */
public class HelperTest {
    
    public HelperTest() {
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

//    /**
//     * Test of convertPayload method, of class Helper.
//     */
//    @Test
//    public void testConvertPayload() {
//        System.out.println("convertPayload");
//        Payload expResult = null;
//        Payload<Object> p = new Payload<Object>() {};
//        Payload result = Helper.convertPayload(p);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of convertResult method, of class Helper.
//     */
//    @Test
//    public void testConvertResult() {
//        System.out.println("convertResult");
//        Helper instance = new Helper();
//        Result expResult = null;
//        Result result = instance.convertResult(null);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of fixedHash method, of class Helper.
     */
    @Test
    public void testFixedHash() {
        System.out.println("fixedHash");
        String value = "hu";
        int min = 5;
        int max = 10;
        Helper instance = new Helper();
        Set<Integer> results = new HashSet<>();
        for (int i = 0; i < 100 ; i++) {
            value = value + "ha";
            int result = instance.fixedHash(value, min, max);
            assertTrue(result >= 5, result + " not over 5");
            assertTrue(result <= 10, result + " over 10");
            results.add(result);
        }
        // TODO review the generated test code and remove the default call to fail.
        assertTrue(results.size() > 1, "Only same hash returned");
        assertTrue(results.size() > 4, "Only same hash returned");
        assertTrue(results.size() <= 5, "Weird hashes returned: "+ results.size());
    }
    
}
