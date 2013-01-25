package mangotiger.text;

/**
 * A String helper class.
 * @author tom_gagnier@yahoo.com
 */
public class Strings {
  private Strings() {
    // intentionally empty
  }

  /**
   * Truncate a string to a maximum length.
   * @param string    the string to truncate
   * @param maxLength the maximum length of the string
   * @return the original string or substring if it is too long
   */
  public static String truncate(final String string, final int maxLength) {
    return string.length() <= maxLength ? string : string.substring(0, maxLength);
  }

  /**
   * The first word of a string delimited by a character.
   * @param s         the string to parse.
   * @param delimiter the delimiter separating the string pieces
   * @return The first word of a character delimited string.
   */
  public static String first(final String s, final char delimiter) {
    final int index = s.indexOf(delimiter);
    return index == -1 ? s : s.substring(0, index);
  }

  /**
   * The first word of a string delimited by '.'.
   * @param s the string to parse.
   * @return The first word of a character delimited by '.'
   */
  public static String first(final String s) {
    return first(s, '.');
  }

  /**
   * The last word of a string delimited a character.
   * @param s         the string to parse.
   * @param delimiter the delimiter separating the string pieces
   * @return The last word of a character delimited string.
   */
  public static String last(final String s, final char delimiter) {
    final int beginIndex = s.lastIndexOf(delimiter) + 1;
    return s.substring(beginIndex);
  }

  /**
   * The last word of a string delimited a period ('.') character.
   * @param string the string to parse.
   * @return The last word of a character delimited string.
   */
  public static String last(String string) {
    return last(string, '.');
  }

  /**
   * Convert the leading character of a string to lower case.
   */
  public static String toLeadingLowerCase(String s) {
    if (s == null || s.length() == 0 || Character.isLowerCase(s.charAt(0))) {
      return s;
    }
    final StringBuffer buffer = new StringBuffer(s);
    buffer.setCharAt(0, Character.toLowerCase(s.charAt(0)));
    return buffer.toString();
  }

  /**
   * Convert the leading character of a string to upper case.
   */
  public static String toLeadingUpperCase(String s) {
    if (s == null || s.length() == 0 || Character.isUpperCase(s.charAt(0))) {
      return s;
    }
    final StringBuffer buffer = new StringBuffer(s);
    buffer.setCharAt(0, Character.toUpperCase(s.charAt(0)));
    return buffer.toString();
  }

  /**
   * @return null if the string is null or comprised of whitespace, otherwise the string itself.
   */
  public static String toNullIfWhitespace(String s) {
    return s == null || s.matches("\\s*") ? null : s;
  }

  /**
   * When column names are x_xxxxx_xxxx with the first letter being a single char, it means our attribute is
   * named xXxxxxXxxx. Middlegen then generates getters and setters of the form
   * XxxxxxXxxx. If we fix that in middlegen's preference file, Hibernate fails to see
   * the getters/setters (explaining why middlegen did that in the first place). So, here, we're going to make
   * sure that the pattern is Xx for the first two chars.
   */
  public static String capitalize(final String columnName) {
    return columnName.substring(0, 1).toUpperCase() + columnName.substring(1, 2).toLowerCase() + columnName.substring(2);
  }
}
