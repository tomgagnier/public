package mangotiger.time;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/** @author Tom Gagnier */
public class Times {

  public static final long MILLISECONDS_PER_SECOND = 1000L;
  public static final long MILLISECONDS_PER_MINUTE = MILLISECONDS_PER_SECOND * 60L;
  public static final long MILLISECONDS_PER_HOUR = MILLISECONDS_PER_MINUTE * 60L;
  public static final long MILLISECONDS_PER_DAY = MILLISECONDS_PER_HOUR * 24L;
  public static final long MILLISECONDS_PER_WEEK = MILLISECONDS_PER_DAY * 7L;
  private static final TimeZone TIME_ZONE = Calendar.getInstance().getTimeZone();

  private Times() {
    // intentionally empty
  }

  /** The time for midnight of the current day. */
  public static long midnight() {
    return midnight(new Date().getTime());
  }

  /** The time for midnight of the given time. */
  public static long midnight(final long time) {
    return time / MILLISECONDS_PER_DAY * MILLISECONDS_PER_DAY - TIME_ZONE.getOffset(time);
  }
}
