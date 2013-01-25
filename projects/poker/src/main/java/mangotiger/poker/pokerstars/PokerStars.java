/*
 * $Source: $
 *
 * Copyright (C) 2002-2005, Thomas Robert Gagnier, Jr., All Rights Reserved.
 * Unauthorized use, disclosure or reproduction of this source code is strictly
 * prohibited by United States copyright law and international treaty provisions.
 * Use of source code requires an appropriate source license.
 */
package mangotiger.poker.pokerstars;

import java.io.File;
import java.util.Iterator;

import mangotiger.io.RecursiveFileIterator;
import mangotiger.poker.Casino;
import mangotiger.poker.EventChannel;
import mangotiger.poker.EventParser;
import mangotiger.poker.Game;
import mangotiger.poker.channel.EventParserImpl;
import mangotiger.poker.events.Ante;
import mangotiger.poker.events.Bet;
import mangotiger.poker.events.BigBlind;
import mangotiger.poker.events.Button;
import mangotiger.poker.events.Call;
import mangotiger.poker.events.Check;
import mangotiger.poker.events.Collect;
import mangotiger.poker.events.Flop;
import mangotiger.poker.events.Fold;
import mangotiger.poker.events.GameEnd;
import mangotiger.poker.events.Muck;
import mangotiger.poker.events.NewGame;
import mangotiger.poker.events.PlayerCards;
import mangotiger.poker.events.PlayerComment;
import mangotiger.poker.events.PlayerSeated;
import mangotiger.poker.events.Raise;
import mangotiger.poker.events.River;
import mangotiger.poker.events.SmallBlind;
import mangotiger.poker.events.Table;
import mangotiger.poker.events.Tournament;
import mangotiger.poker.events.Turn;
import mangotiger.poker.events.Type;
import mangotiger.poker.events.Bankroll;
import mangotiger.poker.game.TexasHoldem;

/** @author Tom Gagnier */
@SuppressWarnings({"OverlyCoupledClass", "OverlyCoupledMethod", "FeatureEnvy"})
public class PokerStars implements Casino {

  public static final File HAND_HISTORY = new File("C:/Program Files/PokerStars/HandHistory");
  private long time;

  public void setTime(final long systemTimeMillis) {
    time = systemTimeMillis;
  }

  public Game newGame(final NewGame event) {
    return new TexasHoldem(event.getGame(), event.getDate());
  }

  public Iterator<File> files() {
    return files(time);
  }

  public Iterator<File> files(final long since) {
    time = System.currentTimeMillis();
    return new RecursiveFileIterator(HAND_HISTORY, new NoLimitHoldemTourneyFilter(since));
  }

  public EventParser newEventParser(final EventChannel channel) {
    final EventParser eventParser = new EventParserImpl(channel);
    addMatchers(eventParser);
    return eventParser;
  }

  public void addMatchers(final EventParser eventParser) {
    eventParser.add(Bet.class, "([^:]+): bets ([^ ]+).*", Type.player, Type.amount);
    eventParser.add(Call.class, "([^:]+): calls ([^ ]+).*", Type.player, Type.amount);
    eventParser.add(Check.class, "^([^:]+): checks.*", Type.player);
    eventParser.add(Fold.class, "([^:]+): folds.*", Type.player);
    eventParser.add(Raise.class, "([^:]+): raises ([^ ]+) to [^ ]+.*", Type.player, Type.amount);
    eventParser.add(PlayerComment.class, "(.*) said, \"(.*)\"", Type.player, Type.said);
    eventParser.add(Collect.class, "(.*) collected (.*) from (.*)", Type.player, Type.amount, Type.description);
    eventParser.add(Muck.class, "(.*): doesn't show hand.*", Type.player);
    eventParser.add(Ante.class, "([^:]+): posts the ante ([0-9]+)", Type.player, Type.amount);
    eventParser.add(SmallBlind.class, "([^:]+): posts small blind ([0-9]+)", Type.player, Type.amount);
    eventParser.add(BigBlind.class, "([^:]+): posts big blind ([0-9]+)", Type.player, Type.amount);
    eventParser.add(PlayerCards.class, "([^:]+): shows \\[([^\\]]+)\\] \\(([^)]+)\\)", Type.player, Type.cards,
                    Type.description);
    eventParser.add(PlayerSeated.class,
                    "Seat ([0-9]+): (.*) \\(([1-9][0-9]*) in chips\\) *(is sitting (out))?",
                    Type.seat, Type.player, Type.amount, Type.description, Type.status);
    eventParser.add(Bankroll.class,
                    "Seat ([0-9]+): (.*) \\(([1-9][0-9]*) in chips\\) *(is sitting (out))?",
                    Type.seat, Type.player, Type.amount, Type.description, Type.status);
    eventParser.add(PlayerCards.class, "Dealt to (.*) \\[(.*)\\]", Type.player, Type.cards);
    eventParser.add(Flop.class, "\\*\\*\\* FLOP \\*\\*\\* (\\[.*\\])", Type.cards);
    eventParser.add(River.class, "\\*\\*\\* RIVER \\*\\*\\* (\\[.*\\] \\[.*\\])", Type.cards);
    eventParser.add(Turn.class, "\\*\\*\\* TURN \\*\\*\\* (\\[.*\\] \\[.*\\])", Type.cards);
    eventParser.add(NewGame.class,
                    "^PokerStars Game #([0-9]+): Tournament #[0-9]+, ([^-]+) - Level [^-]+ - (.*)",
                    Type.game, Type.description, Type.date);
    eventParser.add(Tournament.class,
                    "^PokerStars Game #[0-9]+: Tournament #([0-9]+), ([^-]+) - Level [^-]+ - (.*)",
                    Type.tournament, Type.description, Type.date);
    eventParser.add(GameEnd.class, "\\*\\*\\* SUMMARY \\*\\*\\*");
    eventParser.add(Table.class, "Table ('[^']+)' Seat #[0-9]+ is the button", Type.table);
    eventParser.add(Button.class, "Table '[^']+' Seat #([0-9]+) is the button", Type.seat);
  }

  @Override public String toString() {
    return getClass().getSimpleName() +
           "{time=" + time +
           '}';
  }
}
