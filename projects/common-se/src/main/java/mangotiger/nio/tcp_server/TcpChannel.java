package mangotiger.nio.tcp_server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A channel using the TCP protocol.
 *
 * @author tom_gagnier@yahoo.com
 */
public final class TcpChannel implements Channel {

  private static final Log logger = LogFactory.getLog(TcpChannel.class);
  private static final int READ_WRITE = SelectionKey.OP_READ | SelectionKey.OP_WRITE;
  private static final long TIMEOUT_MILLISECONDS = 1000L;
  private static final int MINIMUM_PORT = 0x400;
  private static final int MAXIMIM_PORT = 0xFFFF;
  /**
   * The delimiter used to mark the end of a message.
   */
  public static final String DELIMITER = "\n";

  private final int bufferSize;
  private final ServerSocketChannel serverSocketChannel;
  private final Handler handler;
  private final Selector selector;
  private final Map sessions = new HashMap();
  private final InetSocketAddress localAddress;

  /**
   * A TCP Server Channel.
   *
   * @param bufferSize
   * @param handler
   * @param port
   *
   * @throws IOException
   */
  public TcpChannel(final Handler handler, final int port, final int bufferSize) throws IOException {
    this.handler = handler;
    this.bufferSize = bufferSize;
    selector = Selector.open();
    serverSocketChannel = ServerSocketChannel.open();
    final ServerSocket serverSocket = serverSocketChannel.socket();
    if (MINIMUM_PORT <= port && port <= MAXIMIM_PORT) {
      serverSocket.bind(new InetSocketAddress(port));
    } else {
      serverSocket.bind(null);
    }
    serverSocketChannel.configureBlocking(false);
    serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    localAddress = new InetSocketAddress(InetAddress.getLocalHost(), serverSocket.getLocalPort());
  }

  /**
   * A TCP Server Channel that randomly selects a port.  Used for testing only.
   *
   * @param handler
   * @param bufferSize
   *
   * @throws IOException
   */
  public TcpChannel(final Handler handler, final int bufferSize) throws IOException {
    this(handler, -1, bufferSize);
  }

  public Iterator sessions() {
    return sessions.values().iterator();
  }

  public Session getSession(final InetSocketAddress address) {
    return (Session) sessions.get(address);
  }

  public InetSocketAddress getLocalAddress() {
    return localAddress;
  }

  /**
   * Select the next group of ready keys.
   *
   * @throws IOException
   */
  public void select() throws IOException {
    final int numberOfKeys = selector.select(TIMEOUT_MILLISECONDS);
    if (numberOfKeys == 0) {
      return;
    }
    for (Iterator i = selector.selectedKeys().iterator(); i.hasNext();) {
      final SelectionKey key = (SelectionKey) i.next();
      i.remove(); // required to prevent this key from being selected again
      process(key);
    }
  }

  private void process(final SelectionKey key) {
    TcpSession session = null;
    try {
      if (key.isAcceptable()) {
        final SocketChannel socketChannel = ((ServerSocketChannel) key.channel()).accept();
        logger.debug(socketChannel);
        socketChannel.configureBlocking(false);
        session = new TcpSession(socketChannel, handler, bufferSize);
        if (!handler.accept(session)) {
          return;
        }
        sessions.put(session.getAddress(), session);
        final SelectionKey selectionKey = socketChannel.register(selector, READ_WRITE);
        selectionKey.attach(session);
      } else {
        session = (TcpSession) key.attachment();
      }
      if (key.isReadable()) {
        session.read();
      }
    } catch (ClosedChannelException e) {
      cancel(session, key);
    } catch (IOException e) {
      logger.warn(e, e);
      cancel(session, key);
    }
  }

  private void cancel(final TcpSession session, final SelectionKey key) {
    sessions.remove(session);
    key.cancel();
  }

  public void close() throws IOException {
    selector.close();
    serverSocketChannel.socket().close();
    serverSocketChannel.close();
  }

  public String toString() {
    return getLocalAddress().toString();
  }
}
