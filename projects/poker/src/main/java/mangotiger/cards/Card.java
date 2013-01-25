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
import java.util.Comparator;
import java.util.List;

/**
 * A standard playing card.
 * @author Tom Gagnier
 */
public final class Card implements Comparable<Card> {

  static final Card[] CARDS = new Card[Suit.values().length * Rank.values().length];

  static {
    int index = -1;
    for (Rank rank : Rank.values()) {
      for (Suit suit : Suit.values()) {
        CARDS[++index] = new Card(rank, suit);
      }
    }
  }

  private final Suit suit;
  private final Rank rank;
  private final String string;

  private Card(final Rank rank, final Suit suit) {
    this.rank = rank;
    this.suit = suit;
    string = new String(new char[]{rank.toChar(), suit.toChar()});
  }

  /**
   * Construct a card from a number.
   * @param index the 2 of clubs = 0, 2 of diamonds = 1, 2 of hearts = 3, ..., Ace of Spades = 51
   */
  public static Card instance(final int index) {
    return CARDS[index % CARDS.length];
  }

  public static Card instance(final Rank rank, final Suit suit) {
    return instance(index(rank, suit));
  }

  public static Card instance(final String card) {
    return instance(Rank.instance(card), Suit.instance(card));
  }

  public static List<Card> newListOfCards(final String cards) {
    final String[] array = cards.split(" ");
    final List<Card> list = new ArrayList<Card>(array.length);
    for (String card : array) {
      list.add(instance(card));
    }
    return list;
  }

  /** The index of the card of (rank, suit) in CARDS. */
  private static int index(final Rank rank, final Suit suit) {
    return rank.ordinal() * Suit.values().length + suit.ordinal();
  }

  public Rank rank() {
    return rank;
  }

  public Suit suit() {
    return suit;
  }

  @Override public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    final Card that = (Card)o;
    return rank == that.rank && suit == that.suit;
  }

  public int intValue() {
    return index(rank, suit);
  }

  @Override public int hashCode() {
    return 29 * suit.hashCode() + rank.hashCode();
  }

  public int compareTo(final Card card) {
    return equals(card) ? 0 : intValue() < card.intValue() ? -1 : 1;
  }

  @Override public String toString() {
    return string;
  }

  public static final Comparator<Card> BY_RANK = new Comparator<Card>() {
    public int compare(final Card o1, final Card o2) {
      return o1.rank().compareTo(o2.rank());
    }
  };
  
  public static final Comparator<Card> BY_SUIT_BY_RANK = new Comparator<Card>() {
    public int compare(final Card o1, final Card o2) {
      final int suit = o1.suit().compareTo(o2.suit());
      return suit == 0 ? o1.rank().compareTo(o2.rank()) : suit;
    }
  };
}
