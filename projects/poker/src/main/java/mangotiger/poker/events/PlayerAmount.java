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
public class PlayerAmount implements PlayerEvent, Amount {

  private final String player;
  private final int amount;

  public PlayerAmount(final String player, final int amount) {
    this.player = player == null ? "null" : player;
    this.amount = amount;
  }

  public int getAmount() {
    return amount;
  }

  public String getPlayer() {
    return player;
  }

  @Override public boolean equals(final Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    final PlayerAmount that = (PlayerAmount)obj;
    return amount == that.amount && player.equals(that.player);
  }

  @Override public int hashCode() {
    int result = player.hashCode();
    result = 29 * result + amount;
    return result;
  }

  @Override public String toString() {
    return getClass().getSimpleName() + "{player=" + player + ",amount=" + amount + '}';
  }
}
