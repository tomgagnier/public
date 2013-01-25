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
public final class PlayerSeated implements PlayerEvent, Seat {

  private final String player;
  private final int seat;

  @Parameters({Type.player, Type.seat})
  public PlayerSeated(final String player, final int seat) {
    this.player = player == null ? "null" : player;
    this.seat = seat;
  }

  public String getPlayer() {
    return player;
  }

  public int getSeat() {
    return seat;
  }

  @Override public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    final PlayerSeated that = (PlayerSeated)o;
    return seat == that.seat && player.equals(that.player);
  }

  @Override public int hashCode() {
    int result = 29 * player.hashCode();
    result = 29 * result + seat;
    return result;
  }

  @Override public String toString() {
    return "PlayerSeated{player='" + player + "',seat=" + seat + '}';
  }
}
