package interview.anagram;

import java.util.*;

public class CharacterCensus {

  private final Map<Character, Integer> census = new HashMap<Character, Integer>();

  public CharacterCensus add(String s) {
    for (int i = 0; i < s.length(); ++i) {
      char c = s.charAt(i);
      census.put(c, countOf(c) + 1);
    }
    return this;
  }

  public int countOf(char c) {
    return census.containsKey(c) ? census.get(c) : 0;
  }

  public Map<Character, Integer> census() {
    return Collections.unmodifiableMap(census);
  }

  public boolean equals(Object object) {
    if (object instanceof CharacterCensus) {
      CharacterCensus that = (CharacterCensus) object;
      return this.census().equals(that.census());
    }
    return false;
  }

  public int hashCode() {
    return census().hashCode();
  }

  public String toString() {
    return getClass().getSimpleName() + orderedKeyValues();
  }

  private String orderedKeyValues() {
    StringBuilder builder = new StringBuilder();
    for (Character c : new TreeSet<Character>(census.keySet()))
      builder.append(builder.length() == 0 ? '[' : ',').append(c).append('=').append(census().get(c));
    return builder.append(']').toString();
  }
}

