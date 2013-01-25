/*
 * $Source: $
 *
 * Copyright (C) 2002-2005, Thomas Robert Gagnier, Jr., All Rights Reserved.
 * Unauthorized use, disclosure or reproduction of this source code is strictly
 * prohibited by United States copyright law and international treaty provisions.
 * Use of source code requires an appropriate source license.
 */
package mangotiger.poker.statistics;

import java.lang.reflect.Field;

/** @author Tom Gagnier */
@SuppressWarnings({"PublicMethodNotExposedInInterface"}) public class PlayerStats {

  public final String name;
  // actions
  public final Ratio bets = new Ratio("bets");
  public final Ratio calls = new Ratio("calls");
  public final Ratio checks = new Ratio("checks");
  public final Ratio folds = new Ratio("folds");
  public final Ratio raises = new Ratio("raises");
  // other stats
  public final Ratio completesSmallBlind = new Ratio("completes small blind");
  public final Ratio defendsBigBlind = new Ratio("defends big blind");
  public final Ratio limps = new Ratio("limps");
  public final Ratio attemptsSteal = new Ratio("attempts steal");
  public final Ratio involved = new Ratio("involved");

  public PlayerStats(final String name) {
    this.name = name == null ? "" : name;
  }

  public PlayerStats add(final PlayerStats player) {
    if (player == null) return null;
    assert name.equals(player.name);
    for (Field field : PlayerStats.class.getFields()) {
      if (Ratio.class.equals(field.getType())) {
        final Ratio thisRatio = get(field, this);
        final Ratio thatRatio = get(field, player);
        thisRatio.add(thatRatio);
      }
    }
    return this;
  }

  @Override public boolean equals(final Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    final PlayerStats player = (PlayerStats)obj;
    if (!name.equals(player.name)) return false;
    for (Field field : PlayerStats.class.getFields()) {
      if (Ratio.class.equals(field.getType())) {
        final Ratio thisRatio = get(field, this);
        final Ratio thatRatio = get(field, player);
        if (!thisRatio.equals(thatRatio)) return false;
      }
    }
    return true;
  }

  private static Ratio get(final Field field, final PlayerStats player) {
    try {
      return (Ratio)field.get(player);
    } catch (IllegalAccessException e) {
      throw new IllegalStateException(e);
    }
  }

  @Override public int hashCode() {
    return name.hashCode();
  }

  @Override public String toString() {
    final StringBuilder builder = new StringBuilder();
    builder.append(getClass().getSimpleName()).append("{name=").append(name);
    for (Field field : PlayerStats.class.getFields()) {
      if (Ratio.class.equals(field.getType())) {
        builder.append(',').append(field.getName()).append('=').append(get(field, this));
      }
    }
    return builder.append('}').toString();
  }
}
