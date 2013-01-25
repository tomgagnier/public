package mangotiger.time;

/**
 * @author tom_gagnier@yahoo.com
 */

import junit.framework.TestCase;

/** Test the Alarm. */
public final class AlarmTest extends TestCase {

  /** Simple test. */
  public void test() throws InterruptedException {
    final int snoozeIntervalMillis = 10;
    final long start = System.currentTimeMillis();
    final Alarm alarm = new Alarm(snoozeIntervalMillis, start);
    assertTrue(alarm.isRinging());
    alarm.snooze();
    assertFalse(alarm.isRinging());
    Thread.sleep(snoozeIntervalMillis * 3 / 2);
    assertTrue(alarm.isRinging());
  }
}