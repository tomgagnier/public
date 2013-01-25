/*
 * $Source: $
 *
 * Copyright (C) 2002-2005, Thomas Robert Gagnier, Jr., All Rights Reserved.
 * Unauthorized use, disclosure or reproduction of this source code is strictly
 * prohibited by United States copyright law and international treaty provisions.
 * Use of source code requires an appropriate source license.
 */
package mangotiger.cards;

/** @author Tom Gagnier */
public enum Suit {
  Clubs, Diamonds, Hearts, Spades;
  private static final char[] chars = {'C', 'D', 'H', 'S'};

  public static Suit instance(final String card) {
    for (int i = 0; i < card.length(); ++i) {
      switch (Character.toUpperCase(card.charAt(i))) {
      case 'S':
        return Spades;
      case 'H':
        return Hearts;
      case 'D':
        return Diamonds;
      case 'C':
        return Clubs;
      default:
      }
    }
    throw new IllegalArgumentException("no Suit found in '" + card + '\'');
  }

  public char toChar() {
    return chars[ordinal()];
  }
}
