package mangotiger.time;

import junit.framework.TestCase;

/**
 * Test the @link Timer.
 *
 * @author tom_gagnier@yahoo.com
 */
public final class TimerTest extends TestCase {

  private static final long TWELVE_FIFTEEN = toMillis(12L, 15L, 0L);

  /**
   * Test format().
   */
  public static void testFormat() {
    final String expected = "12:15:00";
    assertEquals(expected, Timer.format(TWELVE_FIFTEEN));
    assertEquals(expected, Timer.format(gmtMidnight() + TWELVE_FIFTEEN));
  }

  /**
   * Test toString().
   */
  public static void testToString() {
    final long start = gmtMidnight();
    final Timer timer = new Timer("test", start);
    final long delta = toMillis(6L, 5L, 4L);
    assertEquals("test 06:05:04", timer.toString(start + delta));
    assertEquals("test 06:05:04", timer.toString(start - delta));
  }

  private static long toMillis(final long hours, final long minutes, final long seconds) {
    return hours * Timer.MILLIS_PER_HOUR + minutes * Timer.MILLIS_PER_MINUTE + seconds * Timer.MILLIS_PER_SECOND;
  }

  private static long gmtMidnight() {
    return System.currentTimeMillis() / Timer.MILLIS_PER_DAY * Timer.MILLIS_PER_DAY;
  }

}