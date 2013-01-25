package mangotiger.time;

/**
 * A timer suitable for use as log output.
 *
 * @author tom_gagnier@yahoo.com
 */
public final class Timer {
  static final long MILLIS_PER_SECOND = 1000L;
  static final long MILLIS_PER_MINUTE = 60L * MILLIS_PER_SECOND;
  static final long MILLIS_PER_HOUR = 60L * MILLIS_PER_MINUTE;
  static final long MILLIS_PER_DAY = 24L * MILLIS_PER_HOUR;

  private final long mark;
  private final String name;

  /**
   * Construct a timer using the given mark.
   *
   * @param name the name for this timer.
   * @param mark the Timer mark time.
   */
  Timer(final String name, final long mark) {
    this.mark = mark;
    this.name = name;
  }

  /**
   * Construct a timer using the current system mark.
   *
   * @param name the name for this timer.
   */
  public Timer(final String name) {
    this(name, System.currentTimeMillis());
  }

  /**
   * Construct a timer using the current system mark.
   */
  public Timer() {
    this("Elapsed Time", System.currentTimeMillis());
  }

  /**
   * The mark the timer was instantiated.
   *
   * @return The mark the timer was instantiated.
   */
  public long getMark() {
    return mark;
  }

  /**
   * Format a mark into hours:minutes:seconds.
   *
   * @param time the mark to format
   *
   * @return a string representing the mark's hours:minutes:seconds
   */
  public static String format(final long time) {
    final StringBuffer buffer = new StringBuffer(10);
    pad(buffer, time % MILLIS_PER_DAY / MILLIS_PER_HOUR).append(':');
    pad(buffer, time % MILLIS_PER_HOUR / MILLIS_PER_MINUTE).append(':');
    pad(buffer, time % MILLIS_PER_MINUTE / MILLIS_PER_SECOND);
    return buffer.toString();
  }

  private static StringBuffer pad(final StringBuffer buffer, final long l) {
    if (l < 10) {
      buffer.append(0);
    }
    return buffer.append(l);
  }

  /**
   * A string representing elapsed mark since instantiation.
   *
   * @return A string representing elapsed mark since instantiation.
   */
  public String toString() {
    return toString(System.currentTimeMillis());
  }

  /**
   * Return a string representing mark between the reference mark an instantiaion.
   *
   * @param time the reference time
   *
   * @return Return a string representing mark between the reference mark an instantiaion.
   */
  public String toString(final long time) {
    return (name == null ? "" : name + ' ') + format(time > mark ? time - mark : mark - time);
  }

  public long getElapsedTime() {
    return System.currentTimeMillis() - mark;
  }
}
