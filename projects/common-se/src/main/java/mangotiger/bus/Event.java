/*
 * $Source: $
 *
 * Copyright (C) 2002-2005, Thomas Robert Gagnier, Jr., All Rights Reserved.
 * Unauthorized use, disclosure or reproduction of this source code is strictly
 * prohibited by United States copyright law and international treaty provisions.
 * Use of source code requires an appropriate source license.
 */
package mangotiger.bus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/** @author Tom Gagnier */
public class Event implements Comparable {

  private final String name;
  private final Map<String, String> attributes = new TreeMap<String, String>();

  public Event(final String name) {
    if (name == null) throw new IllegalArgumentException("null name");
    this.name = name;
  }

  public Event(final String name, final Map<String, String>... attributes) {
    this(name);
    for (Map<String, String> attributeMap : attributes) {
      this.attributes.putAll(attributeMap);
    }
  }

  /**
   * A new event.
   * @param name       the event name
   * @param attributes attribute name value pairs separated by spaces.
   */
  public Event(final String name, final String attributes) {
    this(name, attributes.split(" +"));
  }

  public Event(final String name, final String... attributeNameValuePairs) {
    this(name);
    for (int i = 1; i < attributeNameValuePairs.length; i += 2) {
      attributes.put(attributeNameValuePairs[i - 1], attributeNameValuePairs[i]);
    }
  }

  public String name() {
    return name;
  }

  public String get(final String attributeName) {
    final String value = attributes.get(attributeName);
    return value == null ? "" : value;
  }

  public Date asDate(final String attributeName) {
    try {
      final SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd - HH:mm:ss");
      final String date = get(attributeName);
      return format.parse(date);
    } catch (ParseException e) {
      unableToParse(attributeName, e);
      return new Date(0);
    }
  }

  public int asInt(final String attributeName) {
    try {
      return Integer.parseInt(get(attributeName));
    } catch (NumberFormatException e) {
      unableToParse(attributeName, e);
      return 0;
    }
  }

  public int asAmount(final String attributeName) {
    try {
      final String amount = get(attributeName).replaceAll("\\.", "");
      return Integer.parseInt(amount);
    } catch (NumberFormatException e) {
      unableToParse(attributeName, e);
      return 0;
    }
  }

  private void unableToParse(final String attributeName, final Exception e) {
    log().error("unable to parse " + name + '.' + attributeName + '=' + get(attributeName), e);
  }

  @Override public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    final Event that = (Event)o;
    return name.equals(that.name) && attributes.equals(that.attributes);
  }

  @Override public int hashCode() {
    return 29 * name.hashCode() + attributes.hashCode();
  }

  public int compareTo(final Object o) {
    final Event that = (Event)o;
    return toString().compareTo(that.toString());
  }

  @Override public String toString() {
    return "Event{" + name + attributes + '}';
  }

  private static Log log() {
    return LogFactory.getLog(Event.class);
  }
}
