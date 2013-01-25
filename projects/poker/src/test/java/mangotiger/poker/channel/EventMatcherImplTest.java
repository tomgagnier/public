/*
 * $Source: $
 *
 * Copyright (C) 2002-2005, Thomas Robert Gagnier, Jr., All Rights Reserved.
 * Unauthorized use, disclosure or reproduction of this source code is strictly
 * prohibited by United States copyright law and international treaty provisions.
 * Use of source code requires an appropriate source license.
 */
package mangotiger.poker.channel;

import java.lang.reflect.Constructor;
import java.util.Date;

import junit.framework.TestCase;
import mangotiger.util.Dates;
import mangotiger.poker.EventMatcher;
import mangotiger.poker.channel.EventMatcherImpl;
import mangotiger.poker.events.Bet;
import mangotiger.poker.events.Type;
import mangotiger.poker.events.Parameters;

/** @author Tom Gagnier */
@SuppressWarnings({"ClassWithoutToString", "FeatureEnvy", "MagicNumber"})
public class EventMatcherImplTest extends TestCase {

  EventMatcherImpl eventMatcher;

  @Override protected void setUp() throws Exception {
    super.setUp();
    final Constructor<?> constructor = Bet.class.getConstructors()[0];
    eventMatcher = new EventMatcherImpl(constructor,
                                        "Bet\\{player=([^,]*),amount=([^ ]*)\\}",
                                        Type.player, Type.amount);
    assertTrue(eventMatcher.isValid());
  }

  public void testBadNewEvent() throws Exception {
    try {
      eventMatcher.newEvent();
      fail("Test not implemented yet");
    } catch (IllegalStateException e) {
      assertEquals("unable to construct public mangotiger.poker.events.Bet(java.lang.String,int) using []",
                   e.getMessage());
    }
  }

  public void testConvert() throws Exception {
    assertNull(eventMatcher.convert(String.class, null));
    assertNull(eventMatcher.convert(Integer.class, null));
    assertEquals("hello", eventMatcher.convert(String.class, "hello"));
    assertEquals(1, eventMatcher.convert(Integer.class, "1"));
    assertEquals(1, eventMatcher.convert(Integer.TYPE, "1"));
    assertEquals(Dates.newDate(2001, 12, 31, 15, 20, 0),
                 eventMatcher.convert(Date.class, "2001/12/31 - 15:20:00"));
    try {
      eventMatcher.convert(getClass(), "");
      fail("expected IllegalStateException");
    } catch (IllegalStateException e) {
      assertEquals("unsupported parameter type: class mangotiger.poker.channel.EventMatcherImplTest", e.getMessage());
    }
  }

  public void testDuplicateKeys() {
    final Constructor<?> constructor = DuplicateKeys.class.getConstructors()[0];
    try {
      final EventMatcher bad = new EventMatcherImpl(constructor, "", Type.player, Type.player);
      fail(bad.toString() + " not expected");
    } catch (IllegalStateException e) {
      assertEquals("duplicate instances of parameter type 'player' in " + constructor, e.getMessage());
    }
  }

  public void testNewEvent() throws Exception {
    assertEquals(new Bet("mangotiger", 42), eventMatcher.newEvent("Bet{player=mangotiger,amount=42}"));
    assertNull(eventMatcher.newEvent("no match"));
  }

  public void testToDate() throws Exception {
    assertNull(eventMatcher.toDate(null));
    try {
      eventMatcher.toDate("bad date input");
      fail("expected IllegalStateException");
    } catch (IllegalStateException e) {
      assertEquals("unable to parse 'bad date input'", e.getMessage());
    }
  }

  public void testToString() throws Exception {
    assertEquals("EventMatcherImpl" +
                 "{public mangotiger.poker.events.Bet(java.lang.String,int)" +
                 ",groups={amount=2, player=1}" +
                 ",pattern=Bet\\{player=([^,]*),amount=([^ ]*)\\}}",
                 eventMatcher.toString());
  }

  public static class DuplicateKeys {

    final String player1;
    final String player2;

    @Parameters({Type.player, Type.player})
    public DuplicateKeys(final String player1, final String player2) {
      this.player1 = player1;
      this.player2 = player2;
    }
  }
}