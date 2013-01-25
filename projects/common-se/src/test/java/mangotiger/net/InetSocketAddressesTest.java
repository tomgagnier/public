package mangotiger.net;

import java.net.*;
import mangotiger.test.*;

/**
 * Test InetSocketAddresses.
 *
 * @author tom_gagnier@yahoo.com
 */
@SuppressWarnings({"ClassWithoutToString", "MagicNumber"})
public final class InetSocketAddressesTest extends MTestCase {
  public static void testLocalhost() throws UnknownHostException {
    final InetSocketAddress expected = new InetSocketAddress("localhost", 52);
    assertEquals(expected, InetSocketAddresses.parseString("localhost:52"));
  }

  public static void testPanasonicCom() throws UnknownHostException {
    final InetSocketAddress expected = new InetSocketAddress("panasonic.com", 80);
    assertEquals(expected, InetSocketAddresses.parseString("panasonic.com:80"));
  }

  public static void testBadAddress() throws UnknownHostException {
    try {
      InetSocketAddresses.parseString("panasonic.com:-1");
      fail();
    } catch (IllegalArgumentException e) {
      // expected
    }
    try {
      InetSocketAddresses.parseString("panasonic.com:100000");
      fail();
    } catch (IllegalArgumentException e) {
      // expected
    }
    try {
      InetSocketAddresses.parseString("too:many:colons");
      fail();
    } catch (IllegalArgumentException e) {
      // expected
    }
  }
}
