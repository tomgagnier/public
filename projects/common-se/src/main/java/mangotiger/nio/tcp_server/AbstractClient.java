package mangotiger.nio.tcp_server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A base class for creating small network clients.
 *
 * @author tom_gagnier@yahoo.com
 */
public abstract class AbstractClient implements Runnable {

  private static final Log logger = LogFactory.getLog(AbstractClient.class);

  private final InetSocketAddress host;
  private final ByteBuffer writeBuffer;
  private final ByteBuffer readBuffer;
  private final SocketChannel socketChannel;
  private boolean active = true;

  /**
   * An abstract client.
   *
   * @param hostname   the host name of the tcp_server
   * @param port       the port number of the tcp_server
   * @param bufferSize a suitable buffer size
   * @param timeout    the timeout on a read attempt in milliseconds (zero = infinite)
   *
   * @throws IOException
   */
  protected AbstractClient(final String hostname, final int port, final int bufferSize, final int timeout)
      throws IOException {
    host = new InetSocketAddress(hostname, port);
    logger.info("connecting to " + host);
    socketChannel = SocketChannel.open(host);
    socketChannel.socket().setSoTimeout(timeout);
    writeBuffer = ByteBuffer.allocate(bufferSize);
    readBuffer = ByteBuffer.allocate(bufferSize);
  }

  /**
   * Write this message to the tcp_server.
   *
   * @param message the message to write
   *
   * @throws IOException
   */
  public final void write(final String message) throws IOException {
    synchronized (writeBuffer) {
      writeBuffer.clear();
      final CharBuffer charBuffer = writeBuffer.asCharBuffer();
      charBuffer.put(message).put(TcpChannel.DELIMITER);
      charBuffer.flip();
      writeBuffer.limit(charBuffer.limit() * 2);
      while (writeBuffer.remaining() > 0) {
        socketChannel.write(writeBuffer);
      }
    }
  }

  /**
   * Read the next message.
   *
   * @return the next message.
   *
   * @throws SocketException if the socket timeout has expired
   * @throws IOException
   */
  protected final String read() throws IOException {
    readBuffer.clear();
    socketChannel.read(readBuffer);
    readBuffer.flip();
    final CharBuffer charBuffer = readBuffer.asCharBuffer();
    // assume entire message transmitted
    final int newLimit = readBuffer.limit() / 2 - 1;
    charBuffer.limit(newLimit > 0 ? newLimit : 0);
    return charBuffer.toString();
  }

  /**
   * Close the connection to the tcp_server and stop the run loop.
   */
  protected void close() {
    try {
      socketChannel.close();
      active = false;
    } catch (IOException e) {
      // intentionally empty
    }
  }

  public final void run() {
    try {
      for (active = true; active;) {
        execute();
      }
    } catch (IOException e) {
      logger.error(e, e);
    }
  }

  /**
   * Execute the body of the run loop.
   *
   * @throws IOException
   */
  protected abstract void execute() throws IOException;

  public String toString() {
    return "host=" + host + ",socketChannel=" + socketChannel + ",connected=" + socketChannel.isConnected();
  }

}
