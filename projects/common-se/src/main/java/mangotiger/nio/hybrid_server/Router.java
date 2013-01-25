package mangotiger.nio.hybrid_server;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

/**
 * A protocol independent channel.
 * @author tom_gagnier@yahoo.com
 */
public interface Router {
  /**
   * Handle an input message.
   * @param sender        a sender used for direct responses.
   * @param message       a new message to handle.
   * @param sourceAddress the message source.
   */
  void route(Sender sender, ByteBuffer message, InetSocketAddress sourceAddress);
}
