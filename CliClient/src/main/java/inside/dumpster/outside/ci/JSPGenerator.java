/*
 *
 */
package inside.dumpster.outside.ci;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * This class just generates some random JSP files.
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class JSPGenerator {
  public static void main(String[] args) throws IOException {
    JSPGenerator generator = new JSPGenerator();

    try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out))) {
      generator.generateJSPFile(writer);
    }

    System.out.println();
  }


  public void generateJSPFile(BufferedWriter out) throws IOException {
    out.write("<%@ page contentType=\"text/html;charset=UTF-8\" language=\"java\" %>"); out.newLine();
    out.write("<jsp:useBean id=\"payload\" scope=\"request\" class=\"inside.dumpster.client.Payload\"\\>"); out.newLine();
    out.write("<jsp:useBean id=\"result\" scope=\"request\" class=\"inside.dumpster.client.Result\"\\>"); out.newLine();
    out.write("<jsp:useBean id=\"user\" scope=\"request\" class=\"inside.dumpster.bl.auth.User\"\\>"); out.newLine();
    out.write("<!DOCTYPE html>"); out.newLine();
    out.write("  <head>"); out.newLine();
    out.write("    <title><%=payload.getDestination()%></title>"); out.newLine();
    out.write("  </head>"); out.newLine();
    out.write("  <body>"); out.newLine();
    out.write("    <div>"); out.newLine();
    out.write("    <small>User: <%=user.getId()> <%=user.isCookieAccepted()></small>"); out.newLine();
    out.write("    </div>"); out.newLine();
    out.write("    <h2>Dest: <%=payload.getDstDevice()%></h2>"); out.newLine();
    out.write("    <h2>Src:  <%=payload.getSrcDevice()%></h2>"); out.newLine();
    out.write("    <h1>Result:</h1>"); out.newLine();
    out.write("    <div><%=result.getResult()%></div>"); out.newLine();
    out.write("  </body>"); out.newLine();
    out.write("</html>"); out.newLine();

  }
}
