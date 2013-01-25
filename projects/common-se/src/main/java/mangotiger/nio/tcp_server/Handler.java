package mangotiger.nio.tcp_server;

/**
 * Routes messages to handlers.
 *
 * @author tom_gagnier@yahoo.com
 */
public interface Handler {

  /**
   * Handle an input message.
   *
   * @param session a client proxy.
   * @param message a new message to handle.
   */
  void handle(Session session, String message);

  /**
   * Accept the client.
   *
   * @param session the client under consideration.
   *
   * @return true if the session is accepted.
   */
  boolean accept(Session session);
}
