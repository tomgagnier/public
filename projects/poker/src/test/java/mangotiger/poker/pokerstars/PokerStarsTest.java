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
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import junit.framework.TestCase;
import mangotiger.poker.Casino;
import mangotiger.poker.EventChannel;
import mangotiger.poker.EventParser;
import mangotiger.poker.channel.EventChannelImpl;
import mangotiger.poker.channel.EventLogger;
import mangotiger.poker.game.Pot;
import mangotiger.time.Times;

/** @author Tom Gagnier */
@SuppressWarnings({"ClassWithoutToString", "PublicMethodNotExposedInInterface"})
public class PokerStarsTest extends TestCase {

  Casino factory = new PokerStars();
  EventChannel channel = new EventChannelImpl();
  EventParser parser = factory.newEventParser(channel);

  private static Log log() {
    return LogFactory.getLog(PokerStarsTest.class);
  }

  public void test() throws IOException {
    assertPokerStarsFileValid("HH20050909 T12350072 No Limit Hold'em  $15 + $1.txt");
  }

  private void assertPokerStarsFileValid(final String resource) throws IOException {
    final EventLogger logger = new EventLogger();
    channel.subscribe(logger);
    final Pot pot = new Pot();
    channel.subscribe(pot);
    final InputStream in = getClass().getResourceAsStream(resource);
    assertNotNull(in);
    parser.parse(in);
    assertEquals(0, pot.getTotal());
  }

  public void testFiles() throws Exception {
    final long since = System.currentTimeMillis() - Times.MILLISECONDS_PER_WEEK;
    final Iterator<File> files = factory.files(since);
    while (files.hasNext()) {
      final File file = files.next();
      log().info(file);
      parser.parse(file);
    }
  }

  public void testRaise() throws IOException {
    assertPokerStarsFileValid("Raise.txt");
  }

  public void testSidePot() throws IOException {
    assertPokerStarsFileValid("SidePot.txt");
  }
}