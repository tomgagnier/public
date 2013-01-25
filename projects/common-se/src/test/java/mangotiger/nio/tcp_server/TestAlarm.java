package mangotiger.nio.tcp_server;

import junit.framework.TestCase;
import mangotiger.time.Alarm;

/**
 * Test the @link Alarm class.
 * @author tom_gagnier@yahoo.com
 */
public final class TestAlarm extends TestCase {
  /**
   * Simple test.
   * @throws InterruptedException
   */
  public static void test() throws InterruptedException {
    final long snoozeIntervalMillis = 10L;
    final long start = System.currentTimeMillis();
    final Alarm alarm = new Alarm(snoozeIntervalMillis, start);
    assertTrue(alarm.isRinging());
    alarm.snooze();
    assertFalse(alarm.isRinging());
    Thread.sleep(snoozeIntervalMillis * 3L / 2L);
    assertTrue(alarm.isRinging());
  }
}