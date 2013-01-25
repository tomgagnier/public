/*
 * $Source: $
 *
 * Copyright (C) 2002-2005, Thomas Robert Gagnier, Jr., All Rights Reserved.
 * Unauthorized use, disclosure or reproduction of this source code is strictly
 * prohibited by United States copyright law and international treaty provisions.
 * Use of source code requires an appropriate source license.
 */
package mangotiger.cards;

import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

/** @author Tom Gagnier */
@SuppressWarnings({"ClassWithoutToString"})
public class PokerHandTest extends TestCase {
  public void testIsStraightFlush() {
    final List<Card> cards = Card.newListOfCards("TS JS QS KS AS");
    assertTrue(PokerHand.isStraightFlush(cards));
  }

// TODO public void testPokerHand() throws Exception {
//    final PokerHand[] hand = {new PokerHand("AH KD 8C 6S 3D"), new PokerHand("JH JD AC 6S 3D"),
//                              new PokerHand("AS AC 8S 8D QH"), new PokerHand("3C 3S 3H AS 9D"),
//                              new PokerHand("AS 2C 3S 4S 5S"), new PokerHand("2S 3S 4S 5S 7S"),
//                              new PokerHand("KS KH KC 4S 4D"), new PokerHand("AS AH AD AC 2S"),
//                              new PokerHand("AS 2S 3S 4S 5S"), new PokerHand("TS JS QS KS AS"),};
//    for (int i = 0; i < hand.length - 1; ++i) {
//      for (int j = i + 1; j < hand.length; ++j) {
//        assertTrue(hand[i].compareTo(hand[j]) < 0);
//      }
//    }
//  }

  public void testRanksAreEqual() throws Exception {
    final List<Card> hand = Card.newListOfCards("TS JS QS KS AS");
    final Rank[] straight = new Rank[]{Rank.Ace, Rank.King, Rank.Queen, Rank.Jack, Rank.Ten};
    assertFalse(PokerHand.ranksAreEqual(straight, hand));
    Arrays.sort(straight);
    assertTrue(PokerHand.ranksAreEqual(straight, hand));
  }
}