package mangotiger.text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * The <code>Formats</code> interface defines a number of constant values used throughout the application tcp_server,
 * such as standard date and time values.
 * @author Zacharias Beckman
 */
public class Formats {

  public static final String TIME_FORMAT = "HH:mm";
  public static final String TIME_FORMAT12 = "h:mm a";

  /**
   * Standard Oracle date format string, used for converting between <code>timeDateFormatter</code> and the Oracle
   * <code>TO_DATE()</code> function.
   */
  public static final String oracleTimeDateFormat = "MM-DD-YYYY HH24:MI:SS";

  /**
   * Private constructor, used to prevent instantiation. This is a utility class and should not be instantiated. It's
   * members can be referenced directly, for example, <code>Formats.oracleTimeDateFormat</code>.
   */
  private Formats() {
    // prevent instantiation
  }

  /**
   * Standard date formatter for generating system times. This formatter should be used when generated any externalized
   * date value (for example, all XML generation methods should use this formatter). This formatter provides a long date
   * format based on <i>"EEEE yyyy-MM-dd hh:mm:ss z"</i> where "EEEE" is the full-English day of the week, "yyyy-MM-dd"
   * represents the year, month and day, "hh:mm:ss" the hour, minute and second, and "z" the time zone.
   */
  public static DateFormat standardDateFormatter() {
    return new SimpleDateFormat("EEEE yyyy-MM-dd hh:mm:ss z");
  }

  /**
   * Standard short-form date formatter, typically used in user interfaces. This formatter simply provides the date in
   * <i>"MM-DD-YYYY"</i> (month, day and year) format.
   */
  public static DateFormat shortDateFormatter() {
    return new SimpleDateFormat("MM-dd-yyyy");
  }

  /**
   * Simple short-form date formatter whose formats result in chronologically sorted times when sorted by alphanumeric
   * sort. The format is <i>"YYYY-MM-DD"</i>.
   */
  public static DateFormat alphaSortedDateFormatter() {
    return new SimpleDateFormat("yyyy-MM-dd");
  }

  /**
   * Standard date formatter with time, typically used in data store operations. This formatter provides the date in
   * <i>"DD-MM-YYYY HH:MM:SS"</i> (day, month, year, hour(24), minute, second) format. This format is ideal for
   * conversion to the data store, since it is accurate to the second whereas other formatters are not.
   */
  public static DateFormat timeDateFormatter() {
    return new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
  }

  /** Time only formatter. Includes only hours and minutes. Intended for use in the UI for appointments, etc. */
  public static DateFormat timeFormatter() {
    return new SimpleDateFormat(TIME_FORMAT);
  }

  public static DateFormat timeFormatter12() {
    return new SimpleDateFormat(TIME_FORMAT12);
  }

  /**
   * TIMESTAMP date formatter with time, typically used in data store operations. This formatter provides the date in
   * <i>"yyyy-dd-MM HH:mm:ss.SS"</i> format. This format is ideal for conversion to the data store, since it is accurate
   * to the milisecond whereas other formatters are not.
   */
  public static DateFormat timestampFormatter() {
    return new SimpleDateFormat("yyyy-dd-MM HH:mm:ss.SS");
  }

  /**
   * Standard short-form date formatter, typically used in user interfaces or when conveying date values to the
   * underlying database. This formatter simply provides the date in <i>"DD-MMM-YYYY"</i> (month, day and year) format,
   * where the month is abbreviated. For example, "09-February-2001."
   */
  public static DateFormat namedDateFormatter() {
    return new SimpleDateFormat("dd-MMM-yyyy");
  }

  /**
   * Alternate short-form date formatter, used exclusively in user interfaces This formatter simply provides the date in
   * <i>"September 5, 1967"</i> format (MMM d, yyyy).
   */
  public static DateFormat businessTextDateFormatter() {
    return new SimpleDateFormat("MMM dd, yyyy");
  }

  /**
   * Alternate short-form date formatter, intended for use in user interfaces This formatter provides the date in
   * <i>"September 5, 1967 at 15:32"</i> format.
   */
  public static DateFormat businessTextDateTimeFormatter() {
    return new SimpleDateFormat("MMM dd, yyyy 'at' hh:mm aaa");
  }
}

