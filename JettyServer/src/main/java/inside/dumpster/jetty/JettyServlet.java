/*
 *
 */
package inside.dumpster.jetty;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import inside.dumpster.backend.BackendException;
import inside.dumpster.bl.BusinessLogicException;
import inside.dumpster.bl.BusinessLogicFactory;
import inside.dumpster.bl.BusinessLogicServiceWrapper;
import inside.dumpster.bl.auth.Authenticator;
import inside.dumpster.bl.auth.MustAcceptCookiesError;
import inside.dumpster.bl.auth.User;
import inside.dumpster.client.Payload;
import inside.dumpster.client.Payload.Destination;
import inside.dumpster.client.Result;
import inside.dumpster.client.impl.PayloadHelper;
import java.io.IOException;
import java.util.UUID;
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
  private final Authenticator authenticator = new Authenticator();
  private final BusinessLogicFactory factory = new BusinessLogicFactory();

  @Override
  protected void doGet(
          HttpServletRequest request,
          HttpServletResponse response)
          throws ServletException, IOException {
    doPost(request, response);
//    String destination = request.getPathInfo();
//    response.setContentType("application/json");
//    response.setStatus(HttpServletResponse.SC_OK);
//    response.getWriter().println("{ \"status\": \"ok\"}");
  }
  @Override
  protected void doPost(
          HttpServletRequest request,
          HttpServletResponse response)
          throws ServletException, IOException {
    final User user;
    try {
      user = authenticator.authenticateUser(request.getAuthType(), UUID.randomUUID().toString(), request.getUserPrincipal(), new Object(), request.getParameterMap());

//      if (user.isCookieAccepted()) {
//        authenticator.reauthenticate(user);
//      }

      String pathinfo = request.getPathInfo().substring(1);
      String destination = PayloadHelper.getDestination(pathinfo);
      System.out.println("Dest: "+destination);
      Gson gson = getGson();
      BusinessLogicServiceWrapper<? extends Payload, ? extends Result> service =
              factory.lookupService(Destination.fromString(destination));

      Payload payload = new Payload();
      payload = PayloadHelper.fillPayloadFromURI(payload, pathinfo);
      payload.setInputStream(request.getInputStream());

      Result result = service.invoke(payload);

      response.setContentType("application/json");
      request.setAttribute("payload", payload);
      request.setAttribute("result", result);
      request.setAttribute("user", user);
//response.sendRedirect("web/default.jsp");

//      response.setStatus(HttpServletResponse.SC_OK);
      System.out.println("response:"+result.getResult());
//      response.getWriter().write(gson.toJson(result));

    } catch (MustAcceptCookiesError ex) {
      request.setAttribute("acceptCookies", Boolean.TRUE);

    } catch (BusinessLogicException | BackendException ex) {
      Logger.getLogger(JettyServlet.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
      request.getRequestDispatcher("/default.jsp").forward(request, response);
      authenticator.clearSession();
    }
  }

  private Gson getGson() {
    Gson gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
      @Override
      public boolean shouldSkipField(FieldAttributes f) {
        if (f.getName().equals("next")){
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
    return gson;
  }
}
