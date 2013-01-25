package mangotiger.util.regex;

/**
 * @author tom_gagnier@yahoo.com
 */

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import junit.framework.TestCase;
import mangotiger.nio.ByteBuffers;

/**
 * Test the Censor. <p/> Hex dumps of the two test files: <code> od -h james-big-endian.txt 0000000 fffe cd53 7151 0d00
 * 0a00 4600 6100 6e00 0000020 2000 4700 6f00 6e00 6700 0d00 0a00 0d00 0000040 0a00 d56c 6e8f 9f52 0d00 0a00 4600 6100
 * 0000060 2000 4c00 7500 6e00 2000 4700 6f00 6e00 0000100 6700 0d00 0a00 0d00 0a00 d64e 8859 8476 0000120 0d00 0a00
 * 5400 6100 2000 4d00 6100 2000 0000140 4400 6500 0d00 0a00 0d00 0a00 577f 2f4f 0000160 7972 0d00 0a00 4c00 7500 6f00
 * 2000 4200 0000200 6f00 2000 5400 6500 0000210 <p/> od -h james-little-endian.txt 0000000 feff 53cd 5171 000d 000a
 * 0046 0061 006e 0000020 0020 0047 006f 006e 0067 000d 000a 000d 0000040 000a 6cd5 8f6e 529f 000d 000a 0046 0061
 * 0000060 0020 004c 0075 006e 0020 0047 006f 006e 0000100 0067 000d 000a 000d 000a 4ed6 5988 7684 0000120 000d 000a
 * 0054 0061 0020 004d 0061 0020 0000140 0044 0065 000d 000a 000d 000a 7f57 4f2f 0000160 7279 000d 000a 004c 0075 006f
 * 0020 0042 0000200 006f 0020 0054 0065 0000210 </code>
 */
public final class CensorTest extends TestCase {
  private static final Log LOG = LogFactory.getLog(CensorTest.class);
  private Censor censor;
  private static final Pair[] PAIRS = new Pair[]{new Pair("Fan Gong", ""), new Pair("\u53cd\u5171", ""),
      new Pair("I like the Fa Lun Gong", "I like the "), new Pair("\u53cd\u5171", ""),};

  @Override protected void setUp() throws Exception {
    super.setUp();
    censor = new Censor();
  }

  /** Test banning the contents of a Big Endian input stream. */
  public void testBigEndian() throws Exception {
    initializeCensor("james-big-endian.txt");
    assertDirtyWords();
  }

  /** Test banning the contents of a Little Endian input stream. */
  public void testLittleEndian() throws Exception {
    initializeCensor("james-little-endian.txt");
    assertDirtyWords();
  }

  private void initializeCensor(final String resource) throws IOException {
    final InputStream is = asStream(resource);
    censor.ban(is);
    LOG.info(censor);
  }

  private InputStream asStream(final String resource) {
    return getClass().getResourceAsStream(resource);
  }

  /** Test the toString method. */
  public void testToString() throws IOException {
    final Censor littleEndianCensor = new Censor();
    littleEndianCensor.ban(asStream("james-little-endian.txt"));
    final Censor bigEndianCensor = new Censor();
    bigEndianCensor.ban(asStream("james-big-endian.txt"));
    final String littleEndianString = littleEndianCensor.toString();
    final String bigEndianString = bigEndianCensor.toString();
    assertEquals(littleEndianString, bigEndianString);
    LOG.info(bigEndianString);
  }

  /** Test editing a byte buffer. */
  public void testEditByteBuffer() throws IOException {
    initializeCensor("james-little-endian.txt");
    for (Pair aPAIRS : PAIRS) {
      final ByteBuffer buffer = ByteBuffer.allocate(128);
      put(buffer, aPAIRS.input);
      LOG.debug(ByteBuffers.describe(buffer));
      final String before = censor.edit(buffer);
      LOG.debug(ByteBuffers.describe(buffer));
      assertEquals(aPAIRS.input, before);
      final String after = buffer.asCharBuffer().toString();
      assertEquals(aPAIRS.expect, after);
    }
  }

  private static void put(final ByteBuffer buffer, final String phrase) {
    buffer.order(ByteOrder.LITTLE_ENDIAN);
    final CharBuffer charBuffer = buffer.asCharBuffer();
    charBuffer.put(phrase);
    charBuffer.flip();
    buffer.limit(charBuffer.limit() * 2);
  }

  private void assertDirtyWords() {
    for (final Pair pair : PAIRS) {
      final String actual = censor.edit(pair.input);
      assertEquals(pair.expect, actual);
    }
  }

  static final class Pair {
    final String input;
    final String expect;

    Pair(final String before, final String after) {
      input = before;
      expect = after;
    }
  }
}
