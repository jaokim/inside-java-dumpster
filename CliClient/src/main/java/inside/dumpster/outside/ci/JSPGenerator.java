/*
 *
 */
package inside.dumpster.outside.ci;

import inside.dumpster.client.Payload;
import inside.dumpster.client.impl.NetFlowData;
import inside.dumpster.client.impl.PayloadDataGenerator;
import inside.dumpster.client.web.HttpPayload;
import inside.dumpster.client.web.HttpPayloadParseLine;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class just generates some random JSP files.
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class JSPGenerator {
  public static void main(String[] args) throws IOException {
    JSPGenerator generator = new JSPGenerator();

    NetFlowData<HttpPayload> data = new NetFlowData<HttpPayload>(new HttpPayloadParseLine());

    final List<HttpPayload> jspPages = new ArrayList<>();
    data.getStream().filter((payload) -> {
      if (Collections.binarySearch(jspPages, payload,
              (HttpPayload o1, HttpPayload o2) ->
                      new PayloadDataGenerator().getJspPath(o1).
                              compareTo(new PayloadDataGenerator().getJspPath(o2))) != 0) {
        jspPages.add(payload);
        return true;
      }
      return false;
    }).forEach(action -> {

      File jspFile = new File("E:/jsp/" + new PayloadDataGenerator().getJspPath(action));
      jspFile.getParentFile().mkdirs();
      if (!jspFile.exists()) {
        try {
          jspFile.createNewFile();
        } catch (IOException ex) {
          Logger.getLogger(JSPGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
      try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
                jspFile)))) {
          generator.generateJSPFile(action, writer);
        } catch (IOException ex) {
        Logger.getLogger(JSPGenerator.class.getName()).log(Level.SEVERE, null, ex);
      }
    });
    System.out.println();
  }


  public void generateJSPFile(Payload payload, BufferedWriter out) throws IOException {
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
    switch (new PayloadDataGenerator().getPayloadDataType(payload.getDestination())) {
      case Image:
        out.write("    <img src=\"%=result.getResult()%\">"); out.newLine();
        break;
      case Text:
        break;
    }
    out.write("  </body>"); out.newLine();
    out.write("</html>"); out.newLine();

  }
}
