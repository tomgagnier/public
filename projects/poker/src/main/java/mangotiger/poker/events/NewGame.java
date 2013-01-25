/*
 * $Source: $
 *
 * Copyright (C) 2002-2005, Thomas Robert Gagnier, Jr., All Rights Reserved.
 * Unauthorized use, disclosure or reproduction of this source code is strictly
 * prohibited by United States copyright law and international treaty provisions.
 * Use of source code requires an appropriate source license.
 */
package mangotiger.poker.events;

import java.util.Date;

/** @author Tom Gagnier */
public final class NewGame {
  private final String game;
  private final String description;
  private final Date date;

  @Parameters({Type.game, Type.description, Type.date})
  public NewGame(final String game, final String description, final Date date) {
    this.game = game == null ? "null" : game;
    this.description = description == null ? "null" : description;
    this.date = date == null ? new Date(0) : date;
  }

  public Date getDate() {
    return date;
  }

  public String getDescription() {
    return description;
  }

  public String getGame() {
    return game;
  }

  @Override public boolean equals(final Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    final NewGame that = (NewGame)obj;
    return description.equals(that.description) && game.equals(that.game) && date.equals(that.date);
  }

  @Override public int hashCode() {
    int result = game.hashCode();
    result = 29 * result + description.hashCode();
    result = 29 * result + date.hashCode();
    return result;
  }

  @Override public String toString() {
    return getClass().getSimpleName() +
           "{game='" + game + '\'' +
           ",description='" + description + '\'' +
           ",date=" + date +
           '}';
  }
}
