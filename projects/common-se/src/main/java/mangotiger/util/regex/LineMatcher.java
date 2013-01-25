package mangotiger.util.regex;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mangotiger.lang.Strings;

/**
 * A simple line matcher.  It is built from a name, a map of attribute names to group indices and a regular expression.
 * When give a line to match, it return true or false.  If true, a map attribute name to value is created.
 * @author Tom Gagnier
 */
public final class LineMatcher implements Comparable {
  private final String name;
  private final Pattern pattern;
  private final Map<String, Integer> groupIndicesByAttribute;
  private final Map<String, String> attributes = new HashMap<String, String>();

  /**
   * An bus line matcher. Group numbers can be skipped if attribute is mapped to the next available group number.  In
   * other words, "1 one 2 two 3 three" is equivalent to "one two three".
   * @param eventName           the name of the matcher
   * @param groupAttributePairs a space delimited list of ordered group number and attribute names pairs
   * @param regex               the regular expression used to match line candidates
   */
  public LineMatcher(final String eventName, final String groupAttributePairs, final String regex) {
    name = eventName;
    pattern = Pattern.compile(regex);
    groupIndicesByAttribute = toGroupIndicesByAttribute(groupAttributePairs);
  }

  /** Convert a string of attribute names and group indices to a map. */
  private static Map<String, Integer> toGroupIndicesByAttribute(final String attributes) {
    final Map<String, Integer> groupsByAttribute = new TreeMap<String, Integer>();
    int index = 1;
    for (String string : Strings.split(attributes, " +")) {
      if (string.matches("[0-9]+")) {
        index = Integer.parseInt(string);
      } else {
        groupsByAttribute.put(string, index++);
      }
    }
    return groupsByAttribute;
  }

  public String name() {
    return name;
  }

  /** The attribute name to value map.  Empty after a false match. */
  public Map<String, String> attributes() {
    return new TreeMap<String, String>(attributes);
  }

  /**
   * Attempts to match the line.  If matched, then an attribute map of names to values is created.
   * @return true if matched, else false
   */
  public boolean matches(final String line) {
    attributes.clear();
    final Matcher matcher = pattern.matcher(line);
    if (!matcher.matches()) return false;
    final int groupCount = matcher.groupCount();
    for (String attribute : groupIndicesByAttribute.keySet()) {
      final int index = groupIndicesByAttribute.get(attribute);
      if (index <= groupCount) {
        final String value = matcher.group(index);
        attributes.put(attribute, value == null ? "" : value.trim());
      }
    }
    return true;
  }

  @Override public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    final LineMatcher lineMatcher = (LineMatcher)o;
    if (!groupIndicesByAttribute.equals(lineMatcher.groupIndicesByAttribute)) return false;
    if (!name.equals(lineMatcher.name)) return false;
    return pattern.equals(lineMatcher.pattern);
  }

  @Override public int hashCode() {
    int result = name.hashCode();
    result = 29 * result + pattern.hashCode();
    result = 29 * result + groupIndicesByAttribute.hashCode();
    return result;
  }

  public int compareTo(final Object o) {
    final LineMatcher that = (LineMatcher)o;
    return toString().compareTo(that.toString());
  }

  @Override public String toString() {
    return "LineMatcher{" + name + groupIndicesByAttribute + ", pattern=" + pattern.pattern() + '}';
  }
}