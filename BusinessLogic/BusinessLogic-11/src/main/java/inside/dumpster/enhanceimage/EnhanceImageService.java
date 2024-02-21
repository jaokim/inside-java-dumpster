/*
 *
 */
package inside.dumpster.enhanceimage;

import inside.dumpster.backend.Backend;
import inside.dumpster.backend.BackendException;
import inside.dumpster.backend.database.Database;
import inside.dumpster.backend.repository.StoredData;
import inside.dumpster.backend.repository.data.LImage;
import inside.dumpster.bl.BusinessLogicException;
import inside.dumpster.bl.BusinessLogicService;
import inside.dumpster.monitoring.event.DataProcessing;
import inside.dumpster.monitoring.event.DataProcessingDetail;
import inside.dumpster.monitoring.event.DataUpload;
import inside.dumpster.outside.Bug;
import inside.dumpster.outside.Buggy;
import static inside.dumpster.uploadimage.UploadImageService.toBufferedImage;
import java.applet.Applet;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
@Buggy(because = "it uses a bytebuffer that isn't freed")
public class EnhanceImageService extends BusinessLogicService<EnhanceImagePayload, EnhanceImageResult> {

  private static final Logger logger = Logger.getLogger(EnhanceImageService.class.getName());
  private static final List<EnhanceImageResult> jackRabbitCache = new CopyOnWriteArrayList<>();

  public EnhanceImageService(Class<EnhanceImagePayload> type, Class<EnhanceImageResult> type1) {
    super(type, type1);
  }

  @Override
  public EnhanceImageResult invoke(EnhanceImagePayload payload) throws BusinessLogicException {
    try {
      EnhanceImageResult result = new EnhanceImageResult();
      final Backend backend = Backend.getInstance();
      final int numOfBytes = payload.getDstBytes();

      Database db = backend.getDatabase();
      DataUpload uploadEvent = new DataUpload();
      uploadEvent.transactionId = payload.getTransactionId();
      uploadEvent.datatype = "Image";
      uploadEvent.srcDevice = payload.getSrcDevice();
      uploadEvent.size = numOfBytes;
      uploadEvent.begin();

      InputStream is;
      is = db.getImageData(payload.getDstPort()); //payload.getInputStream();

      if (is == null) {
        uploadEvent.datatype = "NoData";
        uploadEvent.end();
        uploadEvent.commit();
        return null;
      }


      BufferedImage image;
      int imgByte;

      image = ImageIO.read(is);

      final ByteBuffer bb;
      if (Bug.isBuggy(this)) {
        bb = allocateDirectByteBuffer(numOfBytes);
      } else {
        bb = ByteBuffer.wrap(new byte[numOfBytes]);
      }

      if (image != null) {

        DataProcessing processEvent = new DataProcessing();
        processEvent.transactionId = payload.getTransactionId();
        processEvent.datatype = "Image";
        processEvent.processType = "Enhance";

        processEvent.begin();


        StoredData before = backend.getImageRepository().storeData(new LImage(image));
        System.out.println("Before: "+before.getId());

        processImage(image, bb);

//        CropImageFilter filter = new CropImageFilter(0, 0, image.getHeight()/2, image.getWidth()/2);
//        Applet a = new Applet();
//        Image croppedImage = a.createImage(new FilteredImageSource(image.getSource() , filter));
//
//        image = toBufferedImage(croppedImage);
        processEvent.end();
        processEvent.commit();

        StoredData data = backend.getImageRepository().storeData(new LImage(image));
        System.out.println("After: "+data.getId());

        result.setResult(data.getId().toString());

        uploadEvent.id = data.getId().toString();
      } else {
        uploadEvent.datatype = "NoData";
        uploadEvent.size = 0;
      }
      uploadEvent.end();
      uploadEvent.commit();
      return result;
    } catch (IOException | BackendException ex) {
      ex.printStackTrace();
      throw new BusinessLogicException(ex);
    }
  }

  private ByteBuffer allocateDirectByteBuffer(int size) {
    ByteBuffer directBuffer = ByteBuffer.allocateDirect(size);
    return directBuffer;
  }

//  @Override
//  public Class<JackRabbitPayload> getPayloadClass() {
//    return JackRabbitPayload.class;
//  }
//
//  @Override
//  public Class<JackRabbitResult> getResultClass() {
//    return JackRabbitResult.class;
//  }

  private void processImage(BufferedImage image, ByteBuffer bb) {
    int byteIndex = 0;
    int x = 0, y = 0;

    DataProcessingDetail detail = new DataProcessingDetail();
    while (x < image.getWidth()) {
      detail.x = x;
      while (y < image.getHeight()) {
        detail.y = y;
        detail.byteIndex = byteIndex++;

        detail.begin();

        byte processed = processPixel(x, y, image);
        bb.put(processed);

        detail.end();
        detail.commit();
        y++;
      }
      x++;
    }
  }

  private byte processPixel(int x, int y, BufferedImage image) {
    int rgb = image.getRGB(x, y);
    System.out.println("rgb: "+rgb);
    if (rgb == 0) {
      processPixelReason = "Slow pixel noticed in the system";
      System.out.println(""+processPixelReason);
//      try { Thread.sleep(20); } catch (InterruptedException ex) { }

    }
    byte rgbb = (byte)(rgb);
    return rgbb;
  }


  private String getProcessedPixelReason(int byteIndex) {
    return processPixelReason;
  }
  private String processPixelReason;

}
