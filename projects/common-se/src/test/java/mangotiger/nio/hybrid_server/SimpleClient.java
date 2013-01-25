package mangotiger.nio.hybrid_server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SocketChannel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A Blocking Test Client used for writing and reading text to a PNE Server.
 * @author tom_gagnier@yahoo.com
 */
final class SimpleClient {
  private static final Log LOG = LogFactory.getLog(SimpleClient.class);
  private static final int BUFFER_SIZE = 128;
  private final Channel tcp;
  private final Channel udp;
  private final InetSocketAddress hostAddress;

  SimpleClient(final InetSocketAddress hostAddress) throws IOException {
    LOG.info(hostAddress);
    this.hostAddress = hostAddress;
    tcp = new Tcp();
    udp = new Udp();
  }

  Channel tcp() {
    return tcp;
  }

  Channel udp() {
    return udp;
  }

  static abstract class Channel {
    final ByteBuffer write = ByteBuffer.allocate(BUFFER_SIZE);
    final ByteBuffer read = ByteBuffer.allocate(BUFFER_SIZE);

    final void write(final String message) throws IOException {
      write.clear();
      write.put(message.getBytes());
      write.flip();
      write(write);
    }

    final String read() throws IOException {
      read.clear();
      read(read);
      read.flip();
      final String result = new String(read.array(), 0, read.limit());
      return result;
    }

    abstract void write(ByteBuffer buffer) throws IOException;

    abstract void read(ByteBuffer buffer) throws IOException;
  }

  /** The TCP Channel. */
  final class Tcp extends Channel {
    private final SocketChannel socketChannel;

    Tcp() throws IOException {
      socketChannel = SocketChannel.open(hostAddress);
      LOG.info("connected=" + socketChannel.isConnected());
      LOG.info(socketChannel);
    }

    @Override void write(final ByteBuffer buffer) throws IOException {
      while (buffer.remaining() > 0) {
        socketChannel.write(buffer);
      }
    }

    @Override void read(final ByteBuffer buffer) throws IOException {
      socketChannel.read(buffer);
    }
  }

  /** The UDP Channel. */
  final class Udp extends Channel {
    private final DatagramChannel datagramChannel;

    Udp() throws IOException {
      datagramChannel = DatagramChannel.open();
    }

    @Override void write(final ByteBuffer buffer) throws IOException {
      datagramChannel.send(buffer, hostAddress);
    }

    @Override void read(final ByteBuffer buffer) throws IOException {
      datagramChannel.receive(buffer);
    }
  }
}
