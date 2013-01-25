package mangotiger.nio.hybrid_server;

import java.io.IOException;

/**
 * The Callback responsibilities.
 * @author tom_gagnier@yahoo.com
 */
public interface Callback {
  /** Write to the selected channel. */
  void flushWriteQueue() throws IOException;

  /** Read from the selected channel. */
  void read() throws IOException;

  /**
   * The number of Queued Writes.
   * @return The number of Queued Writes.
   */
  int writeQueueSize();
}
