/*
 * $Source: $
 *
 * Copyright (C) 2002-2005, Thomas Robert Gagnier, Jr., All Rights Reserved.
 * Unauthorized use, disclosure or reproduction of this source code is strictly
 * prohibited by United States copyright law and international treaty provisions.
 * Use of source code requires an appropriate source license.
 */
package mangotiger.lang;

import java.util.Arrays;
import java.util.List;

/**
 * A String utility class.
 * @author Tom Gagnier
 */
public final class Strings {
  public static final String[] EMPTY_ARRAY = new String[0];

  /** Like {@link String#split(String)}, except an empty list is returned if the trimmed string is null or empty. */
  public static List<String> split(final String string, final String regex) {
    final String trimmedString = string == null ? "" : string.trim();
    return Arrays.asList(trimmedString.length() == 0 ? EMPTY_ARRAY : trimmedString.split(regex));
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
   * The first word of a string delimited by '.'.
   * @param s the string to parse.
   * @return The first word of a character delimited by '.'
   */
  public static String first(final String s) {
    return first(s, '.');
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
   * The last word of a string delimited a period ('.') character.
   * @param s the string to parse.
   * @return The last word of a character delimited string.
   */
  public static String last(final String s) {
    return last(s, '.');
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

  /** Convert the leading character of a string to lower case. */
  public static String decapitalize(final String s) {
    if (s == null || s.length() == 0 || Character.isLowerCase(s.charAt(0))) {
      return s;
    }
    final StringBuffer buffer = new StringBuffer(s);
    buffer.setCharAt(0, Character.toLowerCase(s.charAt(0)));
    return buffer.toString();
  }

  /** Convert the leading character of a string to upper case. */
  public static String capitalize(final String s) {
    if (s == null || s.length() == 0 || Character.isUpperCase(s.charAt(0))) {
      return s;
    }
    final StringBuffer buffer = new StringBuffer(s);
    buffer.setCharAt(0, Character.toUpperCase(s.charAt(0)));
    return buffer.toString();
  }

  private Strings() {
    // Intentionally empty
  }
}
