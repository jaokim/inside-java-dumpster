<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="payload" scope="request" class="inside.dumpster.client.Payload"/>
<jsp:useBean id="result" scope="request" class="inside.dumpster.client.Result"/>
<jsp:useBean id="user" scope="request" class="inside.dumpster.bl.auth.User"/>
<jsp:useBean id="acceptCookies" scope="request" class="java.lang.String"/>
<jsp:useBean id="exception" scope="request" type="java.lang.Exception"/>

<!DOCTYPE html>
  <head>
    <title><%=payload.getDestination()%></title>
  </head>
  <body>
    <div>
        <small>User: <%=user.getId()%> <%=user.isCookieAccepted()%></small><br/>
        <small>Database: <%=inside.dumpster.backend.Backend.getInstance().getDatabase()%></small><br/>
    </div>
    <h2>Dest: <%=payload.getDstDevice()%></h2>
    <h2>Src:  <%=payload.getSrcDevice()%></h2>
    <h1>Result:</h1>
    <div><%=result.getResult()%></div>
    <h1><%=(!"yes".equals(acceptCookies)) ? ("You must accept cookies to enter this site") : ""%></h1>
    <h1><%=(exception != null && !exception.getClass().toString().equals("class java.lang.Throwable")) ? ("Exception occured:" + exception.getClass().toString() + ", " + exception.getMessage()) : ""%></h1>
    
  </body>
</html>
