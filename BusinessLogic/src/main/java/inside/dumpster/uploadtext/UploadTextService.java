/*
 * 
 */
package inside.dumpster.uploadtext;

import inside.dumpster.bl.BusinessLogicException;
import inside.dumpster.bl.BusinessLogicService;
import inside.dumpster.monitoring.event.DataUpload;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class UploadTextService extends BusinessLogicService<UploadTextPayload, UploadTextResult>  {

  public UploadTextService(Class<UploadTextPayload> payloadClass, Class<UploadTextResult> resultClass) {
    super(payloadClass, resultClass);
  }
  
  @Override
  public UploadTextResult invoke(UploadTextPayload payload) throws BusinessLogicException {
    DataUpload uploadEvent = new DataUpload();
    uploadEvent.transactionId = payload.getTransactionId();
    uploadEvent.datatype = "Text";
    uploadEvent.srcDevice = payload.getSrcDevice();
    uploadEvent.size = payload.getDstBytes();
    uploadEvent.begin();

    if(payload.getInputStream() == null) {
      // nothing to do
      return null;
    }
    
    UploadTextResult result;
    if(System.getProperty("inside.dumpster.UploadText.bug", "true").equals("true")) {
      result = doItDumb(payload);
    } else {
      result = doItNotSoDumb(payload);
    }
        
    uploadEvent.end();
    uploadEvent.commit();
    return result;
  }
  
  private UploadTextResult doItDumb(UploadTextPayload payload) {
    UploadTextResult result = new UploadTextResult();
    InputStream input = payload.getInputStream();
    
    
    int numberOfChunks = Integer.parseInt(payload.getDstPackets());
    String[] array = new String[numberOfChunks];
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
      for (int i = 0; i<numberOfChunks;  i++) {
        array[i] = new String();
      }
      String line = reader.readLine();
      while (line != null) {
        int linePos = 0;
      
        for (int i = 0; i<numberOfChunks;  i++) {
         array[i] = array[i] + line.substring(linePos, linePos+1);
         linePos++;
         if(linePos >= line.length()) {
           break;
         }
        }
        line = reader.readLine();
      } 

      
    } catch (IOException ex) {
      Logger.getLogger(UploadTextService.class.getName()).log(Level.SEVERE, null, ex);
    }
    return result;
  }
  private UploadTextResult doItNotSoDumb(UploadTextPayload payload) {
    UploadTextResult result = new UploadTextResult();
    InputStream input = payload.getInputStream();
    
    int numberOfChunks = Integer.parseInt(payload.getDstPackets());
    StringBuilder[] array = new StringBuilder[numberOfChunks];
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
      for (int i = 0; i<numberOfChunks;  i++) {
        array[i] = new StringBuilder();
      }
      String line = reader.readLine();
      while (line != null) {
        int linePos = 0;
      
        for (int i = 0; i<numberOfChunks;  i++) {
         array[i].append(line.substring(linePos, linePos+1));
         linePos++;
         if(linePos >= line.length()) {
           break;
         }
        }
        line = reader.readLine();
      } 

      
    } catch (IOException ex) {
      Logger.getLogger(UploadTextService.class.getName()).log(Level.SEVERE, null, ex);
    }
    return result;
  }
  
}
