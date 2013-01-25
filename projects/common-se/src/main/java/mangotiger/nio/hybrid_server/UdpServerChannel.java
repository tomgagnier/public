package mangotiger.nio.hybrid_server;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import mangotiger.nio.ByteBufferSource;

/** The TCP Channel. */
public final class UdpServerChannel extends AbstractPneChannel implements ServerChannel {
  private static final Log LOG = LogFactory.getLog(UdpServerChannel.class);
  private final UdpCallback callback;
  private final DatagramChannel datagramChannel;
  private final SelectionKey key;

  /**
   * A UDP tcp_server.
   * @param byteBufferSource the I/O multiplexor.
   * @param router           the message router.
   * @param port             UDP tcp_server's local port.
   */
  public UdpServerChannel(final ByteBufferSource byteBufferSource, final Router router, final int port) throws IOException {
    datagramChannel = DatagramChannel.open();
    final DatagramSocket socket = datagramChannel.socket();
    final SocketAddress address = new InetSocketAddress(port);
    socket.bind(address);
    datagramChannel.configureBlocking(false);
    final int operations = SelectionKey.OP_READ | SelectionKey.OP_WRITE;
    key = datagramChannel.register(selector(), operations);
    callback = new UdpCallback(this, byteBufferSource, router, datagramChannel);
    key.attach(callback);
    LOG.info("UDP Port: " + port);
  }

  public void send(final ByteBuffer message, final SocketAddress target) {
    try {
      callback.write(message, target);
    } catch (IOException e) {
      key.cancel();
      LOG.error(callback, e);
    }
  }

  public void queue(final ByteBuffer message, final SocketAddress target) {
    callback.queue(message, target);
  }

  @Override protected int getPort() {
    return datagramChannel.socket().getLocalPort();
  }

  @Override protected void closeChannel() throws IOException {
    datagramChannel.close();
    datagramChannel.socket().close();
  }

  public Protocol protocol() {
    return Protocol.Udp;
  }

  public int writeQueueSize() {
    return callback.writeQueueSize();
  }

  @Override void processSelectionKey(final SelectionKey selectionKey) throws IOException {
    if (selectionKey.isReadable()) {
      callback.read();
    }
    if (selectionKey.isWritable()) {
      callback.flushWriteQueue();
    }
  }

  @Override public String toString() {
    return "UdpServerChannel" + "{callback=" + callback + ",datagramChannel=" + datagramChannel + ",key=" + key +
           ",super=" + super.toString() + '}';
  }
}
