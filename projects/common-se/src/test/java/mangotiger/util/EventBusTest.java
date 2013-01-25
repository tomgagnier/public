/*
 * $Source: $
 *
 * Copyright (C) 2002-2005, Thomas Robert Gagnier, Jr., All Rights Reserved.
 * Unauthorized use, disclosure or reproduction of this source code is strictly
 * prohibited by United States copyright law and international treaty provisions.
 * Use of source code requires an appropriate source license.
 */
package mangotiger.util;

import java.util.Date;

import junit.framework.TestCase;

/** @author Tom Gagnier */
public class EventBusTest extends TestCase {
  EventBus bus = new EventBus("on");
  Consumer consumer = new Consumer();

  public void test() {
    bus.register(String.class, consumer);
    bus.register(Integer.class, consumer);
    bus.register(Exception.class, consumer);
    bus.publish("hello");
    assertEquals("hello", consumer.object);
    bus.publish(1);
    assertEquals(1, consumer.object);
    bus.publish(1L);
    bus.publish(new Exception());
  }

  public void testUnhandledEvent() {
    try {
      bus.register(Date.class, consumer);
      fail("expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertEquals("java.lang.NoSuchMethodException: mangotiger.util.EventBusTest$Consumer.on(java.util.Date)",
                   e.getMessage());
    }
  }

  static class Consumer {
    Object object;

    public void on(final String s) {
      object = s;
    }

    public void on(final Integer i) {
      object = i;
    }

    public static void on(final Exception e) throws Exception {
      throw e;
    }

    @Override public String toString() {
      return "Statistician{object=" + object + '}';
    }
  }
}