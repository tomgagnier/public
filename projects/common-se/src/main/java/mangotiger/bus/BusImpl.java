/*
 * $Source: $
 *
 * Copyright (C) 2002-2005, Thomas Robert Gagnier, Jr., All Rights Reserved.
 * Unauthorized use, disclosure or reproduction of this source code is strictly
 * prohibited by United States copyright law and international treaty provisions.
 * Use of source code requires an appropriate source license.
 */
package mangotiger.bus;

import mangotiger.lang.Strings;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** @author Tom Gagnier */
public class BusImpl implements Bus {
  private final String prefix;
  private final Map<String, Consumer> consumersByEventName = new TreeMap<String, Consumer>();
  private final Set<Object> unregistered = new HashSet<Object>();
  private final Pattern eventMethod;
  private boolean debug;

  public BusImpl() {
    this("on");
  }

  /** @param prefix the required method prefix used by the publisher */
  public BusImpl(final String prefix) {
    this.prefix = prefix;
    final String regex = "public void ([a-zA-Z][a-zA-Z_$]+\\.)+" + prefix + "([A-Z][a-zA-Z0-9]+)\\(" +
                         Event.class.getName() + "\\)";
    eventMethod = Pattern.compile(regex);
  }

  public BusImpl(final boolean debug) {
    this();
    this.debug = debug;
  }

  public void register(final Object consumer) {
    final Class clazz = consumer.getClass();
    for (Method method : clazz.getMethods()) {
      final Matcher matcher = eventMethod.matcher(method.toString());
      final boolean isEventMethod = matcher.matches();
      if (isEventMethod) {
        final String eventName = Strings.decapitalize(matcher.group(2));
        register(consumer, eventName);
      }
    }
  }

  public void register(final Object consumer, final String eventName) {
    if (!consumersByEventName.containsKey(eventName)) consumersByEventName.put(eventName, new Consumer());
    consumersByEventName.get(eventName).add(eventName, consumer);
  }

  public void unregister(final Object consumer) {
    unregistered.add(consumer);
  }

  public void publish(final Event event) {
    removeUnregistered();
    final Consumer consumer = consumersByEventName.get(event.name());
    if (consumer == null) {
      if (debug) {
        //noinspection UseOfSystemOutOrSystemErr
        System.err.println(event);
      }
    } else {
      if (debug) {
        //noinspection UseOfSystemOutOrSystemErr
        System.out.println(event);
      }
      consumer.publish(event);
    }
  }

  private void removeUnregistered() {
    if (unregistered.size() == 0) return;
    for (Consumer c : consumersByEventName.values()) {
      for (Iterator<WeakReference<Object>> iterator = c.consumerRefs.iterator(); iterator.hasNext();) {
        if (unregistered.contains(iterator.next().get())) iterator.remove();
      }
    }
    unregistered.clear();
  }

  @Override public String toString() {
    return "BusImpl{prefix='" + prefix + '\'' + ",consumersByEventName=" + consumersByEventName +
           ",unregistered=" + unregistered + ",eventMethod=" + eventMethod + '}';
  }

  private class Consumer {
    private final List<WeakReference<Object>> consumerRefs = new ArrayList<WeakReference<Object>>();
    private final Map<String, Method> methodsByClassName = new TreeMap<String, Method>();

    private void add(final String event, final Object consumer) {
      try {
        consumerRefs.add(new WeakReference<Object>(consumer));
        final Class clazz = consumer.getClass();
        final String classname = clazz.getName();
        if (methodsByClassName.containsKey(classname)) return;
        final StringBuilder name = new StringBuilder(prefix.length() + event.length()).append(prefix).append(event);
        name.setCharAt(prefix.length(), Character.toUpperCase(event.charAt(0)));
        final Method method = consumer.getClass().getMethod(name.toString(), Event.class);
        methodsByClassName.put(classname, method);
      } catch (NoSuchMethodException e) {
        throw new IllegalStateException(e);
      }
    }

    private void publish(final Event event) {
      log().debug(event);
      for (WeakReference<Object> consumerRef : consumerRefs) {
        final String classname = consumerRef.get().getClass().getName();
        final Method method = methodsByClassName.get(classname);
        final Object consumer = consumerRef.get();
        try {
          method.invoke(consumer, event);
        } catch (IllegalAccessException e) {
          log().error(new StringBuilder().append("unable to access ").append(method).toString(), e);
        } catch (InvocationTargetException e) {
          log().error(new StringBuilder().append("unable to invoke ").append(method).toString(), e);
        }
      }
    }

    @Override public String toString() {
      return "Consumer{consumerRefs=" + consumerRefs + ",methodsByClassName=" + methodsByClassName + '}';
    }

    private Log log() {
      return LogFactory.getLog(Consumer.class);
    }
  }
}
