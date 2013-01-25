package mangotiger.util.regex;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mangotiger.io.InputStreams;

/**
 * Find regular expressions in an input string and record matches by position.
 * @author tom_gagnier@yahoo.com
 */
public final class RegexFinder {
  private final String input;
  private final SortedMap<Integer,String> matchesByPosition = new TreeMap<Integer, String>();

  /**
   * Create a regex finder using an input string.
   * @param input the string to search
   */
  public RegexFinder(final String input) {
    this.input = input;
  }

  /**
   * Create a regex finder using a file as input.
   * @param file the file to search.
   * @throws IOException
   */
  public RegexFinder(final File file) throws IOException {
    this(new FileInputStream(file));
  }

  /**
   * Create a regex finder using an input stream as input.
   * @param inputStream the inpust stream  to search.
   * @throws IOException
   */
  public RegexFinder(final InputStream inputStream) throws IOException {
    this(InputStreams.toString(inputStream));
  }

  /**
   * Find all matches of the regular expression and record them by start position. <p/> The pattern flags default to
   * Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL.
   * @param regex the regular expression to use for matching.
   */
  public Collection<String> match(final String regex) {
    return match(regex, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
  }

  /**
   * Find all matches of the regular expression and record them by start position.
   * @param regex the regular expression to use for matching.
   * @param flags the
   * @return a collection of results ordered by location.
   */
  public Collection<String> match(final String regex, final int flags) {
    final SortedMap<Integer,String> results = new TreeMap<Integer, String>();
    final Pattern pattern = Pattern.compile(regex, flags);
    final Matcher matcher = pattern.matcher(input);
    while (matcher.find()) {
      final String match = matcher.group();
      results.put(matcher.start(), match);
    }
    matchesByPosition.putAll(results);
    return results.values();
  }

  /**
   * A list of all matches found so far, ordered by position.
   * @return A list of all matches found so far, ordered by position.
   */
  public List<String> matches() {
    final List<String> list = new ArrayList<String>(matchesByPosition.size());
    for (Integer integer : matchesByPosition.keySet()) {
      list.add(matchesByPosition.get(integer));
    }
    return list;
  }

  public RegexFinder clear() {
    matchesByPosition.clear();
    return this;
  }

  @Override public String toString() {
    final StringBuffer buffer = new StringBuffer();
    for (Iterator<Integer> i = matchesByPosition.keySet().iterator(); i.hasNext();) {
      final Object key = i.next();
      buffer.append(matchesByPosition.get(key));
      if (i.hasNext()) {
        buffer.append('\n');
      }
    }
    return buffer.toString();
  }
}
