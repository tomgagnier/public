package mangotiger.util.regex;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.util.BitSet;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import mangotiger.io.InputStreams;
import mangotiger.nio.ByteBuffers;

/**
 * A simple censor that strikes banned phrases from an object.
 * @author tom_gagnier@yahoo.com
 */
public final class Censor {
  private static final Log LOG = LogFactory.getLog(Censor.class);
  private final SortedSet bannedPhrases = new TreeSet();

  /**
   * Ban this phrase.
   * @param phrase the phrase to ban
   */
  private void ban(final String phrase) {
    if (phrase != null) {
      final String trimmedPhrase = phrase.trim();
      if (phrase.length() > 0) {
        bannedPhrases.add(trimmedPhrase);
        final ByteBuffer bb = ByteBuffer.allocate(trimmedPhrase.length() * 2);
        bb.asCharBuffer().put(trimmedPhrase);
        bb.limit(bb.capacity());
        LOG.info(ByteBuffers.describe(bb));
      }
    }
  }

  /**
   * Ban the phrases at this URL.
   * @param url the URL is converted to an input stream.
   * @throws IOException if unable to read the URL.
   */
  public void ban(final URL url) throws IOException {
    ban(url.openStream());
  }

  /**
   * Ban the phrases in this Unicode InputStream.
   * @param in the input stream.
   * @throws IOException if unable to read the input stream
   */
  public void ban(final InputStream in) throws IOException {
    final List<String> strings = InputStreams.utf16InputStreamToLines(in);
    for (String string : strings) {
      ban(string);
    }
  }

  /**
   * Censor the string.
   * @param string the string to censor.
   * @return the censored string.
   */
  public String edit(final String string) {
    if (string == null) {
      return null;
    }
    final BitSet deletedChars = new BitSet(string.length());
    for (Object bannedPhrase1 : bannedPhrases) {
      final String bannedPhrase = (String)bannedPhrase1;
      if (bannedPhrase == null) {
        continue;
      }
      for (int begin = string.indexOf(bannedPhrase, 0);
           begin != -1 && begin < string.length(); begin = string.indexOf(bannedPhrase, begin + 1)) {
        final int end = begin + bannedPhrase.length();
        deletedChars.set(begin, end);
      }
    }
    final int numberToDelete = deletedChars.cardinality();
    if (numberToDelete == 0) {
      return string;
    }
    final int capacity = string.length() - numberToDelete;
    final StringBuffer buffer = new StringBuffer(capacity);
    for (int i = 0; i < capacity; ++i) {
      if (!deletedChars.get(i)) {
        buffer.append(string.charAt(i));
      }
    }
    return buffer.toString();
  }

  /**
   * Censor the buffer.
   * @param buffer the buffer to censor.
   * @return the censored string.
   */
  public String edit(final ByteBuffer buffer) {
    final ByteOrder originalOrder = buffer.order();
    buffer.order(ByteOrder.LITTLE_ENDIAN);
    final String original = buffer.asCharBuffer().toString();
    final String replacement = edit(original);
    //noinspection StringEquality
    if (original != replacement) {
      for (int i = 0; i < replacement.length(); ++i) {
        buffer.putChar(replacement.charAt(i));
      }
      buffer.flip();
    }
    buffer.order(originalOrder);
    return original;
  }

  @Override public String toString() {
    final StringBuffer buf = new StringBuffer();
    buf.append(getClass().getName());
    buf.append("{bannedPhrases=");
    for (Object bannedPhrase1 : bannedPhrases) {
      final String bannedPhrase = (String)bannedPhrase1;
      final ByteBuffer bb = ByteBuffer.allocate(bannedPhrase.length() * 2);
      final CharBuffer cb = bb.asCharBuffer();
      cb.put(bannedPhrase);
      cb.flip();
      final ByteBuffer readOnlyBB = bb.asReadOnlyBuffer();
      final String description = ByteBuffers.describe(readOnlyBB, "\n");
      buf.append(description);
    }
    buf.append('}');
    return buf.toString();
  }
}
