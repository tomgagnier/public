package mangotiger.time;

/**
 * An alarm clock.
 * @author tom_gagnier@yahoo.com
 */
public final class Alarm {
  private final long snoozeIntervalMillis;
  private long alarmMillis;

  /**
   * Construct a new alarm clock. <p/> All times use the same frame of references as System.curretnTimeMilliseconds().
   * @param snoozeIntervalMillis the time to be quiet, in milliseconds.
   * @param alarmMillis          time the alarm expires.
   */
  public Alarm(final long snoozeIntervalMillis, final long alarmMillis) {
    this.snoozeIntervalMillis = snoozeIntervalMillis;
    this.alarmMillis = alarmMillis;
  }

  /**
   * Construct a new alarm clock.
   *
   * @param snoozeIntervalMilliseconds the time to be quiet, in milliseconds.
   */
  /**
   * Construct a new alarm clock.
   * @param snoozeIntervalMilliseconds the time to be quiet, in milliseconds.
   */
  public Alarm(final long snoozeIntervalMilliseconds) {
    snoozeIntervalMillis = snoozeIntervalMilliseconds;
    alarmMillis = System.currentTimeMillis() + snoozeIntervalMilliseconds;
  }

  /**
   * Determine if the alarm is ringing.
   * @return true if the alarm is ringing.
   */
  public boolean isRinging() {
    return alarmMillis <= System.currentTimeMillis();
  }

  /** Turn off the alarm. */
  public void stop() {
    alarmMillis = Long.MAX_VALUE;
  }

  /** Reset the alarm, using the current snooze interval. */
  public void snooze() {
    alarmMillis = System.currentTimeMillis() + snoozeIntervalMillis;
  }

  /**
   * Return true if the alarm is not ringing.
   * @return true if the alarm is not ringing.
   */
  public boolean isSilent() {
    return !isRinging();
  }

  @Override public String toString() {
    return "Alarm{snoozeIntervalMillis=" + snoozeIntervalMillis + ",alarmMillis=" + alarmMillis + '}';
  }
}
