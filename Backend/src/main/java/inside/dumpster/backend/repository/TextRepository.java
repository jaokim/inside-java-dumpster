/*
 * 
 */
package inside.dumpster.backend.repository;

import inside.dumpster.backend.repository.data.Id;
import inside.dumpster.backend.repository.data.Text;
import inside.dumpster.outside.Bug;
import inside.dumpster.outside.Buggy;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
@Buggy(because="using non-buffered streams")
public class TextRepository implements Repository<Text> {
  
  @Override
  public StoredData storeData(final Text im) throws IOException {
    File file = File.createTempFile("textrepo_", ".txt");
    final Id id = new Id(file.getName());
    long len = 0;
    if(im.hasInputStream()) {
      InputStream inputStream = null;
      OutputStream outputStream = null;
      
      try {
        if(Bug.isBuggy(this)) {
          inputStream = im.getInputStream(); 
          outputStream = new FileOutputStream(file);
        } else {
          inputStream = new BufferedInputStream(im.getInputStream()); 
          outputStream = new BufferedOutputStream(new FileOutputStream(file));
        }
        
        int data;
        while((data = inputStream.read()) != -1) {
          outputStream.write(data);
          len++;
        }
      } finally {
        try {inputStream.close();} catch(Exception e) {}
        try {outputStream.close();} catch(Exception e) {}
      }
    } else {
      try (FileOutputStream fos = new FileOutputStream(file)) {
        for(byte b : im.getBuffer()) {
          fos.write(b);
          len++;
        }
      }
    }
    StoredData data = new StoredData(len, id);
    return data;
  }


  @Override
  public void removeData(Id id) {
    
  }
  
  
}
