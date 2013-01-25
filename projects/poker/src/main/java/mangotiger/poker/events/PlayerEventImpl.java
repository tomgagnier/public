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
class PlayerEventImpl implements PlayerEvent {

  private final String player;

  PlayerEventImpl(final String player) {
    this.player = player;
  }

  public String getPlayer() {
    return player;
  }

  @Override public boolean equals(final Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    final PlayerEventImpl that = (PlayerEventImpl)obj;
    return player.equals(that.player);
  }

  @Override public int hashCode() {
    return player.hashCode();
  }

  @Override public String toString() {
    return getClass().getSimpleName() + "{player=" + player + '}';
  }
}
