package mangotiger.nio.hybrid_server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import mangotiger.nio.hybrid_server.AbstractCallback;
import mangotiger.nio.hybrid_server.Router;
import mangotiger.nio.hybrid_server.Sender;
import mangotiger.nio.ByteBufferSource;
import mangotiger.nio.ByteBuffers;

/**
 * UDP Callback code shared by both Client and Server.
 * @author tom_gagnier@yahoo.com
 */
final class UdpCallback extends AbstractCallback {
  private static final Log LOG = LogFactory.getLog(UdpCallback.class);
  private final DatagramChannel datagramChannel;
  private InetSocketAddress remoteAddress;

  UdpCallback(final Sender sender, final ByteBufferSource pool, final Router router, final DatagramChannel datagramChannel) {
    super(sender, pool, router);
    this.datagramChannel = datagramChannel;
  }

  ByteBuffer write(final ByteBuffer buffer, final SocketAddress address) throws IOException {
    datagramChannel.send(buffer, address);
    buffer.rewind();
    return buffer;
  }

  @Override ByteBuffer write(final Object queuedObject) throws IOException {
    final QueuedWrite queuedWrite = (QueuedWrite)queuedObject;
    if (LOG.isDebugEnabled()) {
      LOG.debug("write" + queuedWrite);
    }
    return write(queuedWrite.buffer, queuedWrite.address);
  }

  @Override int readBuffer(final ByteBuffer buffer) throws IOException {
    buffer.clear();
    remoteAddress = (InetSocketAddress)datagramChannel.receive(buffer);
    return buffer.position();
  }

  public void queue(final ByteBuffer buffer, final SocketAddress address) {
    final ByteBuffer output = copy(buffer);
    final QueuedWrite queuedWrite = new QueuedWrite(output, address);
    if (LOG.isDebugEnabled()) {
      LOG.debug(ByteBuffers.describe(output, "output"));
      LOG.debug("queue" + queuedWrite);
    }
    queueObject(queuedWrite);
  }

  @Override InetSocketAddress getRemoteAddress() {
    return remoteAddress;
  }

  static final class QueuedWrite {
    final ByteBuffer buffer;
    final SocketAddress address;

    QueuedWrite(final ByteBuffer output, final SocketAddress address) {
      buffer = output;
      this.address = address;
      if (buffer == null || address == null) {
        throw new IllegalArgumentException("null arguments: " + toString());
      }
    }

    @Override public String toString() {
      return ByteBuffers.describe(buffer, address.toString());
    }
  }

  @Override public String toString() {
    return "UdpCallback" + "{datagramChannel=" + datagramChannel + ",remoteAddress=" + remoteAddress + '}';
  }
}
