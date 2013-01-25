/*
 * $Source: $
 *
 * Copyright (C) 2002-2005, Thomas Robert Gagnier, Jr., All Rights Reserved.
 * Unauthorized use, disclosure or reproduction of this source code is strictly
 * prohibited by United States copyright law and international treaty provisions.
 * Use of source code requires an appropriate source license.
 */
package mangotiger.bus;

/**
 * A simple bus bus that registers consumers and publishes {@link Event}s.
 * @author Tom Gagnier
 */
public interface Bus {
  /**
   * Register a consumer of bus events.  All events matching the signature <pre>public void
   * on<i>EventName</i>(Event)</pre> are
   * registered. For example: <code>public Class { public void onCall(Event e){}} </code> will have be registered for
   * all events named "call".
   */
  void register(Object consumer);

  /**
   * Register a consumer of a named event.  For example, if a consumer is registered for an event named "call", the
   * <code>Bus</code> will publish the event to consumer via it's <code>public void onCall(Event)</code> method.
   * @throws IllegalArgumentException if a suitable "onEvent" method does not exist.
   */
  void register(Object consumer, String eventName);

  /** Publish an event to all registered consumers. */
  void publish(Event event);

  /** Remove consumer. */
  void unregister(Object consumer);
}
