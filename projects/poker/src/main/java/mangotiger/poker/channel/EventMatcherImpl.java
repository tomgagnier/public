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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import mangotiger.poker.EventMatcher;
import mangotiger.poker.events.Type;
import mangotiger.poker.events.Parameters;

/** @author Tom Gagnier */
public final class EventMatcherImpl implements EventMatcher {

  private final Pattern pattern;
  private final Map<Type, Integer> groupIndexByType = new TreeMap<Type, Integer>();
  private final Constructor<?> constructor;
  private final Type[] constructorTypes;
  private final SimpleDateFormat yyyyMMdd_HHmmss = new SimpleDateFormat("yyyy/MM/dd - HH:mm:ss");
  private static final Type[] ZERO_TYPES = new Type[0];

  public EventMatcherImpl(final Constructor<?> constructor, final String regex, final Type... groups) {
    this.constructor = constructor;
    pattern = Pattern.compile(regex);
    constructorTypes = args(constructor);
    for (int i = 0; i < groups.length; ++i) {
      final Type key = groups[i];
      if (groupIndexByType.containsKey(key)) {
        throw new IllegalStateException("duplicate instances of parameter type '" + key + "' in " + constructor);
      }
      groupIndexByType.put(key, i + 1);
    }
  }

  public boolean isValid() {
    return constructorTypes.length == constructor.getParameterTypes().length &&
           groupIndexByType.keySet().containsAll(Arrays.asList(constructorTypes));
  }

  public String getName() {
    return constructor.getName();
  }

  public static Type[] args(final Constructor<?> constructor) {
    return constructor.isAnnotationPresent(Parameters.class)
           ? constructor.getAnnotation(Parameters.class).value() : ZERO_TYPES;
  }

  @Override public String toString() {
    return getClass().getSimpleName() + '{' + constructor + ",groups=" + groupIndexByType + ",pattern=" + pattern + '}';
  }

  public Object newEvent(final String input) {
    final Matcher matcher = pattern.matcher(input);
    if (matcher.matches()) {
      final Object[] parameters = parametersFrom(matcher);
      return newEvent(parameters);
    }
    return null;
  }

  Object newEvent(final Object... parameters) {
    try {
      return constructor.newInstance(parameters);
    } catch (Exception e) {
      log().error(e, e);
      throw new IllegalStateException("unable to construct " + constructor + " using " + Arrays.asList(parameters), e);
    }
  }

  Object[] parametersFrom(final Matcher matcher) {
    final Object[] parameters = new Object[constructor.getParameterTypes().length];
    for (int i = 0; i < parameters.length; ++i) {
      final Type type = constructorTypes[i];
      final Integer index = groupIndexByType.get(type);
      assert index != null;
      final String group = matcher.group(index);
      final Class<?> parameterType = constructor.getParameterTypes()[i];
      parameters[i] = convert(parameterType, group);
    }
    return parameters;
  }

  private static Log log() {
    return LogFactory.getLog(EventMatcherImpl.class);
  }

  Object convert(final Class<?> parameterType, final String value) {
    if (value == null) return value;
    if (String.class.equals(parameterType)) return value;
    if (Integer.class.equals(parameterType)) return Integer.parseInt(value);
    if (Integer.TYPE.equals(parameterType)) return Integer.parseInt(value);
    if (Date.class.equals(parameterType)) return toDate(value);
    final String message = "unsupported parameter type: " + parameterType;
    log().error(message);
    throw new IllegalStateException(message);
  }

  Date toDate(final String value) {
    try {
      return value == null ? null : yyyyMMdd_HHmmss.parse(value);
    } catch (ParseException e) {
      throw new IllegalStateException("unable to parse '" + value + '\'', e);
    }
  }
}