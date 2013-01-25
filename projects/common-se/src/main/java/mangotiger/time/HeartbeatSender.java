package mangotiger.time;

/**
 * A heartbeat signal.
 */
public interface HeartbeatSender {
  /**
   * Return true if heartbeat sent.
   *
   * @return true if heartbeat sent.
   */
  boolean pulse();
}
