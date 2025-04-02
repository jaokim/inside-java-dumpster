/*
 *
 */
package inside.dumpster.jetty;

import inside.dumpster.backend.Backend;
import inside.dumpster.backend.BackendException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.CharBuffer;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class TestServlet extends HttpServlet {

  @Override
  protected void doGet(
          HttpServletRequest request,
          HttpServletResponse response)
          throws ServletException, IOException {
    doPost(request, response);
  }
  @Override
  protected void doPost(
          HttpServletRequest request,
          HttpServletResponse response)
          throws ServletException, IOException {
    response.setStatus(200);
    Backend backend = Backend.builder().build();
    
    response.getWriter().println("Database: "+backend.getDatabase().toString());
    String test = "teststring";
    String testdst = "987654";
    InputStream is = new ByteArrayInputStream(test.getBytes());
    try {
      backend.getDatabase().insertTextData(testdst, is);
      
      InputStream data = backend.getDatabase().getTextData(testdst);
      InputStreamReader bis = new InputStreamReader(data);
      byte[] bytes = new byte[120];
      data.read(bytes);
      response.getWriter().println("Database: "+new String(bytes));
      
      
    } catch (BackendException ex) {
      ex.printStackTrace(response.getWriter());
    }
    
    
    
  }
}
