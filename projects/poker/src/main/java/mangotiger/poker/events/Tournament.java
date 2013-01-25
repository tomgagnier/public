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
public final class Tournament {

  private final String tournament;
  private final String description;
  private final Date date;

  @Parameters({Type.tournament, Type.description, Type.date})
  public Tournament(final String tournament, final String description, final Date date) {
    this.tournament = tournament == null ? "null" : tournament;
    this.description = description == null ? "null" : description;
    this.date = date == null ? new Date(0) : date;
  }

  public Date getDate() {
    return date == null ? null : new Date(date.getTime());
  }

  public String getDescription() {
    return description;
  }

  public String getTournament() {
    return tournament;
  }

  @Override public boolean equals(final Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    final Tournament that = (Tournament) obj;
    return description.equals(that.description) && tournament.equals(that.tournament) && date.equals(that.date);
  }

  @Override public int hashCode() {
    int result = tournament.hashCode();
    result = 29 * result + description.hashCode();
    result = 29 * result + date.hashCode();
    return result;
  }

  @Override public String toString() {
    return getClass().getSimpleName() +
        "{tournament='" + tournament + '\'' +
        ",game='" + description + '\'' +
        ",date=" + date +
        '}';
  }
}
