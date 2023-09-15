<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="payload" scope="request" class="inside.dumpster.client.Payload"/>
<jsp:useBean id="result" scope="request" class="inside.dumpster.client.Result"/>
<jsp:useBean id="user" scope="request" class="inside.dumpster.bl.auth.User"/>

<!DOCTYPE html>
  <head>
    <title><%=payload.getDestination()%></title>
  </head>
  <body>
    <div>
    <small>User: <%=user.getId()%> <%=user.isCookieAccepted()%></small>
    </div>
    <h2>Dest: <%=payload.getDstDevice()%></h2>
    <h2>Src:  <%=payload.getSrcDevice()%></h2>
    <h1>Result:</h1>
    <div><%=result.getResult()%></div>
  </body>
</html>
