/*
 *
 */
package inside.dumpster.eldorado;

import inside.dumpster.client.Result;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.function.Executable;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class ElDoradoServiceTest {

    public ElDoradoServiceTest() {
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
     * Test of invoke method, of class ElDoradoService.
     */
    @Test
    public void testInvoke() throws Exception {
        System.out.println("invoke");
        ElDoradoPayload payload = new ElDoradoPayload();
        payload.setGold(1254908787);
        ElDoradoService instance = new ElDoradoService(ElDoradoPayload.class, Result.class);

//        StackOverflowError error = assertThrows(StackOverflowError.class, new Executable() {
//            @Override
//            public void execute() throws Throwable {
//                Result result = instance.invoke(payload);
////                fail("No SOE thrown");
//            }
//        });
//        assertNotNull(error);
    }

}
