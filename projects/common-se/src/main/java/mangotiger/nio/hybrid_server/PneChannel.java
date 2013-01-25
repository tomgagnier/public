package mangotiger.nio.hybrid_server;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * A PNE Channel represents a protocol independent socket connection.
 * @author tom_gagnier@yahoo.com
 */
interface PneChannel {
  int TIMEOUT_MILLISECONDS = 1000;

  /**
   * The Local Internet Socket Address for the TCP and UDP channels.
   * @return The Internet Socket Address for the TCP and UDP channels.
   */
  InetSocketAddress getLocalAddress();

  /** Close the channel. */
  void close() throws IOException;

  /** Select the next set of ready keys. */
  void select() throws IOException;

  /**
   * The size of the write queue.
   * @return The size of the write queue.
   */
  int writeQueueSize();
}
