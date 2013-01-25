package mangotiger.topcoder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Lines of text.
 * @author tom_gagnier@yahoo.com
 */
final class Lines {

  private static final String PRINTABLE_CHAR = "^.*[A-Za-z0-9~`!@#$%^&*()_\\\\\\-+={\\[\\]}:;\"'?/,.].*";
  private static final int MAXIMUM_COLUMN_WIDTH = 70;
  private static final String DASH = "-";

  private final List<String> lines;
  private int index;
  private static final int CHARACTERS_PER_LINE = 40;

  Lines(final List<String> lines) {
    this.lines = new ArrayList<String>(lines);
  }

  Lines(final File input) throws IOException {
    lines = new LinkedList<String>();
    final BufferedReader in = new BufferedReader(new FileReader(input));
    for (String line = in.readLine(); line != null; line = in.readLine()) {
      final String candidate = line.trim();
      if (acceptLine(candidate)) {
        lines.add(candidate);
      }
    }
  }

  private static boolean acceptLine(final String line) {
    return line.length() > 0 && !DASH.equals(line) && line.matches(PRINTABLE_CHAR);
  }

  public void setIndex(final int index) {
    this.index = index % lines.size();
  }

  public String get() {
    return get(index);
  }

  public String get(final int i) {
    return i < lines.size() ? (String)lines.get(i) : null;
  }

  public int find(final String regex) {
    for (String line = get(); line != null; line = next()) {
      if (line.matches(regex)) {
        return index;
      }
    }
    return index;
  }

  public Lines remain() {
    return between(index + 1, end());
  }

  public Lines between(final int begin, final int end) {
    return new Lines(lines.subList(begin, end));
  }

  public Lines between(final int begin, final String regex) {
    final int end = find(regex);
    final List<String> between = lines.subList(begin, end);
    return new Lines(between);
  }

  public String after(final String regex) {
    return get(find(regex) + 1);
  }

  public int end() {
    return lines.size();
  }

  public int index() {
    return index;
  }

  public void print(final PrintStream out) {
    print(out, "");
  }

  public void print(final PrintStream out, final String tab) {
    final String lead = tab + " * ";
    for (int i = 0; i < lines.size(); ++i) {
      final String line = lines.get(i);
      if (line.length() > 0 && !DASH.equals(line)) {
        if (i != 0) {
          out.println(lead);
        }
        out.print(lead);
        print(line, out, lead);
        out.println();
      }
    }
  }

  private static void print(final String s, final PrintStream out, final String lead) {
    int p = 0;
    for (int j = 0; j < s.length(); ++j) {
      final char c = s.charAt(j);
      if (++p < MAXIMUM_COLUMN_WIDTH || !Character.isWhitespace(c)) {
        out.print(c);
      } else {
        p = 0;
        out.println();
        out.print(lead);
      }
    }
  }

  public String next() {
    return ++index == end() ? null : get();
  }

  @Override public String toString() {
    final StringBuffer buf = new StringBuffer(lines.size() * CHARACTERS_PER_LINE);
    buf.append(getClass().getName()).append("{index=").append(index).append('}');
    final int maxNumberLength = Integer.toString(lines.size() - 1).length();
    for (int i = 0; i < lines.size(); ++i) {
      buf.append("\n\t").append(leftJustify(i, maxNumberLength));
      buf.append(i).append(") ").append(lines.get(i));
    }
    return buf.toString();
  }

  private static String leftJustify(final int integer, final int width) {
    final StringBuffer buffer = new StringBuffer(width).append(integer);
    while (buffer.length() < width) {
      buffer.insert(0, ' ');
    }
    return buffer.toString();
  }
}
