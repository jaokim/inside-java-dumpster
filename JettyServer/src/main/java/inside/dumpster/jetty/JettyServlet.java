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
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
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
  }

  @Override
  protected void doPost(
          HttpServletRequest request,
          HttpServletResponse response)
          throws ServletException, IOException {
    final User user;
    boolean cookiesAccepted = false;
    try {
      authenticator.loginUser(request.getAuthType(), UUID.randomUUID().toString(), request.getUserPrincipal(), new Object(), request.getParameterMap());
        
      String pathinfo = request.getPathInfo().substring(1);
      String destination = PayloadHelper.getDestination(pathinfo);
      System.out.println("Dest: "+destination);
      BusinessLogicServiceWrapper<? extends Payload, ? extends Result> service =
              factory.lookupService(Destination.fromString(destination));

      Payload payload = new Payload();
      payload = PayloadHelper.fillPayloadFromURI(payload, pathinfo);
      payload.setInputStream(request.getInputStream());

      Result result = service.invoke(payload);
      
      response.setContentType("text/html");
      request.setAttribute("payload", payload);
      request.setAttribute("result", result);
      request.setAttribute("exception", null);

      System.out.println("response:"+result.getResult());

      user = authenticator.getLoggedInUser();
      request.setAttribute("user", user);
      
      if (user.isCookieAccepted()) {
        cookiesAccepted = true;
        final String authTicket  = authenticator.getAuthTicket(user);
        Cookie cookie = new Cookie("auth", authTicket);
        cookie.setComment("Reauth ticket");
        response.addCookie(cookie);
      } else {
        cookiesAccepted = false;
      }
      
      request.setAttribute(COOKIES_ACCEPTED, cookiesAccepted ? "yes" : "no");
      
    } catch (MustAcceptCookiesError ex) {
      cookiesAccepted = false;
      request.setAttribute(COOKIES_ACCEPTED, cookiesAccepted ? "yes" : "no");
    
    } catch (BusinessLogicException | BackendException ex) {
      request.setAttribute("exception", ex);
      
    } finally {
      request.getRequestDispatcher("/default.jsp").forward(request, response);
      authenticator.clearSession();
    }
    
      
    
  }
    protected static final String COOKIES_ACCEPTED = "cookiesAccepted";

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
