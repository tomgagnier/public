package mangotiger.nio.hybrid_server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

import mangotiger.nio.ByteBufferSource;

/**
 * A tcp_server that echo's the client's input.
 * @author tom_gagnier@yahoo.com
 */
@SuppressWarnings({"ClassWithoutToString"})
final class EchoServer extends Multiplexor {
  private final TcpServerChannel tcp;
  private final UdpServerChannel udp;
  private static final int BYTE_BUFFER_CAPACITY = 128;
  private static final int PORT = 4000;

  /** Construct the echo tcp_server. */
  EchoServer() throws IOException {
    super(BYTE_BUFFER_CAPACITY);
    final Router router = new Router() {
      public void route(final Sender sender, final ByteBuffer message, final InetSocketAddress sourceAddress) {
        message.flip();
        sender.send(message, sourceAddress);
        message.clear();
      }
    };
    final ByteBufferSource bufferSource = byteBufferSource();
    tcp = new TcpServerChannel(bufferSource, router, PORT);
    udp = new UdpServerChannel(bufferSource, router, PORT);
    add(tcp);
    add(udp);
  }

  /**
   * The tcp_server's TCP channel.
   * @return The tcp_server's TCP channel.
   */
  public ServerChannel tcp() {
    return tcp;
  }

  /**
   * The tcp_server's UDP channel.
   * @return The tcp_server's UDP channel.
   */
  public ServerChannel udp() {
    return udp;
  }
}
