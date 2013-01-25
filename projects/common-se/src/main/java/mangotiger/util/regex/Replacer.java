package mangotiger.util.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author tom_gagnier@yahoo.com
 */
public class Replacer {

  private final Pattern pattern;
  private final String replacement;

  public Replacer(String regex, String replacement) {
    pattern = Pattern.compile(regex);
    this.replacement = replacement;
  }

  /**
   * Replace the string if it matches the pattern or return the string.
   */
  public String execute(final String string) {
    final Matcher matcher = pattern.matcher(string);
    if (matcher.find()) {
      return matcher.replaceFirst(replacement);
    }
    return string;
  }

  public Pattern getPattern() {
    return pattern;
  }

  public String getReplacement() {
    return replacement;
  }

  

}
