/*
 *
 */
package inside.dumpster.uploadimage;

import inside.dumpster.backend.Backend;
import inside.dumpster.backend.BackendException;
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
@inside.dumpster.payload.Image
public class UploadImageService extends BusinessLogicService<UploadImagePayload, UploadImageResult> {

  public UploadImageService() {
    super(UploadImagePayload.class, UploadImageResult.class);
  }


  @Override
  public UploadImageResult invoke(UploadImagePayload payload) throws BusinessLogicException {
    try {
      UploadImageResult result = new UploadImageResult();
      final Backend backend = Backend.getInstance();
      DataUpload uploadEvent = new DataUpload();
      uploadEvent.transactionId = payload.getTransactionId();
      uploadEvent.datatype = "Image";
      uploadEvent.srcDevice = payload.getSrcDevice();
      uploadEvent.size = payload.getDstBytes();
      uploadEvent.begin();

      InputStream is = backend.getDatabase().getImageData(payload.getDstPort());//payload.getInputStream();
      if(is == null) {
        uploadEvent.datatype = "NoData";
        uploadEvent.end();
        uploadEvent.commit();
        return null;
      }
      BufferedImage image = ImageIO.read(is);
//      DataUtils<Image> utils = new DataUtils<>(new Image());
//      Image image = utils.convertToData(is);
      if (image != null) {
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


        result.setResult(data.getId().toString());

        uploadEvent.id = data.getId().toString();
      } else {
        result.setResult("-1");
      }
      uploadEvent.end();
      uploadEvent.commit();
      return result;
    } catch (BackendException | IOException ex) {
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
