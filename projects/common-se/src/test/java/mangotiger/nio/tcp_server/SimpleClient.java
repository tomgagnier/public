package mangotiger.nio.tcp_server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A simple client used for testing.
 *
 * @author tom_gagnier@yahoo.com
 */
public final class SimpleClient {

  private static final Log logger = LogFactory.getLog(SimpleClient.class);

  private static final int BUFFER_SIZE = 128;

  private final InetSocketAddress host;
  private final ByteBuffer writeBuffer = ByteBuffer.allocate(BUFFER_SIZE);
  private final ByteBuffer readBuffer = ByteBuffer.allocate(BUFFER_SIZE);
  private final SocketChannel socketChannel;

  /**
   * Construct SimpleClient from its Host's Address.
   *
   * @param host the tcp_server host address
   *
   * @throws IOException
   */
  public SimpleClient(final InetSocketAddress host) throws IOException {
    this.host = host;
    socketChannel = SocketChannel.open(host);
    logger.info(this);
  }

  /**
   * Write a message to the host.
   *
   * @param message the message to write
   *
   * @throws IOException
   */
  public void write(final String message) throws IOException {
    writeBuffer.clear();
    final CharBuffer charBuffer = writeBuffer.asCharBuffer();
    charBuffer.put(message).put(TcpChannel.DELIMITER);
    charBuffer.flip();
    writeBuffer.limit(charBuffer.limit() * 2);
    while (writeBuffer.remaining() > 0) {
      socketChannel.write(writeBuffer);
    }
  }

  /**
   * Read a message from the host.
   *
   * @return the message from the host
   *
   * @throws IOException
   */
  public String read() throws IOException {
    readBuffer.clear();
    socketChannel.read(readBuffer);
    readBuffer.flip();
    final CharBuffer charBuffer = readBuffer.asCharBuffer();
    // assume entire message transmitted
    final int newLimit = readBuffer.limit() / 2 - 1;
    charBuffer.limit(newLimit > 0 ? newLimit : 0);
    return charBuffer.toString();
  }

  public String toString() {
    return "SimpleClient" +
        "{host=" + host +
        ",socketChannel=" + socketChannel +
        ",connected=" + socketChannel.isConnected() +
        '}';
  }
}
