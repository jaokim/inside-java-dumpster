/*
 * 
 */
package inside.dumpster.uploadimage;

import inside.dumpster.backend.Backend;
import inside.dumpster.backend.repository.StoredData;
import inside.dumpster.backend.repository.data.LImage;
import inside.dumpster.bl.BusinessLogicException;
import inside.dumpster.bl.BusinessLogicService;
import inside.dumpster.monitoring.event.DataProcessing;
import inside.dumpster.monitoring.event.DataUpload;
import java.applet.Applet;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

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
    try {
      final Backend backend = Backend.getInstance();
      DataUpload uploadEvent = new DataUpload();
      uploadEvent.transactionId = payload.getTransactionId();
      uploadEvent.datatype = "Image";
      uploadEvent.srcDevice = payload.getSrcDevice();
      uploadEvent.size = payload.getDstBytes();
      uploadEvent.begin();
      
      InputStream is = payload.getInputStream();
      if(is == null) {
        uploadEvent.datatype = "NoData";
        uploadEvent.end();
        uploadEvent.commit();
        return null;
      }
      BufferedImage image = ImageIO.read(is);
//      DataUtils<Image> utils = new DataUtils<>(new Image());
//      Image image = utils.convertToData(is);
          
      DataProcessing processEvent = new DataProcessing();
      processEvent.transactionId = payload.getTransactionId();
      processEvent.datatype = "Image";
      processEvent.processType = payload.getProcessing().name();
          
      switch(payload.getProcessing()) {
        case Convert:
          processEvent.begin();
          CropImageFilter filter = new CropImageFilter(0, 0, image.getHeight()/2, image.getWidth()/2);
          Applet a = new Applet();
          Image croppedImage = a.createImage(new FilteredImageSource(image.getSource() , filter));
          image = toBufferedImage(croppedImage);
          processEvent.end();
          break;
        case None:
          break;
      }
      processEvent.commit();
      
      StoredData data = backend.getImageRepository().storeData(new LImage(image));
      
      UploadImageResult result = new UploadImageResult();
      result.setResult(data.getId().toString());
      
      uploadEvent.id = data.getId().toString();
      uploadEvent.end();
      uploadEvent.commit();
      return result;
    } catch (IOException ex) {
      throw new BusinessLogicException();
    }
  }
    
    public static BufferedImage toBufferedImage(Image img)
{
    if (img instanceof BufferedImage)
    {
        return (BufferedImage) img;
    }

    // Create a buffered image with transparency
    BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

    // Draw the image on to the buffered image
    Graphics2D bGr = bimage.createGraphics();
    bGr.drawImage(img, 0, 0, null);
    bGr.dispose();

    // Return the buffered image
    return bimage;
}
  
  
}
