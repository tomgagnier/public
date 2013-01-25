/*
 * $Source: $
 *
 * Copyright (C) 2002-2005, Thomas Robert Gagnier, Jr., All Rights Reserved.
 * Unauthorized use, disclosure or reproduction of this source code is strictly
 * prohibited by United States copyright law and international treaty provisions.
 * Use of source code requires an appropriate source license.
 */
package mangotiger.bus;

import junit.framework.TestCase;

/** @author Tom Gagnier */
@SuppressWarnings({"ClassWithoutToString"})
public class BusTest extends TestCase {
  Bus bus = new BusImpl("handle");
  Consumer consumer = new Consumer();
  private static final Event HELLO = new Event("hello");
  private static final Event GOODBYE = new Event("goodbye");

  public void testRegisterFunction() {
    bus.register(consumer, "hello");
    bus.register(consumer, "goodbye");
    assertConsumerWorks();
  }

  public void testRegisterObject() {
    bus.register(consumer);
    assertConsumerWorks();
  }

  private void assertConsumerWorks() {
    assertNull(consumer.hello);
    bus.publish(HELLO);
    assertNotNull(consumer.hello);
    assertEquals(consumer.hello.name(), "hello");
    assertNull(consumer.goodbye);
    bus.publish(GOODBYE);
    assertNotNull(consumer.goodbye);
    assertEquals(consumer.goodbye.name(), GOODBYE.name());
  }

  static class Consumer {
    Event hello;
    Event goodbye;

    public void handleHello(final Event event) {
      hello = event;
    }

    public void handleGoodbye(final Event event) {
      goodbye = event;
    }
  }
}