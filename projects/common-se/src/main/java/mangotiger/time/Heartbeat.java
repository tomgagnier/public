package mangotiger.time;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A simple class that sends a heartbeat on its own thread.
 *
 * @author tom_gagnier@yahoo.com
 */
public final class Heartbeat implements Runnable {

  private static final Log logger = LogFactory.getLog(Heartbeat.class);
  private final long intervalMillis;
  private final HeartbeatSender sender;
  private boolean halting;
  private boolean running;

  /**
   * Construct a heartbeat from a signal and an interval.
   *
   * @param sender         the signal sent by the heartbeat
   * @param intervalMillis the time between signals
   */
  public Heartbeat(final HeartbeatSender sender, final long intervalMillis) {
    this.sender = sender;
    this.intervalMillis = intervalMillis;
  }

  /**
   * Determine if the heartbeat running.
   *
   * @return true if the heartbeat is running
   */
  public boolean isRunning() {
    return running;
  }

  public void run() {
    try {
      halting = false;
      running = true;
      while (!halting && sender.pulse()) {
        Thread.sleep(intervalMillis);
      }
    } catch (InterruptedException e) {
      logger.error(e, e);
    } finally {
      running = false;
      halting = false;
    }
  }

  /**
   * Asynchronous call to stop the heartbeat.
   */
  public void halt() {
    halting = true;
  }

  public String toString() {
    return "Heartbeat" +
        "{intervalMillis=" + intervalMillis +
        ",signal=" + sender +
        ",halting=" + halting +
        ",running=" + running +
        '}';
  }

}
