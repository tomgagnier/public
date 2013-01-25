package mangotiger.nio.tcp_server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Iterator;

/**
 * A communication channel.
 *
 * @author tom_gagnier@yahoo.com
 */
public interface Channel {

  /**
   * The Local Internet Socket Address of the channel.
   *
   * @return The Local Internet Socket Address of the channel.
   */
  InetSocketAddress getLocalAddress();

  /**
   * Close the channel.
   *
   * @throws IOException
   */
  void close() throws IOException;

  /**
   * Select the next set of ready keys.
   *
   * @throws IOException
   */
  void select() throws IOException;

  /**
   * The active sessions connected to this channel.
   *
   * @param address the Internet socket address of the session.
   *
   * @return The active sessions connected to this channel.
   */
  Session getSession(InetSocketAddress address);

  /**
   * An iterator to all active sessions.
   *
   * @return An iterator to all active sessions.
   */
  Iterator sessions();
}
