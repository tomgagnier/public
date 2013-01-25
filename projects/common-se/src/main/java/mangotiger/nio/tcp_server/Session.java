package mangotiger.nio.tcp_server;

import java.net.InetSocketAddress;

/**
 * A session.
 *
 * @author tom_gagnier@yahoo.com
 */
public interface Session {

  /**
   * The remote socket address of this session.
   *
   * @return The remote socket address of this session.
   */
  InetSocketAddress getAddress();

  /**
   * Send a message to the client.
   *
   * @param message the message to notify.
   */
  void send(String message);

  /**
   * Get the value from the session context.
   *
   * @param key the value's key
   *
   * @return the value from the session context
   */
  Object get(String key);

  /**
   * Put the value in the session context.
   *
   * @param key   the value's key
   * @param value the value
   */
  void put(String key, Object value);

  /**
   * Notify the session it is no longer connected.
   */
  void close();
}
