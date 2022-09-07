/*
 * 
 */
package inside.dumpster.jetty;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import inside.dumpster.bl.BusinessLogicException;
import inside.dumpster.bl.BusinessLogicFactory;
import inside.dumpster.bl.BusinessLogicServiceWrapper;
import inside.dumpster.client.Payload;
import inside.dumpster.client.Payload.Destination;
import inside.dumpster.client.Result;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class JettyServlet extends HttpServlet {
  private final BusinessLogicFactory factory = new BusinessLogicFactory();
  
  @Override
  protected void doGet(
          HttpServletRequest request,
          HttpServletResponse response)
          throws ServletException, IOException {

    String destination = request.getPathInfo();
    response.setContentType("application/json");
    response.setStatus(HttpServletResponse.SC_OK);
    response.getWriter().println("{ \"status\": \"ok\"}");
  }
  @Override
  protected void doPost(
          HttpServletRequest request,
          HttpServletResponse response)
          throws ServletException, IOException {

    try {
      String destination = request.getPathInfo().substring(1);
      Gson gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
             @Override
             public boolean shouldSkipField(FieldAttributes f) {
                if(f.getName().equals("next")){
                   return true;
                }
                return false;
             }

             @Override
             public boolean shouldSkipClass(Class<?> clazz) {
               if(clazz.getName().equals(Payload.class.getName())) {
                 return false;
               }
                return true;
             }
          }).create();
      BusinessLogicServiceWrapper<? extends Payload, ? extends Result> service = 
              factory.getServiceWrapper(new Destination(destination));
      Payload payload = gson.fromJson(request.getReader(), Payload.class);
      Result result = service.invoke(payload);
      
      response.setContentType("application/json");
      response.setStatus(HttpServletResponse.SC_OK);
      response.getWriter().write(gson.toJson(result));
    } catch (BusinessLogicException ex) {
      Logger.getLogger(JettyServlet.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
}
