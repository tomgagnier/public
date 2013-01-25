/*
 * $Source: $
 *
 * Copyright (C) 2002-2005, Thomas Robert Gagnier, Jr., All Rights Reserved.
 * Unauthorized use, disclosure or reproduction of this source code is strictly
 * prohibited by United States copyright law and international treaty provisions.
 * Use of source code requires an appropriate source license.
 */
package mangotiger.bus;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import junit.framework.TestCase;

/** @author Tom Gagnier */
@SuppressWarnings({"ClassWithoutToString"})
public class EventTest extends TestCase {
  final Event[] events = {
      new Event("hello", "one 1 two 2 three 3"),
      new Event("hello", "one 1 two 2 three 3"),
      new Event("goodbye", "one 1 two 2 three 3")
  };

  public void testEquals() throws Exception {
    assertTrue(events[0].equals(events[0]));
    assertTrue(events[0].equals(events[1]));
    assertTrue(events[1].equals(events[0]));
    assertTrue(events[1].equals(events[1]));
    assertFalse(events[0].equals(events[2]));
    assertFalse(events[1].equals(events[2]));
    assertFalse("".equals(events[1]));
    //noinspection ObjectEqualsNull
    assertFalse(events[1].equals(null));
  }

  public void testCompareTo() {
    final Set<Event> set = new TreeSet<Event>(Arrays.asList(events));
    assertEquals("[Event{goodbye{one=1, three=3, two=2}}, Event{hello{one=1, three=3, two=2}}]", set.toString());
  }

  public void testNullName() {
    try {
      new Event(null);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("null name", e.getMessage());
    }
  }

  public void testBadEvent() {
    final Event bad = new Event("bad", "date 1 int a amount three");
    assertEquals(0L, bad.asDate("date").getTime());
    assertEquals(0, bad.asInt("int"));
    assertEquals(0, bad.asAmount("amount"));
  }

  public void testHashCode() {
    final Set<Event> set = new HashSet<Event>(Arrays.asList(events));
    assertEquals(2, set.size());
  }

  public void testGet() throws Exception {
    assertEquals("", events[0].get("xyz"));
  }
}