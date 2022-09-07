/*
 * 
 */
package inside.dumpster.backend;

import inside.dumpster.backend.repository.ImageRepository;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class Backend {
  public static Backend getInstance() {
    return new Backend();
  }
  
  
  public ImageRepository getImageRepository() {
    return new ImageRepository();
  }
}
