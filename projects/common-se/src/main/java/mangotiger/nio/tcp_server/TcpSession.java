package mangotiger.nio.tcp_server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A TCP Channel Client.
 *
 * @author tom_gagnier@yahoo.com
 */
final class TcpSession implements Session {
  private static final Log logger = LogFactory.getLog(TcpSession.class);
  private final SocketChannel socketChannel;
  private final InetSocketAddress address;
  private final ByteBuffer inputBuffer;
  private final ByteBuffer outputBuffer;
  private final Handler handler;
  private final Map context = new HashMap();

  TcpSession(final SocketChannel socketChannel, final Handler handler, final int bufferSize) {
    this.socketChannel = socketChannel;
    this.handler = handler;
    inputBuffer = ByteBuffer.allocate(bufferSize);
    outputBuffer = ByteBuffer.allocate(bufferSize);
    final Socket socket = socketChannel.socket();
    address = new InetSocketAddress(socket.getInetAddress(), socket.getPort());
  }

  public InetSocketAddress getAddress() {
    return address;
  }

  public void read() throws IOException {
    final int bytesRead = socketChannel.read(inputBuffer);
    if (bytesRead <= 0) {
      return;
    }
    final String[] messages = extractMessages(inputBuffer, TcpChannel.DELIMITER);
    for (int i = 0; i < messages.length - 1; ++i) {
      handler.handle(this, messages[i]);
    }
  }

  /**
   * Extract string messages from a buffer.  Messages without delimiters remain in the buffer for future processing.
   *
   * @param buffer    a buffer containing delimited string messages
   * @param delimiter the string message delimiter
   *
   * @return An array of string messages.  Do not use the last element of the array.  It the begining (if any), of a
   *         message whose delimiter has not arrived.
   */
  static String[] extractMessages(final ByteBuffer buffer, final String delimiter) {
    buffer.flip();
    final String input = buffer.asCharBuffer().toString();
    final int greedyMatch = -1;
    final String[] messages = input.split(delimiter, greedyMatch);
    buffer.clear();
    if (messages.length > 0) {
      final String remainder = messages[messages.length - 1];
      for (int i = 0; i < remainder.length(); ++i) {
        buffer.putChar(remainder.charAt(i));
      }
    }
    return messages;
  }

  public void send(final String message) {
    try {
      outputBuffer.clear();
      final CharBuffer charBuffer = outputBuffer.asCharBuffer();
      charBuffer.put(message).put(TcpChannel.DELIMITER);
      charBuffer.flip();
      outputBuffer.limit(charBuffer.limit() * 2);
      logger.debug(ByteBuffers.describe(outputBuffer, "write()"));
      socketChannel.write(outputBuffer);
    } catch (IOException e) {
      logger.error(e, e);
    }
  }

  public Object get(final String key) {
    return context.get(key);
  }

  public void put(final String key, final Object value) {
    context.put(key, value);
  }

  public void close() {
    try {
      logger.debug("closing " + this);
      socketChannel.close();
    } catch (IOException e) {
      logger.debug("expected", e);
    }
  }

  public String toString() {
    return address.toString();
  }

  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof TcpSession)) {
      return false;
    }
    final TcpSession that = (TcpSession) o;
    return address.equals(that.address);
  }

  public int hashCode() {
    return address.hashCode();
  }

}
