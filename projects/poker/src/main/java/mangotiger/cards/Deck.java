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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/** @author Tom Gagnier */
public final class Deck {
  private final List<Card> cards;

  public Deck() {
    cards = new ArrayList<Card>(Arrays.asList(Card.CARDS));
  }

  Deck(final List<Card> cards) {
    this.cards = new ArrayList<Card>(cards);
  }

  public String toString() {
    return cards.toString();
  }

  public List<Card> draw(final int count) {
    final List<Card> draw = new ArrayList<Card>(count);
    for (int size = Math.min(cards.size(), count); size > 0; --size) {
      final int index = cards.size() - 1;
      final Card card = cards.remove(index);
      draw.add(card);
    }
    return draw;
  }

  public void shuffle() {
    Collections.shuffle(cards);
  }
}
