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
public final class RankTest extends TestCase {
  public static void testInstanceFromString() {
    assertTrue(Rank.Two == Rank.instance("C2"));
    assertTrue(Rank.Three == Rank.instance("C3"));
    assertTrue(Rank.Four == Rank.instance("C4"));
    assertTrue(Rank.Five == Rank.instance("C5"));
    assertTrue(Rank.Six == Rank.instance("C6"));
    assertTrue(Rank.Seven == Rank.instance("C7"));
    assertTrue(Rank.Eight == Rank.instance("C8"));
    assertTrue(Rank.Nine == Rank.instance("C9"));
    assertTrue(Rank.Ten == Rank.instance("Ct"));
    assertTrue(Rank.Jack == Rank.instance("Cj"));
    assertTrue(Rank.Queen == Rank.instance("Cq"));
    assertTrue(Rank.King == Rank.instance("Ck"));
    assertTrue(Rank.Ace == Rank.instance("Ca"));
    assertTrue(Rank.Ten == Rank.instance("CT"));
    assertTrue(Rank.Jack == Rank.instance("CJ"));
    assertTrue(Rank.Queen == Rank.instance("CQ"));
    assertTrue(Rank.King == Rank.instance("CK"));
    assertTrue(Rank.Ace == Rank.instance("CA"));
    try {
      Rank.instance("");
      fail("expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertEquals("no Rank found in ''", e.getMessage());
    }
  }

  public static void testToChar() {
    assertEquals('2', Rank.Two.toChar());
    assertEquals('3', Rank.Three.toChar());
    assertEquals('4', Rank.Four.toChar());
    assertEquals('5', Rank.Five.toChar());
    assertEquals('6', Rank.Six.toChar());
    assertEquals('7', Rank.Seven.toChar());
    assertEquals('8', Rank.Eight.toChar());
    assertEquals('9', Rank.Nine.toChar());
    assertEquals('T', Rank.Ten.toChar());
    assertEquals('J', Rank.Jack.toChar());
    assertEquals('Q', Rank.Queen.toChar());
    assertEquals('K', Rank.King.toChar());
    assertEquals('A', Rank.Ace.toChar());
  }
}