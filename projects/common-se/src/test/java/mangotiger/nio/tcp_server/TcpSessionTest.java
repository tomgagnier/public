package mangotiger.nio.tcp_server;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;

import junit.framework.TestCase;

/**
 * Test @link TcpSession.
 *
 * @author tom_gagnier@yahoo.com
 */
public final class TcpSessionTest extends TestCase {

  private static final int BUFFER_SIZE = 128;
  private static final String HELLO_WORLD = "hello world";
  private static final String GOODNIGHT_SWEET_PRINCE = "goodnight, sweet prince";
  private static final String REMAINDER = "remainder";
  private static final String DELIMITER = "\r\n";

  /**
   * Test extract messages.
   *
   * @throws Exception
   */
  public static void testExtractMessages() throws Exception {

    final ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
    final CharBuffer charBuffer = buffer.asCharBuffer();
    String[] messages;

    charBuffer.put(HELLO_WORLD).put(DELIMITER);
    charBuffer.put(GOODNIGHT_SWEET_PRINCE).put(DELIMITER);
    buffer.position(2 * charBuffer.position());
    messages = TcpSession.extractMessages(buffer, DELIMITER);
    assertEquals(3, messages.length);
    assertEquals(HELLO_WORLD, messages[0]);
    assertEquals(GOODNIGHT_SWEET_PRINCE, messages[1]);
    assertEquals("", messages[2]);
    assertEquals("java.nio.HeapByteBuffer[pos=0 lim=128 cap=128]", buffer.toString());

    charBuffer.clear();
    charBuffer.put(HELLO_WORLD).put(DELIMITER);
    charBuffer.put(GOODNIGHT_SWEET_PRINCE).put(DELIMITER);
    charBuffer.put(REMAINDER);
    buffer.position(2 * charBuffer.position());
    messages = TcpSession.extractMessages(buffer, DELIMITER);
    assertEquals(3, messages.length);
    assertEquals(HELLO_WORLD, messages[0]);
    assertEquals(GOODNIGHT_SWEET_PRINCE, messages[1]);
    assertEquals(REMAINDER, messages[2]);
    assertEquals("java.nio.HeapByteBuffer[pos=" + REMAINDER.length() * 2 + " lim=128 cap=128]", buffer.toString());
  }

}