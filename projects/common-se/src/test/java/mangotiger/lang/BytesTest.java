package mangotiger.lang;

import java.net.InetSocketAddress;

import mangotiger.test.MTestCase;

/**
 * Test the @see Bytes class.
 * @author tom_gagnier@yahoo.com
 */
@SuppressWarnings({"ClassWithoutToString", "MagicNumber", "StringContatenationInLoop"})
public final class BytesTest extends MTestCase {
  private static final byte[] MAX_VALUE = new byte[]{127, -1, -1, -1};
  private static final byte[] MIN_VALUE = new byte[]{-128, 0, 0, 0};
  private static final byte[] NEGATIVE_1 = new byte[]{-1, -1, -1, -1};
  private static final byte[] NEGATIVE_2 = new byte[]{-1, -1, -1, -2};
  private static final byte[] NEGATIVE_42 = new byte[]{-1, -1, -1, -42};
  private static final byte[] POSITIVE_1 = new byte[]{0, 0, 0, 1};
  private static final byte[] POSITIVE_2 = new byte[]{0, 0, 0, 2};
  private static final byte[] POSITIVE_42 = new byte[]{0, 0, 0, 42};
  private static final String NULL_STRING = null;
  private static final byte[] NULL_BYTES = null;
  private static final byte[] ZERO_BYTES = new byte[0];

  /** Test Bytes.toInt(byte[]). */
  public static void testToInt() {
    assertEquals(-1, Bytes.toInt(NEGATIVE_1));
    assertEquals(-2, Bytes.toInt(NEGATIVE_2));
    assertEquals(-42, Bytes.toInt(NEGATIVE_42));
    assertEquals(Integer.MAX_VALUE, Bytes.toInt(MAX_VALUE));
    assertEquals(Integer.MIN_VALUE, Bytes.toInt(MIN_VALUE));
    assertEquals(1, Bytes.toInt(POSITIVE_1));
    assertEquals(2, Bytes.toInt(POSITIVE_2));
    assertEquals(42, Bytes.toInt(POSITIVE_42));
    assertEquals(42, Bytes.toInt(new byte[]{42}));
    assertEquals(42, Bytes.toInt(new byte[]{0, 0, 42}, 2, 2));
    assertEquals(42, Bytes.toInt(new byte[]{0, 0, 42}, 2, 5));
    assertEquals(16777215, Bytes.toInt(new byte[]{-1, -1, -1}, 0, 3));
  }

  /** Test Bytes.toHex(byte[]). */
  public static void testToHex() {
    assertEquals("null", Bytes.toHex(null));
    assertEquals("00 00 00 2a", Bytes.toHex(POSITIVE_42));
    assertEquals("ff ff ff ff", Bytes.toHex(NEGATIVE_1));
    assertEquals("ff ff ff fe", Bytes.toHex(NEGATIVE_2));
    assertEquals("00 00 00 01", Bytes.toHex(POSITIVE_1));
    assertEquals("00 00 00 02", Bytes.toHex(POSITIVE_2));
    assertEquals("00 00 00 02", Bytes.toHex(POSITIVE_2, -1, 5));
    assertEquals("02", Bytes.toHex(POSITIVE_2, 3, 5));
    assertEquals("null", Bytes.toHex(NULL_BYTES, 0, 0));
  }

  /** Test Bytes.toBytes(int). */
  public static void testToBytesFromInt() {
    assertBytesEqual(NEGATIVE_1, -1);
    assertBytesEqual(NEGATIVE_2, -2);
    assertBytesEqual(NEGATIVE_42, -42);
    assertBytesEqual(MAX_VALUE, Integer.MAX_VALUE);
    assertBytesEqual(MIN_VALUE, Integer.MIN_VALUE);
    assertBytesEqual(POSITIVE_1, 1);
    assertBytesEqual(POSITIVE_2, 2);
    assertBytesEqual(POSITIVE_42, 42);
  }

  /** Test Bytes.toBytes(String). */
  public static void testToBytesFromString() {
    assertEquals(0, Bytes.toBytes(NULL_STRING).length);
  }

  public static void testZeroBytes() {
    assertEquals("", Bytes.toHex(ZERO_BYTES, 0, 0));
  }

  /** Test toBytes(long). */
  public static void testToBytesFromLong() {
    final byte[] negative1 = new byte[]{(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff,
                                        (byte)0xff, (byte)0xff};
    assertEquals(negative1, Bytes.toBytes(-1L));
    final byte[] negative2 = new byte[]{(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff,
                                        (byte)0xff, (byte)0xfe};
    assertEquals(negative2, Bytes.toBytes(-2L));
    final byte[] one = new byte[]{(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
                                  (byte)0x01};
    assertEquals(one, Bytes.toBytes(1L));
  }

  public static void testFromInetSocketAddress() {
    final InetSocketAddress address = new InetSocketAddress(80);
    assertBytesEqual(new byte[]{0, 0, 0, 0, 0, 0x50}, Bytes.toBytes(address));
  }

  private static void assertBytesEqual(final byte[] expected, final byte[] actual) {
    assertEquals(expected.length, actual.length);
    for (int i = 0; i < expected.length; ++i) {
      assertEquals(expected[i], actual[i]);
    }
  }

  private static void assertBytesEqual(final byte[] expected, final int integer) {
    assertEquals(expected, Bytes.toBytes(integer));
  }
}
