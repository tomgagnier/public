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
public final class PlayerComment implements PlayerEvent, Comment {

  private final String player;
  private final String said;

  @Parameters({Type.player, Type.said}) public PlayerComment(final String player, final String said) {
    this.player = player == null ? "null" : player;
    this.said = said == null ? "null" : said;
  }

  public String getPlayer() {
    return player;
  }

  public String getSaid() {
    return said;
  }

  @Override public boolean equals(final Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    final PlayerComment that = (PlayerComment)obj;
    return said.equals(that.said) && player.equals(that.player);
  }

  @Override public int hashCode() {
    int result = player.hashCode();
    result = 29 * result + said.hashCode();
    return result;
  }

  @Override public String toString() {
    return "PlayerComment{player=" + player + ",said=" + said + '}';
  }
}
