package mangotiger.net;

import junit.framework.TestCase;

/**
 * Created by IntelliJ IDEA. Users: bobf Date: Oct 24, 2003 Time: 7:30:51 PM To change this template use Options | File
 * Templates.
 */
@SuppressWarnings({"MagicNumber", "ClassWithoutToString", "UnusedCatchParameter"})
public final class InetAddressesTest extends TestCase {
  /** Test InetAddresses.toBytes(String). */
  public static void testToBytes() {
    final byte[] ip = InetAddresses.toBytes("1.1.1.1");
    assertEquals(4, ip.length);
    assertEquals(1, ip[0]);
    assertEquals(1, ip[1]);
    assertEquals(1, ip[2]);
    assertEquals(1, ip[3]);
  }

  /** Test InetAddresses.toBytes(String). */
  public static void testSignedBytes() {
    final byte[] ip = InetAddresses.toBytes("1.1.1.255");
    assertEquals(4, ip.length);
    assertEquals(1, ip[0]);
    assertEquals(1, ip[1]);
    assertEquals(1, ip[2]);
    assertEquals(-1, ip[3]);
  }

  /** Test InetAddresses.toBytes(String). */
  public static void testIllegalArgument() {
    try {
      InetAddresses.toBytes("1.1.1.0xff");
      fail();
    } catch (Exception e) {
      // expected
    }
    try {
      InetAddresses.toBytes("1.1.1.300");
      fail();
    } catch (Exception e) {
      // expected
    }
  }

  /** Test InetAddresses.toString(byte[]). */
  public static void testToString() {
    assertEquals("255.255.0.1", InetAddresses.toString(new byte[]{-1, -1, 0, 1}));
    assertEquals("255.255.0.1", InetAddresses.toString(new byte[]{(byte)255, (byte)255, 0, 1}));
    assertEquals("null", InetAddresses.toString(null));
    assertEquals("127.0.0.1", InetAddresses.toString(new byte[]{127, 0, 0, 1}));
  }
}
