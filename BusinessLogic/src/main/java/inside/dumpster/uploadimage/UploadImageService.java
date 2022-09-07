/*
 * 
 */
package inside.dumpster.uploadimage;

import inside.dumpster.backend.Backend;
import inside.dumpster.backend.repository.data.DataUtils;
import inside.dumpster.backend.repository.data.Id;
import inside.dumpster.backend.repository.data.Image;
import inside.dumpster.bl.BusinessLogicException;
import inside.dumpster.bl.BusinessLogicService;
import inside.dumpster.monitoring.data.ProcessData;
import inside.dumpster.monitoring.data.UploadData;
import java.io.InputStream;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class UploadImageService extends BusinessLogicService<UploadImagePayload, UploadImageResult> {
 
  public UploadImageService(Class<UploadImagePayload> type, Class<UploadImageResult> type1) {
    super(type, type1);
  }


  @Override
  public UploadImageResult invoke(UploadImagePayload payload) throws BusinessLogicException {
    final Backend backend = Backend.getInstance();
    UploadData uploadEvent = new UploadData();
    uploadEvent.setDatatype("Image");
    uploadEvent.setSrcDevice(payload.getSrcDevice());
    uploadEvent.begin();
   
    InputStream is = payload.getInputStream();
    
    ProcessData processEvent = new ProcessData();
    processEvent.setDatatype("Image");
    processEvent.setProcessType("Convert");
    processEvent.begin();
    
    DataUtils<Image> d = new DataUtils<>();
    Image image = d.convertToData(is);
    processEvent.end();
    
    Id id = backend.getImageRepository().storeData(image);
    
    UploadImageResult result = new UploadImageResult();
    result.setResult(id.toString());
    
    uploadEvent.setId(id.toString());
    uploadEvent.end();
    return result;
  }
  
}
