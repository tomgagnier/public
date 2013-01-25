/*
 * $Source: $
 *
 * Copyright (C) 2002-2005, Thomas Robert Gagnier, Jr., All Rights Reserved.
 * Unauthorized use, disclosure or reproduction of this source code is strictly
 * prohibited by United States copyright law and international treaty provisions.
 * Use of source code requires an appropriate source license.
 */
package mangotiger.poker.game;

import java.util.Date;

import mangotiger.poker.EventChannel;
import mangotiger.poker.Game;
import mangotiger.poker.events.Bet;
import mangotiger.poker.events.Call;
import mangotiger.poker.events.Cards;
import mangotiger.poker.events.Check;
import mangotiger.poker.events.Fold;
import mangotiger.poker.events.GameEnd;
import mangotiger.poker.events.PlayerAmount;
import mangotiger.poker.events.PlayerSeated;
import mangotiger.poker.events.Raise;
import mangotiger.poker.events.Tournament;
import mangotiger.poker.statistics.PlayerStats;
import mangotiger.poker.statistics.Statistician;

/** @author Tom Gagnier */
@SuppressWarnings({"FeatureEnvy", "PublicMethodNotExposedInInterface"})
public class TexasHoldem implements Game {
  private final String name;
  private final Date date;
  private final Pot pot = new Pot();
  private final Statistician statistician = new Statistician();
  private EventChannel channel;
  private PlayerAmount lastAggression;
  private Cards board;
  private String tournament;

  public TexasHoldem(final String name, final Date date) {
    this.name = name;
    this.date = date == null ? date : new Date(date.getTime());
  }

  public String getTournament() {
    return tournament;
  }

  public void on(final Tournament event) {
    tournament = event.getTournament();
  }

  public void on(final Cards event) {
    board = event;
    //noinspection AssignmentToNull
    lastAggression = null;
  }

  public void on(final Raise event) {
    final PlayerStats player = statistician.get(event);
    player.raises.yes();
    player.calls.no();
    player.folds.no();
    lastAggression = event;
  }

  public void on(final Fold event) {
    final PlayerStats player = statistician.get(event);
    player.folds.yes();
    if (lastAggression == null) {
      player.checks.no();
      player.bets.no();
    } else {
      player.calls.no();
      player.raises.no();
    }
  }

  public void on(final Check event) {
    final PlayerStats player = statistician.get(event);
    player.checks.yes();
    player.folds.no();
    player.bets.no();
  }

  public void on(final Call event) {
    final PlayerStats player = statistician.get(event);
    player.calls.yes();
    player.raises.no();
    player.folds.no();
  }

  public void on(final Bet event) {
    final PlayerStats player = statistician.get(event);
    player.bets.yes();
    player.checks.no();
    player.folds.no();
    lastAggression = event;
  }

  public void on(final PlayerSeated event) {
    statistician.add(event);
  }

  public void on(final GameEnd event) {
    channel.cancel(pot);
    channel.cancel(this);
  }

  public void setEventChannel(final EventChannel channel) {
    this.channel = channel;
    this.channel.subscribe(this);
    this.channel.subscribe(pot);
  }

  public Statistician statistican() {
    return statistician;
  }

  @Override public String toString() {
    return getClass().getSimpleName() +
           "{name='" + name + '\'' +
           ",date=" + date +
           ",pot=" + pot +
           ",statistician=" + statistician +
           '}';
  }
}
