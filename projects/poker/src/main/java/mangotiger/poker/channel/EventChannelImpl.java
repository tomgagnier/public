/*
 * $Source: $
 *
 * Copyright (C) 2002-2005, Thomas Robert Gagnier, Jr., All Rights Reserved.
 * Unauthorized use, disclosure or reproduction of this source code is strictly
 * prohibited by United States copyright law and international treaty provisions.
 * Use of source code requires an appropriate source license.
 */
package mangotiger.poker.channel;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import mangotiger.poker.EventChannel;

/** @author Tom Gagnier */
public final class EventChannelImpl implements EventChannel {
  private static final String METHOD_NAME = "on";
  private final Set<WeakReference<Object>> subscribers = new HashSet<WeakReference<Object>>();
  private final Map<Class, HashMap<Class, Method>> methodsByEventBySubscriber =
      new HashMap<Class, HashMap<Class, Method>>();
  private final Queue<Object> eventQueue = new LinkedList<Object>();
  private final Set<WeakReference<Object>> subscriptions = new HashSet<WeakReference<Object>>();
  private final Set<Object> cancellations = new HashSet<Object>();

  public void cancel(final Object subscriber) {
    // removing the subscriber from the subscription list is superfluous - cancellations are done last
    cancellations.add(subscriber);
  }

  private static Log log() {
    return LogFactory.getLog(EventChannelImpl.class);
  }

  public void publish(final Object event) {
    if (event == null) return;
    eventQueue.add(event);
    if (eventQueue.size() == 1) {
      for (; eventQueue.size() > 0; eventQueue.remove()) {
        publishQueuedEvent(eventQueue.peek());
      }
    }
  }

  private void publishQueuedEvent(final Object event) {
    updateSubscribers();
    try {
      for (Iterator<WeakReference<Object>> iterator = subscribers.iterator(); iterator.hasNext();) {
        final WeakReference<Object> reference = iterator.next();
        final Object subscriber = reference.get();
        if (subscriber == null) {
          iterator.remove();
        } else {
          publish(event, subscriber);
        }
      }
    } catch (ConcurrentModificationException e) {
      log().error(e, e);
    }
  }

  private void updateSubscribers() {
    removeCancellations();
    addSubscribers();
  }

  private void removeCancellations() {
    for (Iterator<WeakReference<Object>> iterator = subscribers.iterator(); iterator.hasNext();) {
      final Object subscriber = iterator.next().get();
      if (subscriber == null) {
        iterator.remove();
      } else if (cancellations.contains(subscriber)) {
        iterator.remove();
      }
    }
    cancellations.clear();
  }

  private void addSubscribers() {
    subscribers.addAll(subscriptions);
    subscriptions.clear();
  }

  private void publish(final Object event, final Object subscriber) {
    final Method method = method(subscriber.getClass(), event.getClass());
    try {
      if (method != null) {
        method.invoke(subscriber, event);
      }
    } catch (Exception e) {
      throw new IllegalStateException("invoking " + method, e);
    }
  }

  private Method method(final Class<?> subscriber, final Class<?> event) {
    final Map<Class, Method> methodsByEvent = methodsByEvent(subscriber);
    if (!methodsByEvent.containsKey(event)) {
      final Method method = methodForEvent(subscriber, event);
      methodsByEvent.put(event, method);
    }
    return methodsByEvent.get(event);
  }

  private Map<Class, Method> methodsByEvent(final Class<?> subscriber) {
    if (!methodsByEventBySubscriber.containsKey(subscriber)) {
      methodsByEventBySubscriber.put(subscriber, new HashMap<Class, Method>());
    }
    return methodsByEventBySubscriber.get(subscriber);
  }

  static Method methodForEvent(final Class<?> subscriber, final Class<?> event) {
    for (Method method : subscriber.getMethods()) {
      if (methodMatchesEvent(method, event)) {
        return method;
      }
    }
    return null;
  }

  static boolean methodMatchesEvent(final Method method, final Class<?> event) {
    return METHOD_NAME.equals(method.getName()) &&
        Modifier.isPublic(method.getModifiers()) &&
        Void.TYPE.equals(method.getReturnType()) &&
        method.getParameterTypes().length == 1 &&
        method.getParameterTypes()[0].isAssignableFrom(event);
  }

  public void subscribe(final Object subscriber) {
    subscriptions.add(new WeakReference<Object>(subscriber));
    // remove subscriber from cancellation list to cater to a two directives given
    cancellations.remove(subscriber);
  }

  @Override public String toString() {
    return getClass().getSimpleName() + "{registered=" + subscribers + '}';
  }
}
