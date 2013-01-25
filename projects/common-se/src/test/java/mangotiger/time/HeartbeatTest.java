package mangotiger.time;

import junit.framework.TestCase;

/**
 * Test the @link Heartbeat.
 *
 * @author tom_gagnier@yahoo.com
 */
public final class HeartbeatTest extends TestCase {

  private static final long INTERVAL = 10L;
  private static final int MAX = 100;

  /**
   * Test a normal heartbeat.
   *
   * @throws InterruptedException
   */
  public void testHeartbeat() throws InterruptedException {
    final CountedHeartbeatSender signal = new CountedHeartbeatSender(MAX);
    Heartbeat heartbeat = new Heartbeat(signal, INTERVAL);
    new Thread(heartbeat, "beat").start();
    final long numberOfIntervals = 5L;
    Thread.sleep(numberOfIntervals * INTERVAL);
//    assertTrue(heartbeat.isRunning());
    heartbeat.halt();
    Thread.sleep(2L * INTERVAL);
//    assertFalse(heartbeat.isRunning());
//    final int save = signal.count;Thread.sleep(numberOfIntervals * INTERVAL);
//    assertEquals(save, signal.count);
  }

  /**
   * Test a bad heartbeat.
   *
   * @throws InterruptedException
   */
  public void testBadHeartbeat() throws InterruptedException {
    final int max = 4;
    final CountedHeartbeatSender signal = new CountedHeartbeatSender(max);
    Heartbeat heartbeat = new Heartbeat(signal, INTERVAL);
    new Thread(heartbeat, "beat").start();
    Thread.sleep(2L * max * INTERVAL);
//    assertFalse(heartbeat.isRunning());
//    assertTrue(signal.count == max);
  }

  static final class CountedHeartbeatSender implements HeartbeatSender {
    final int max;
    int count = 0;

    CountedHeartbeatSender(final int max) {
      this.max = max;
    }

    public boolean pulse() {
      ++count;
      return count < max;
    }
  }

}

