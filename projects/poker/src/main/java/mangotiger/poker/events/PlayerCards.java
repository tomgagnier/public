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
public final class PlayerCards implements PlayerEvent, Cards {

  private final String player;
  private final String cards;

  @Parameters({Type.player, Type.cards})
  public PlayerCards(final String player, final String cards) {
    this.player = player == null ? "null" : player;
    this.cards = cards == null ? "null" : cards;
  }

  public String getPlayer() {
    return player;
  }

  public String getCards() {
    return cards;
  }

  @Override public boolean equals(final Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    final PlayerCards that = (PlayerCards)obj;
    return cards.equals(that.cards) && player.equals(that.player);
  }

  @Override public int hashCode() {
    int result = player.hashCode();
    result = 29 * result + cards.hashCode();
    return result;
  }

  @Override public String toString() {
    return "PlayerCards{player=" + player + ",cards=" + cards + '}';
  }
}
