package mangotiger.nio.tcp_server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ClosedSelectorException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * An abstract tcp_server.
 *
 * @author tom_gagnier@yahoo.com
 */
public abstract class AbstractServer implements Runnable, Handler {

  private static final Log logger = LogFactory.getLog(AbstractServer.class);

  private final Channel channel;
  private boolean active;

  /**
   * Construct an Abstract Server.
   *
   * @param port       the port to operate the tcp_server on
   * @param bufferSize the byte buffer capacity
   *
   * @throws IOException
   */
  protected AbstractServer(final int port, final int bufferSize) throws IOException {
    channel = new TcpChannel(this, port, bufferSize);
  }

  /**
   * Construct an Abstract Server.
   *
   * @param bufferSize the byte buffer capacity
   *
   * @throws IOException
   */
  protected AbstractServer(final int bufferSize) throws IOException {
    this(-1, bufferSize);
  }

  /**
   * The local address of the tcp_server.
   *
   * @return The local address of the tcp_server.
   */
  public final InetSocketAddress getLocalAddress() {
    return channel.getLocalAddress();
  }

  public final boolean isActive() {
    return active;
  }

  /**
   * Start the tcp_server.
   */
  public final void run() {
    logger.info("");
    logger.info("starting tcp_server on " + this);
    try {
      for (active = true; active;) {
        execute();
        channel.select();
      }
    } catch (ClosedSelectorException e) {
      logger.info(e);
    } catch (IOException e) {
      logger.error(e, e);
    }
  }

  /**
   * Override this method to execute logic during the select loop.
   */
  protected abstract void execute();

  /**
   * Halt the tcp_server.
   */
  public void close() {
    try {
      channel.close();
    } catch (IOException e) {
      logger.error("", e);
    }
  }

  public String toString() {
    return "Server" +
        "{channel=" + channel +
        '}';
  }

  /**
   * The communication channel used by the tcp_server.
   *
   * @return The communication channel used by the tcp_server.
   */
  protected final Channel getChannel() {
    return channel;
  }
}
