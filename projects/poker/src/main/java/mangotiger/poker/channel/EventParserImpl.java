/*
 * $Source: $
 *
 * Copyright (C) 2002-2005, Thomas Robert Gagnier, Jr., All Rights Reserved.
 * Unauthorized use, disclosure or reproduction of this source code is strictly
 * prohibited by United States copyright law and international treaty provisions.
 * Use of source code requires an appropriate source license.
 */
package mangotiger.poker.channel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import mangotiger.poker.EventChannel;
import mangotiger.poker.EventMatcher;
import mangotiger.poker.EventParser;
import mangotiger.poker.events.Type;

/** @author Tom Gagnier */
public final class EventParserImpl implements EventParser {

  private final Collection<EventMatcher> eventMatchers = new LinkedList<EventMatcher>();
  private final EventChannel channel;

  public EventParserImpl(final EventChannel channel) {
    this.channel = channel;
  }

  @Override public String toString() {
    final StringBuilder events = new StringBuilder();
    for (EventMatcher eventMatcher : eventMatchers) {
      events.append(eventMatcher.getName());
    }
    return getClass().getSimpleName() + '{' + eventMatchers + '}';
  }

  public void parse(final File file) throws IOException {
    InputStream in = null;
    try {
      in = new FileInputStream(file);
      parse(in);
    } finally {
      if (in != null) in.close();
    }
  }

  public void unregister() {
    if (channel != null) channel.cancel(this);
  }

  public void parse(final InputStream in) throws IOException {
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new InputStreamReader(in));
      int lineNumber = 1;
      for (String line = reader.readLine(); line != null; line = reader.readLine(), ++lineNumber) {
        parse(line);
      }
    } finally {
      if (reader != null) reader.close();
    }
  }

  void parse(final String input) {
    if (channel == null) throw new IllegalStateException("event channel not set");
    for (EventMatcher eventMatcher : eventMatchers) {
      final Object event = eventMatcher.newEvent(input);
      if (event != null) {
        channel.publish(event);
      }
    }
  }

  public void add(final Class<?> event, final String regex, final Type... groups) {
    final List<EventMatcher> valid = new LinkedList<EventMatcher>();
    final List<EventMatcher> invalid = new LinkedList<EventMatcher>();
    for (Constructor<?> constructor : event.getConstructors()) {
      final EventMatcher eventMatcher = new EventMatcherImpl(constructor, regex, groups);
      if (eventMatcher.isValid()) {
        valid.add(eventMatcher);
      } else {
        invalid.add(eventMatcher);
      }
    }
    if (valid.size() > 1) log().warn("duplicate constructors match, using first one: " + valid);
    if (valid.size() > 0) {
      eventMatchers.add(valid.get(0));
    } else {
      final StringBuilder message = new StringBuilder("no matching constructor found:");
      for (EventMatcher eventMatcher : invalid) {
        message.append("\n\t").append(eventMatcher);
      }
      throw new IllegalArgumentException(message.toString());
    }
  }

  private static Log log() {
    return LogFactory.getLog(EventParserImpl.class);
  }
}
