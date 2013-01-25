package mangotiger.nio.hybrid_server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import mangotiger.nio.ByteBufferSource;

/** The TCP Channel. */
public final class TcpServerChannel extends AbstractPneChannel implements ServerChannel {
  private static final Log LOG = LogFactory.getLog(TcpServerChannel.class);
  private final ByteBufferSource byteBufferSource;
  private final ServerSocketChannel serverSocketChannel;
  private final Map<SocketAddress,TcpCallback> callbacks = new HashMap<SocketAddress, TcpCallback>();
  private final Router router;

  /** A TCP Server Channel. */
  public TcpServerChannel(final ByteBufferSource byteBufferSource, final Router router, final int port) throws IOException {
    this.byteBufferSource = byteBufferSource;
    this.router = router;
    serverSocketChannel = ServerSocketChannel.open();
    serverSocketChannel.socket().bind(new InetSocketAddress(port));
    serverSocketChannel.configureBlocking(false);
    serverSocketChannel.register(selector(), SelectionKey.OP_ACCEPT);
  }

  @Override protected int getPort() {
    return serverSocketChannel.socket().getLocalPort();
  }

  @Override protected void closeChannel() throws IOException {
    serverSocketChannel.socket().close();
    serverSocketChannel.close();
  }

  @Override void processSelectionKey(final SelectionKey key) throws IOException {
    if (key.isAcceptable()) {
      final SocketChannel socketChannel = ((ServerSocketChannel)key.channel()).accept();
      LOG.debug(socketChannel);
      socketChannel.configureBlocking(false);
      final TcpCallback callback = new TcpCallback(this, byteBufferSource, router, socketChannel);
      callbacks.put(callback.getRemoteAddress(), callback);
      socketChannel.register(selector(), SelectionKey.OP_READ | SelectionKey.OP_WRITE)
          .attach(callback);
    } else {
      final TcpCallback callback = (TcpCallback)key.attachment();
      try {
        if (key.isReadable()) {
          callback.read();
        }
        if (key.isWritable()) {
          callback.flushWriteQueue();
        }
      } catch (IOException e) {
        callbacks.remove(callback);
        throw e;
      }
    }
  }

  public void send(final ByteBuffer message, final SocketAddress socketAddress) {
    final TcpCallback callback = callbacks.get(socketAddress);
    try {
      if (callback != null) {
        callback.write(message);
      }
    } catch (IOException e) {
      //todo: Do I need to cancel the selection key?
      callbacks.remove(callback);
      LOG.debug("", e);
    }
  }

  public void queue(final ByteBuffer message, final SocketAddress socketAddress) {
    final TcpCallback callback = getCallback(socketAddress);
    if (callback != null) {
      callback.queue(message);
    }
  }

  private TcpCallback getCallback(final SocketAddress socketAddress) {
    final TcpCallback callback = callbacks.get(socketAddress);
    if (callback == null) {
      LOG.error(new StringBuffer("missing address: ").append(socketAddress), new Exception());
    }
    return callback;
  }

  public Protocol protocol() {
    return Protocol.Tcp;
  }

  public int writeQueueSize() {
    int writeQueueSize = 0;
    for (Object o : callbacks.values()) {
      final Callback callback = (Callback)o;
      writeQueueSize += callback.writeQueueSize();
    }
    return writeQueueSize;
  }

  @Override public String toString() {
    return "TcpServerChannel" + "{byteBufferSource=" + byteBufferSource + ",serverSocketChannel=" +
           serverSocketChannel + ",callbacks=" + callbacks + ",router=" + router + ",super=" + super.toString() + '}';
  }
}
