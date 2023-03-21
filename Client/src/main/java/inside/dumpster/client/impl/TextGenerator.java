/*
 *
 */
package inside.dumpster.client.impl;

import com.github.javafaker.Faker;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class TextGenerator {
  private long seed = 0;

  public TextGenerator() {
  }
  public TextGenerator(long seed) {
    this.seed = seed;
  }
  public InputStream generateText() throws IOException {
    Faker faker = new Faker(new Random(seed));
    StringBuilder builder = new StringBuilder();
    String fact;
    while(builder.length() < sentences) {
      fact = faker.chuckNorris().fact();
      builder.append(fact);
      builder.append(" \n");
    }
    InputStream is = new ByteArrayInputStream(builder.toString().getBytes());
    return is;
  }

  long sentences;
  public void setSentences(long sentences) {
    this.sentences = sentences;
  }
}
