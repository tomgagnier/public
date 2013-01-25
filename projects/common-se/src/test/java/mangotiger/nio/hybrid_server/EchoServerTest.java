package mangotiger.nio.hybrid_server;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;

/**
 * A test of the echo tcp_server.
 * @author tom_gagnier@yahoo.com
 */
@SuppressWarnings({"ClassWithoutToString"})
public final class EchoServerTest extends TestCase {
  private static final int NUMBER_OF_MESSAGES = 10;
  private EchoServer echoServer;
  private SimpleClient client;

  @Override protected void setUp() throws Exception {
    super.setUp();
    echoServer = new EchoServer();
    new Thread(echoServer, "echo").start();
    client = new SimpleClient(echoServer.tcp().getLocalAddress());
  }

  @Override protected void tearDown() throws Exception {
    super.tearDown();
    echoServer.halt();
  }

  /** Test TcpAddress equals UdpAddress. */
  public void testTcpAddressEqualsUdpAddress() {
    assertEquals(echoServer.tcp().getLocalAddress(), echoServer.udp().getLocalAddress());
  }

  /** Test the tcp_server's toString() method. */
  public void testToString() {
    final String toString = echoServer.toString();
    final String regex = '^' + echoServer.getClass().getName() + "\\[" + ".+/" + // Server Name
                         "([0-9]{1,3}\\.){3}[0-9]{1,3}" + // IP Address
                         ":[0-9]{1,4}" + // Port Number
                         "\\]$";
    final Pattern pattern = Pattern.compile(regex);
    final Matcher matcher = pattern.matcher(toString);
    assertTrue(toString, matcher.matches());
  }

  /** Test the TCP channel. */
  public void testTcp() throws IOException {
    for (int i = 0; i < NUMBER_OF_MESSAGES; ++i) {
      final String message = message(i);
      assertTcp(message);
    }
  }

  /** Test the UDP channel. */
  public void testUdp() throws IOException {
    for (int i = 0; i < NUMBER_OF_MESSAGES; ++i) {
      final String message = message(i);
      assertUdp(message);
    }
  }

  /** Test both TCP and UDP channels. */
  public void testBoth() throws IOException {
    for (int i = 0; i < NUMBER_OF_MESSAGES; ++i) {
      final String message = message(i);
      assertTcp(message);
      assertUdp(message);
    }
  }

  private void assertTcp(final String message) throws IOException {
    client.tcp().write(message);
    final String response = client.tcp().read();
    assertEquals(message, response);
  }

  private void assertUdp(final String message) throws IOException {
    client.udp().write(message);
    final String response = client.udp().read();
    assertEquals(message, response);
  }

  private static String message(final int i) {
    return new StringBuffer("hello, world! ").append(i).toString();
  }
}
