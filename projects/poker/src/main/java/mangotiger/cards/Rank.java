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
public enum Rank {
  Two, Three, Four, Five, Six, Seven, Eight, Nine, Ten, Jack, Queen, King, Ace;
  private static final char[] chars = {'2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'A'};

  public static Rank instance(final String card) {
    for (int i = 0; i < card.length(); ++i) {
      switch (Character.toUpperCase(card.charAt(i))) {
      case '2':
        return Two;
      case '3':
        return Three;
      case '4':
        return Four;
      case '5':
        return Five;
      case '6':
        return Six;
      case '7':
        return Seven;
      case '8':
        return Eight;
      case '9':
        return Nine;
      case 'T':
        return Ten;
      case 'J':
        return Jack;
      case 'Q':
        return Queen;
      case 'K':
        return King;
      case 'A':
        return Ace;
      default:
      }
    }
    throw new IllegalArgumentException("no Rank found in '" + card + '\'');
  }

  public char toChar() {
    return chars[ordinal()];
  }
}
