/*
 * 
 */
package inside.dumpster.client.impl;

import com.github.javafaker.Faker;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class TextGenerator {
  public InputStream generateText() throws IOException {
    Faker faker = new Faker();
    StringBuilder builder = new StringBuilder();
    String fact;
    while(builder.length() < sentences) {
      fact = faker.chuckNorris().fact();
      builder.append(fact);
    }
    InputStream is = new ByteArrayInputStream(builder.toString().getBytes());
    return is;
  }

  long sentences;
  public void setSentences(long sentences) {
    this.sentences = sentences;
  }
}
