/*
 *
 */
package inside.dumpster.enhanceimage;

import inside.dumpster.backend.Backend;
import inside.dumpster.backend.BackendException;
import inside.dumpster.backend.database.Database;
import inside.dumpster.backend.database.DatabaseImpl;
import inside.dumpster.backend.repository.StoredData;
import inside.dumpster.backend.repository.data.LImage;
import inside.dumpster.bl.BusinessLogicException;
import inside.dumpster.bl.BusinessLogicService;
import inside.dumpster.monitoring.event.DataProcessing;
import inside.dumpster.monitoring.event.DataProcessingDetail;
import inside.dumpster.monitoring.event.DataUpload;
import inside.dumpster.outside.Bug;
import inside.dumpster.outside.Buggy;
import inside.dumpster.payload.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
@Buggy(because = "it uses a bytebuffer that isn't freed")
@Image
public class EnhanceImageService extends BusinessLogicService<EnhanceImagePayload, EnhanceImageResult> {
  private static final Logger logger = Logger.getLogger(EnhanceImageService.class.getName());
  private final Backend backend = Backend.getInstance();
  private final Database db = backend.getDatabase();

  public EnhanceImageService(Class<EnhanceImagePayload> type, Class<EnhanceImageResult> type1) {
    super(type, type1);
  }
  public EnhanceImageService() {
    super(EnhanceImagePayload.class, EnhanceImageResult.class);
  }

  @Override
  public EnhanceImageResult invoke(EnhanceImagePayload payload) throws BusinessLogicException {
    try {
      EnhanceImageResult result = new EnhanceImageResult();
      int numOfBytes;

      DataUpload uploadEvent = new DataUpload();
      uploadEvent.transactionId = payload.getTransactionId();
      uploadEvent.datatype = "Image";
      uploadEvent.srcDevice = payload.getSrcDevice();
      uploadEvent.begin();

      InputStream imgData = db.getPayloadData(DatabaseImpl.DataType.Image, payload.toString());

      if (imgData == null) {
        uploadEvent.datatype = "NoData";
        uploadEvent.end();
        uploadEvent.commit();
        return null;
      }
      BufferedImage image;
      image = ImageIO.read(imgData);

      if (image != null) {
        numOfBytes = image.getHeight() * image.getWidth();
        uploadEvent.size = numOfBytes;

        final ByteBuffer bb;
        if (Bug.isBuggy(this)) {
          bb = allocateDirectByteBuffer(numOfBytes);
        } else {
          bb = allocateByteBuffer(numOfBytes);
        }

        DataProcessing processEvent = new DataProcessing();
        processEvent.transactionId = payload.getTransactionId();
        processEvent.datatype = "Image";
        processEvent.processType = "Enhance";

        processEvent.begin();

        StoredData before = backend.getImageRepository().storeData(new LImage(image));
        processImage(image, bb);
        processEvent.end();
        processEvent.commit();

        StoredData data = backend.getImageRepository().storeData(new LImage(image));

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

  private ByteBuffer allocateByteBuffer(int size) {
    ByteBuffer directBuffer = ByteBuffer.allocate(size);
    return directBuffer;
  }

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
