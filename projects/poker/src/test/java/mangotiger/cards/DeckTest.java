/*
 * $Source: $
 *
 * Copyright (C) 2002-2005, Thomas Robert Gagnier, Jr., All Rights Reserved.
 * Unauthorized use, disclosure or reproduction of this source code is strictly
 * prohibited by United States copyright law and international treaty provisions.
 * Use of source code requires an appropriate source license.
 */
package mangotiger.cards;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

/** @author Tom Gagnier */
public final class DeckTest extends TestCase {
  public static void testDraw() {
    assertExpectedDraw("[3C]", 1);
    assertExpectedDraw("[3C, 2S]", 2);
    assertExpectedDraw("[3C, 2S, 2H]", 3);
    assertExpectedDraw("[3C, 2S, 2H, 2D]", 4);
    assertExpectedDraw("[3C, 2S, 2H, 2D, 2C]", 5);
    assertExpectedDraw("[3C, 2S, 2H, 2D, 2C]", 6);
  }

  private static void assertExpectedDraw(final String draw, final int count) {
    final Deck deck = newFiveCardDeck();
    final List<Card> cards = deck.draw(count);
    assertEquals(draw, cards.toString());
  }

  private static Deck newFiveCardDeck() {
    final List<Card> cards = new ArrayList<Card>(5);
    for (int i = 0; i < 5; ++i) {
      cards.add(Card.instance(i));
    }
    return new Deck(cards);
  }
}