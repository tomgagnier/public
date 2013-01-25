/*
 * $Source: $
 *
 * Copyright (C) 2002-2005, Thomas Robert Gagnier, Jr., All Rights Reserved.
 * Unauthorized use, disclosure or reproduction of this source code is strictly
 * prohibited by United States copyright law and international treaty provisions.
 * Use of source code requires an appropriate source license.
 */
package mangotiger.poker;

import mangotiger.poker.events.GameEnd;
import mangotiger.poker.events.NewGame;
import static mangotiger.time.Times.MILLISECONDS_PER_WEEK;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/** @author Tom Gagnier */
@SuppressWarnings({"ClassWithoutToString", "PublicMethodNotExposedInInterface"})
public class Analyst implements Runnable {

  private static final long ANALYSIS_PERIOD = MILLISECONDS_PER_WEEK;
  private final EventChannel channel;
  private final Casino casino;
  private final EventParser parser;
  private Game game;
  private final Set<String> games = new TreeSet<String>();

  public Analyst(final EventChannel channel, final Casino casino) {
    this.channel = channel;
    this.casino = casino;
    parser = casino.newEventParser(channel);
    casino.setTime(System.currentTimeMillis() - ANALYSIS_PERIOD);
    //noinspection ThisEscapedInObjectConstruction
    channel.subscribe(this);
  }

  private static Log log() {
    return LogFactory.getLog(Analyst.class);
  }

  public void on(final NewGame event) {
    final String name = event.getGame();
    if (games.add(name)) {
      game = casino.newGame(event);
      game.setEventChannel(channel);
    }
  }

  public void on(final GameEnd event) {
    channel.publish(game);
  }

  public void run() {
    for (final Iterator<File> files = casino.files(); files.hasNext();) {
      parse(files.next());
    }
  }

  public void parse(final File file) {
    try {
      parser.parse(file);
    } catch (Exception e) {
      log().error("unable to parse " + file, e);
    }
  }
}
