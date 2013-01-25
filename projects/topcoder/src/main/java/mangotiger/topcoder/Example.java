package mangotiger.topcoder;

import java.util.LinkedList;
import java.util.List;

/**
 * A TopCoder example solution.
 * @author tom_gagnier@yahoo.com
 */
final class Example {
  private final int number;
  private final Lines args;
  private final String expect;
  private final Lines comments;
  private static final String RETURNS = "^Returns:.*";
  private static final int RETURNS_LENGTH = 8;
  private static final String OPEN_BRACE = "{";
  private static final String CLOSE_BRACE = "}";
  private static final int TYPICAL_EXPECT_SIZE = 100;

  Example(final Lines lines) {
    number = Integer.parseInt(lines.get().split("\\)")[0]);
    args = new Lines(getValues(lines));
    expect = getExpect(lines);
    comments = lines.remain();
  }

  public int getNumber() {
    return number;
  }

  public Lines getArgs() {
    return args;
  }

  public String getExpect() {
    return expect;
  }

  public Lines getComments() {
    return comments;
  }

  private static List<String> getValues(final Lines lines) {
    final List<String> arguments = new LinkedList<String>();
    boolean brace = false;
    for (String line = lines.next(); argValue(line); line = lines.next()) {
      if (brace) {
        final StringBuilder buf = new StringBuilder().append(arguments.get(arguments.size() - 1)).append(line);
        arguments.set(arguments.size() - 1, buf.toString());
        brace = !line.endsWith(CLOSE_BRACE);
      } else {
        arguments.add(line);
        brace = insideBrace(line);
      }
    }
    return arguments;
  }

  private static boolean insideBrace(final String line) {
    return line.startsWith(OPEN_BRACE) && !line.endsWith(CLOSE_BRACE);
  }

  private static boolean argValue(final String line) {
    return line != null && !line.matches(RETURNS);
  }

  private static String getExpect(final Lines lines) {
    final StringBuffer buf = new StringBuffer(TYPICAL_EXPECT_SIZE);
    buf.append(lines.get().substring(RETURNS_LENGTH).trim());
    if (buf.length() == 0) {
      buf.append(lines.next());
    }
    if (startsWithBrace(buf)) {
      while (endsWithoutBrace(buf)) {
        buf.append(lines.next());
      }
    }
    return buf.toString();
  }

  private static boolean endsWithoutBrace(final StringBuffer buf) {
    return buf.charAt(buf.length() - 1) != CLOSE_BRACE.charAt(0);
  }

  private static boolean startsWithBrace(final StringBuffer buf) {
    return buf.charAt(0) == OPEN_BRACE.charAt(0);
  }

  @Override public String toString() {
    final StringBuffer buf = new StringBuffer(getClass().getName());
    buf.append("{number=").append(number);
    buf.append(",args=").append(args);
    buf.append(",expect=").append(expect);
    buf.append(",comments=").append(comments);
    buf.append('}');
    return buf.toString();
  }
}

