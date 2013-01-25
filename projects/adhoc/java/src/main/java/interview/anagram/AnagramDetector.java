package interview.anagram;

import java.util.regex.*;

public class AnagramDetector {

  private static final Pattern NON_ALPHABET = Pattern.compile("[^A-Za-z]");

  public static boolean areAnagrams(String s1, String s2) {
    return censusFor(s1).equals(censusFor(s2));
  }

  private static CharacterCensus censusFor(String s) {
    return new CharacterCensus().add(stripNonAlphabet(s).toLowerCase());
  }

  private static String stripNonAlphabet(String s) {
    return NON_ALPHABET.matcher(s == null ? "" : s).replaceAll("");
  }
}
