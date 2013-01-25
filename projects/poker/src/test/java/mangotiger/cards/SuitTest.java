/*
 * $Source: $
 *
 * Copyright (C) 2002-2005, Thomas Robert Gagnier, Jr., All Rights Reserved.
 * Unauthorized use, disclosure or reproduction of this source code is strictly
 * prohibited by United States copyright law and international treaty provisions.
 * Use of source code requires an appropriate source license.
 */
package mangotiger.cards;

import junit.framework.TestCase;

/** @author Tom Gagnier */
public final class SuitTest extends TestCase {
  public static void testInstanceFromString() {
    assertTrue(Suit.Clubs == Suit.instance("8c"));
    assertTrue(Suit.Clubs == Suit.instance("8C"));
    assertTrue(Suit.Diamonds == Suit.instance("8d"));
    assertTrue(Suit.Diamonds == Suit.instance("8D"));
    assertTrue(Suit.Hearts == Suit.instance("8h"));
    assertTrue(Suit.Hearts == Suit.instance("8H"));
    assertTrue(Suit.Spades == Suit.instance("8s"));
    assertTrue(Suit.Spades == Suit.instance("8S"));
    try {
      Suit.instance("");
      fail("expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertEquals("no Suit found in ''", e.getMessage());
    }
  }

  public static void testToChar() {
    assertEquals('S', Suit.Spades.toChar());
    assertEquals('H', Suit.Hearts.toChar());
    assertEquals('D', Suit.Diamonds.toChar());
    assertEquals('C', Suit.Clubs.toChar());
  }
}