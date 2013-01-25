package mangotiger.text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A helper class used to convert between name types.
 * @author tom_gagnier@yahoo.com
 */
public final class Names {

  private Names() {
    // intentionally empty
  }

  /**
   * Turn a camel case name into a database name.
   * <p/>
   * Camel case uses case changes to separate names, database names use underscores instead.
   *
   * @param camelCaseName the camel case name to convert
   *
   * @return the database name
   */
  public static String databasifyName(final String camelCaseName) {
    // don't tempt fate, lower-case the first letter right now b4 the regex gets into the action
    String databaseName = camelCaseName.substring(0, 1).toLowerCase() + camelCaseName.substring(1);

    // this means, find me a lower case letter followed by an upper case letter and remember the upper case one
    final Pattern regex = Pattern.compile("[^A-Z]+([A-Z])");
    Matcher matcher = regex.matcher(databaseName);
    while (matcher.find()) {
      // there is (still) an upper case letter in this name (first and only backreference)
      final String capitalLetter = matcher.group(1);
      // DON'T use matcher.replaceFirst because it matched all the non-uppercase letters too
      databaseName = databaseName.replaceFirst(capitalLetter, '_' + capitalLetter.toLowerCase());
      // stick our new string back in the loop
      matcher = regex.matcher(databaseName);
    }
    return databaseName;
  }

  /**
   * Convert a database style name (i.e., names separated by underscores), return a camel case name.
   */
  public static String camelCaseName(final String databaseName) {
    // upper case the first letter
    String camelCasing = databaseName.substring(0, 1).toUpperCase() + databaseName.substring(1);
    // find me a letter of any case or a $ following an underscore
    final Pattern regex = Pattern.compile("(_+([A-Za-z$]))");
    Matcher matcher = regex.matcher(camelCasing);
    while (matcher.find()) {
      final String underscoreAndLetter = matcher.group(1);
      final String justLetter = matcher.group(2);
      if ("$".equals(justLetter)) {
        // remove just the _. the replaceFirst doesn't work b/c $ is a regex token
        camelCasing = camelCasing.substring(0, camelCasing.indexOf('$') - 1) +
                      camelCasing.substring(camelCasing.indexOf('$'));
      } else {
        camelCasing = camelCasing.replaceFirst(underscoreAndLetter, justLetter.toUpperCase());
      }
      // stick our new string back in the loop
      matcher = regex.matcher(camelCasing);
    }
    final int index = camelCasing.length() - 1;
    final char lastChar = camelCasing.charAt(index);
    if (lastChar == '_') camelCasing = camelCasing.substring(0, index);
    return camelCasing;
  }

  /**
   * Convert a camel case name into a display name.
   */
  public static String displayName(final String camelCaseName) {
    if (camelCaseName == null) return "";
    final String trimmedName = camelCaseName.trim();
    if (trimmedName.length() == 0) return "";
    final StringBuffer displayName = new StringBuffer(trimmedName.length() + 6);
    displayName.append(Character.toUpperCase(trimmedName.charAt(0)));
    for (int i = 1; i < trimmedName.length(); ++i) {
      final char c = trimmedName.charAt(i);
      if (Character.isUpperCase(c) && isNextToLowerCaseLetter(trimmedName, i)) {
        displayName.append(' ');
      }
      displayName.append(c);
    }
    return displayName.toString();
  }

  /**
   * @return true if previous or next character is lower case.
   */
  private static boolean isNextToLowerCaseLetter(final String s, final int index) {
    return isLowerCase(s, index - 1) || isLowerCase(s, index + 1);
  }

  /**
   * @return true if character at index is lower case, false if uppercase or out of bounds.
   */
  private static boolean isLowerCase(final String s, int index) {
    return 0 <= index && index < s.length() && Character.isLowerCase(s.charAt(index));
  }
}
