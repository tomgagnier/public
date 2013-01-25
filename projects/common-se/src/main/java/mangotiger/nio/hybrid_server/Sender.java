package mangotiger.nio.hybrid_server;

import java.net.SocketAddress;
import java.nio.ByteBuffer;

import mangotiger.nio.hybrid_server.Protocol;

/**
 * A message sender.
 * @author tom_gagnier@yahoo.com
 */
public interface Sender {
  /**
   * Send a message to a specified target.
   * @param message the message to notify.
   * @param target  the target address.
   */
  void send(ByteBuffer message, SocketAddress target);

  /**
   * Queue a message to a specified target.
   * @param message the message to notify.
   * @param target  the target address.
   */
  void queue(ByteBuffer message, SocketAddress target);

  /**
   * The protocol used to sent the message.
   * @return The protocol used to sent the message.
   */
  Protocol protocol();
}
