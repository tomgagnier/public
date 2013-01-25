package mangotiger.nio.hybrid_server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import mangotiger.nio.hybrid_server.AbstractCallback;
import mangotiger.nio.hybrid_server.Router;
import mangotiger.nio.hybrid_server.Sender;
import mangotiger.nio.ByteBufferSource;
import mangotiger.nio.ByteBuffers;

/**
 * TCP Callback code shared by both Server and Client.
 * @author tom_gagnier@yahoo.com
 */
final class TcpCallback extends AbstractCallback {
  private static final Log LOG = LogFactory.getLog(TcpCallback.class);
  private final SocketChannel socketChannel;
  private final InetSocketAddress address;

  TcpCallback(final Sender sender, final ByteBufferSource pool, final Router router, final SocketChannel socketChannel) {
    super(sender, pool, router);
    this.socketChannel = socketChannel;
    final Socket socket = socketChannel.socket();
    address = new InetSocketAddress(socket.getInetAddress(), socket.getPort());
  }

  public void queue(final ByteBuffer message) {
    if (LOG.isDebugEnabled()) {
      LOG.debug(ByteBuffers.describe(message, "queue"));
    }
    if (message.position() == message.limit()) {
      LOG.error("Zero length message", new Exception());
    }
    queueObject(copy(message));
  }

  @Override public InetSocketAddress getRemoteAddress() {
    return address;
  }

  @Override ByteBuffer write(final Object queuedObject) throws IOException {
    return write((ByteBuffer)queuedObject);
  }

  ByteBuffer write(final ByteBuffer message) throws IOException {
    if (LOG.isDebugEnabled()) {
      LOG.debug(ByteBuffers.describe(message, address));
    }
    if (message.position() == message.limit()) {
      LOG.error("Zero length message", new Exception());
    }
    socketChannel.write(message);
    message.rewind();
    return message;
  }

  @Override int readBuffer(final ByteBuffer buffer) throws IOException {
    return socketChannel.read(buffer);
  }

  @Override public String toString() {
    return "TcpCallback{socketChannel=" + socketChannel + ",address=" + address + '}';
  }
}
