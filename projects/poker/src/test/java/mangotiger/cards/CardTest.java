/*
 * $Source: $
 *
 * Copyright (C) 2002-2005, Thomas Robert Gagnier, Jr., All Rights Reserved.
 * Unauthorized use, disclosure or reproduction of this source code is strictly
 * prohibited by United States copyright law and international treaty provisions.
 * Use of source code requires an appropriate source license.
 */
package mangotiger.cards;

import java.util.Set;
import java.util.TreeSet;

import junit.framework.TestCase;

/**
 * Test {@link Card}.
 * @author Tom Gagnier
 */
public final class CardTest extends TestCase {
  private static final int ACE_OF_SPADES = 51;
  private static final int ACE_OF_HEARTS = 50;
  private static final int TWO_OF_SPADES = 3;
  private static final int TWO_OF_CLUBS = 0;

  public void testCompareTo() {
    final Set<Card> cards = new TreeSet<Card>();
    cards.add(Card.instance("AS"));
    cards.add(Card.instance("JS"));
    cards.add(Card.instance("JH"));
    cards.add(Card.instance("3C"));
    cards.add(Card.instance("7D"));
    cards.add(Card.instance("7H"));
    cards.add(Card.instance("2S"));
    assertEquals("[2S, 3C, 7D, 7H, JH, JS, AS]", cards.toString());
  }

  public static void testToString() {
    assertEquals("2C", Card.instance(Rank.Two, Suit.Clubs).toString());
    assertEquals("AC", Card.instance(Rank.Ace, Suit.Clubs).toString());
    assertEquals("2D", Card.instance(Rank.Two, Suit.Diamonds).toString());
    assertEquals("3D", Card.instance(Rank.Three, Suit.Diamonds).toString());
    assertEquals("KH", Card.instance(Rank.King, Suit.Hearts).toString());
    assertEquals("AS", Card.instance(Rank.Ace, Suit.Spades).toString());
  }

  public static void testEquals() {
    assertTrue(Card.instance("CA").equals(Card.instance(Rank.Ace, Suit.Clubs)));
    assertTrue(Card.instance("AC").equals(Card.instance(Rank.Ace, Suit.Clubs)));
    assertTrue(Card.instance("5D").equals(Card.instance(Rank.Five, Suit.Diamonds)));
    assertTrue(Card.instance("D5").equals(Card.instance(Rank.Five, Suit.Diamonds)));
  }

  public static void testIntValue() {
    assertEquals(TWO_OF_CLUBS, Card.instance("2C").intValue());
    assertEquals(TWO_OF_SPADES, Card.instance("2S").intValue());
    assertEquals(ACE_OF_SPADES, Card.instance("AS").intValue());
    assertEquals(ACE_OF_HEARTS, Card.instance("AH").intValue());
  }
}