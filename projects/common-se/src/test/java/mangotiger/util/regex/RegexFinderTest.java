package mangotiger.util.regex;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import junit.framework.TestCase;

/**
 * @author tom_gagnier@yahoo.com
 */
public class RegexFinderTest extends TestCase {

  RegexFinder regexFinder;
  private static final String ST_CRISPIN_TXT = "st-crispin.txt";
  private static final String NEWLINE = System.getProperty("line.separator");

  public void testInputStream() throws Exception {
    InputStream is = getClass().getResourceAsStream(ST_CRISPIN_TXT);
    regexFinder = new RegexFinder(is);
    regexFinder.match("\\s+And gentlemen.*day.");
    final List matches = regexFinder.matches();
    assertEquals(1, matches.size());
    final String expect = NEWLINE + "    And gentlemen in England now-a-bed" +
                          NEWLINE + "    Shall think themselves accurs'd they were not here," +
                          NEWLINE + "    And hold their manhoods cheap whiles any speaks" +
                          NEWLINE + "    That fought with us upon Saint Crispin's day.";
    final String actual = regexFinder.toString();
    assertEquals(expect, actual);
  }

  public void testFile() throws Exception {
    final File file = new File(getClass().getResource(ST_CRISPIN_TXT).getFile());
    regexFinder = new RegexFinder(file);
    regexFinder.match("[^\r\n]*brother[^\r\n]*");
    final List matches = regexFinder.matches();
    assertEquals(2, matches.size());
    final String expect = "    We few, we happy few, we band of brothers;\n" +
                          "    Shall be my brother; be he ne'er so vile,";
    final String actual = regexFinder.toString();
    assertEquals(expect, actual);
  }
}