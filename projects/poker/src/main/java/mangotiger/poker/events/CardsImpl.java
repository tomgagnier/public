/*
 * $Source: $
 *
 * Copyright (C) 2002-2005, Thomas Robert Gagnier, Jr., All Rights Reserved.
 * Unauthorized use, disclosure or reproduction of this source code is strictly
 * prohibited by United States copyright law and international treaty provisions.
 * Use of source code requires an appropriate source license.
 */
package mangotiger.poker.events;

/** @author Tom Gagnier */
class CardsImpl implements Cards {

  private final String cards;

  CardsImpl(final String cards) {
    this.cards = cards == null ? "null" : cards;
  }

  public String getCards() {
    return cards;
  }

  @Override public boolean equals(final Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    final CardsImpl that = (CardsImpl)obj;
    return cards.equals(that.cards);
  }

  @Override public int hashCode() {
    return cards.hashCode();
  }

  @Override public String toString() {
    return getClass().getSimpleName() + "{cards=" + cards + '}';
  }
}
