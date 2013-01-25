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
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/** @author Tom Gagnier */
public final class PokerHand implements Comparable<PokerHand> {

  private final List<Card> hand;
  private final int rank;

  public PokerHand(final String cards) {
    hand = Card.newListOfCards(cards);
    rank = calculateRank();
  }

  public PokerHand(final Collection<Card> cards) {
    hand = new ArrayList<Card>(cards);
    rank = calculateRank();
  }

  private int calculateRank() {
    if (isStraightFlush(hand)) return 0x80000000 + hand.get(1).rank().ordinal();
    return 0;  // todo
  }

  @Override public String toString() {
    return hand.toString();
  }

// Interface Comparable ...

  public int compareTo(final PokerHand that) {
    return 0;  // todo
  }

  public static boolean isStraightFlush(final List<Card> hand) {
    final int rank = isStraight(hand);
    return rank != -1 && isFlush(hand);
  }

  static boolean isFlush(final List<Card> hand) {
    final Suit suit = hand.get(0).suit();
    for (int i = 1; i < hand.size(); ++i) {
      if (hand.get(i).suit() != suit) return false;
    }
    return true;
  }

  private static final Rank[][] straights = {
      {Rank.Two, Rank.Three, Rank.Four, Rank.Five, Rank.Ace},
      {Rank.Two, Rank.Three, Rank.Four, Rank.Five, Rank.Six},
      {Rank.Three, Rank.Four, Rank.Five, Rank.Six, Rank.Seven},
      {Rank.Four, Rank.Five, Rank.Six, Rank.Seven, Rank.Eight},
      {Rank.Five, Rank.Six, Rank.Seven, Rank.Eight, Rank.Nine},
      {Rank.Six, Rank.Seven, Rank.Eight, Rank.Nine, Rank.Ten,},
      {Rank.Seven, Rank.Eight, Rank.Nine, Rank.Ten, Rank.Jack},
      {Rank.Eight, Rank.Nine, Rank.Ten, Rank.Jack, Rank.Queen},
      {Rank.Nine, Rank.Ten, Rank.Jack, Rank.Queen, Rank.King},
      {Rank.Ten, Rank.Jack, Rank.Queen, Rank.King, Rank.Ace}
  };

  static int isStraight(final List<Card> hand) {
    assert hand.size() == 5;
    Collections.sort(hand);
    for (int i = 0; i < straights.length; ++i) {
      if (ranksAreEqual(straights[i], hand)) return i;
    }
    return -1;
  }

  static boolean ranksAreEqual(final Rank[] ranks, final List<Card> hand) {
    if (ranks.length != hand.size()) return false;
    for (int j = 0; j < ranks.length; ++j) {
      if (hand.get(j).rank() != ranks[j]) return false;
    }
    return true;
  }
}
