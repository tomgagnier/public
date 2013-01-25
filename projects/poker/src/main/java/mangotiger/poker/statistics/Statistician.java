/*
 * $Source: $
 *
 * Copyright (C) 2002-2005, Thomas Robert Gagnier, Jr., All Rights Reserved.
 * Unauthorized use, disclosure or reproduction of this source code is strictly
 * prohibited by United States copyright law and international treaty provisions.
 * Use of source code requires an appropriate source license.
 */
package mangotiger.poker.statistics;

import java.util.Map;
import java.util.TreeMap;

import mangotiger.poker.events.PlayerEvent;

/** @author Tom Gagnier */
public class Statistician {
  final Map<String, PlayerStats> playerStatsByName = new TreeMap<String, PlayerStats>();

  public void add(final Statistician statistician) {
    for (PlayerStats playerStats : statistician.playerStatsByName.values()) {
      add(playerStats);
    }
  }

  public PlayerStats add(final PlayerStats playerStats) {
    return get(playerStats.name).add(playerStats);
  }

  public PlayerStats add(final PlayerEvent event) {
    return get(event);
  }

  public PlayerStats get(final PlayerEvent event) {
    return get(event.getPlayer());
  }

  public PlayerStats get(final String playerName) {
    if (!playerStatsByName.containsKey(playerName)) {
      playerStatsByName.put(playerName, new PlayerStats(playerName));
    }
    return playerStatsByName.get(playerName);
  }

  @Override public String toString() {
    final StringBuilder builder = new StringBuilder();
    builder.append(getClass().getSimpleName()).append('{');
    for (PlayerStats playerStats : playerStatsByName.values()) {
      builder.append("\n\t").append(playerStats);
    }
    return builder.append('}').toString();
  }
}
