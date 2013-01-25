package mangotiger.util;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import mangotiger.text.Formats;
import mangotiger.time.Times;
import static mangotiger.time.Times.MILLISECONDS_PER_DAY;
import static mangotiger.time.Times.MILLISECONDS_PER_MINUTE;

/** @author Tom Gagnier */
public class Dates {

  public static final int HOURS_PER_DAY = 24;

  private Dates() {
    // intentionally empty
  }

  public static Date midnight() {
    return new Date(Times.midnight());
  }

  public static Date datetime(final Date appearanceDate, final Date appearanceTime) {
    if (appearanceTime == null) return appearanceDate;
    if (appearanceDate == null) return appearanceTime;
    final long date = appearanceDate.getTime() / MILLISECONDS_PER_DAY * MILLISECONDS_PER_DAY;
    final long time = appearanceTime.getTime() % MILLISECONDS_PER_DAY;
    return new Date(date + time);
  }

  public static Date newDate(final String monthDayYear) {
    try {
      return Formats.shortDateFormatter().parse(monthDayYear.replace('/', '-'));
    } catch (ParseException e) {
      throw new IllegalArgumentException(e);
    }
  }

  public static Date newTime(final String twentyFour) {
    try {
      return Formats.timeFormatter().parse(twentyFour);
    } catch (ParseException e) {
      throw new IllegalArgumentException(e);
    }
  }

  public static Date newTime(final int minutesOffset) {
    final long offset = System.currentTimeMillis() + minutesOffset * MILLISECONDS_PER_MINUTE;
    return new Date(offset % MILLISECONDS_PER_DAY);
  }

  public static Date newDate(final int dayOffset) {
    return new Date(new Date().getTime() + dayOffset * MILLISECONDS_PER_DAY);
  }

  @SuppressWarnings({"MethodWithTooManyParameters"})
  public static Date newDate(final int year, final int month, final int day) {
    return newDate(year, month, day, 0, 0, 0, 0);
  }

  @SuppressWarnings({"MethodWithTooManyParameters"})
  public static Date newDate(final int year, final int month, final int day,
                             final int hour, final int minute, final int second) {
    return newDate(year, month, day, hour, minute, second, 0);
  }

  @SuppressWarnings({"MethodWithTooManyParameters"})
  public static Date newDate(final int year, final int month, final int day,
                             final int hour, final int minute, final int second,
                             final int millis) {
    final Calendar calendar = Calendar.getInstance();
    calendar.set(year, month - 1, day, hour, minute, second);
    calendar.set(Calendar.MILLISECOND, millis);
    return calendar.getTime();
  }
}
