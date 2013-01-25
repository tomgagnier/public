package mangotiger.net;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import junit.framework.TestCase;

/** Test Base64Encoder encoder. */
public final class Base64Test extends TestCase {
  private static final Log LOG = LogFactory.getLog(Base64Test.class);

  /** Test normal encodings. */
  public static void testEncoding() {
    final String[] INVALID_TEXT = {"!@#$%^&*(*()", "*abcdefghijklmnopqrstuvwxyz", "~0123456789", "9876543210)", "."};
    final String[] VALID_TEXT = {"ABCDEFGHIJKLMNOPQRSTUVWXYZ", "abcdefghijklmnopqrstuvwxyz", "0123456789", "9876543210",
        "+="};
    assertReversibleEncodeDecode("liuliu3d");
    assertReversibleEncodeDecode("xx");
    assertReversibleEncodeDecode("test");
    assertReversibleEncodeDecode(VALID_TEXT);
    assertReversibleEncodeDecode(INVALID_TEXT);
    assertReversibleEncodeDecode("someTextToTest");
  }

  private static void assertReversibleEncodeDecode(final String[] lines) {
    for (String line : lines) {
      assertReversibleEncodeDecode(line);
    }
  }

  private static void assertReversibleEncodeDecode(final String line) {
    final byte[] bytes = line.getBytes();
    final char[] encode = Base64.encode(bytes);
    final byte[] decode = Base64.decode(encode);
    for (int i = 0; i < bytes.length; ++i) {
      assertEquals(bytes[i], decode[i]);
    }
    final String decodedLine = new String(decode);
    assertEquals(line, decodedLine);
    LOG.debug(line + " -> " + new String(encode));
  }
}
