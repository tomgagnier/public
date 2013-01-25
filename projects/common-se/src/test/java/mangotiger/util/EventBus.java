/*
 * $Source: $
 *
 * Copyright (C) 2002-2005, Thomas Robert Gagnier, Jr., All Rights Reserved.
 * Unauthorized use, disclosure or reproduction of this source code is strictly
 * prohibited by United States copyright law and international treaty provisions.
 * Use of source code requires an appropriate source license.
 */
package mangotiger.util;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/** @author Tom Gagnier */
public class EventBus {
  private final Map<Class, List<Object>> consumersByEvent = new HashMap<Class, List<Object>>();
  private final Map<Class, HashMap<Class, Method>> methodsByConsumerByEvent
      = new HashMap<Class, HashMap<Class, Method>>();
  private final String method;

  public EventBus() {
    this("handle");
  }

  public EventBus(final String method) {
    this.method = method;
  }

  public void register(final Class event, final Object consumer) {
    registerMethod(event, consumer.getClass());
    registerConsumer(event, consumer);
  }

  private void registerMethod(final Class event, final Class consumer) {
    try {
      methodsByConsumer(event).put(consumer, consumer.getMethod(method, event));
    } catch (NoSuchMethodException e) {
      throw new IllegalArgumentException(e);
    }
  }

  private Map<Class, Method> methodsByConsumer(final Class event) {
    if (!methodsByConsumerByEvent.containsKey(event)) {
      methodsByConsumerByEvent.put(event, new HashMap<Class, Method>());
    }
    return methodsByConsumerByEvent.get(event);
  }

  private void registerConsumer(final Class event, final Object consumer) {
    if (!consumersByEvent.containsKey(event)) {
      consumersByEvent.put(event, new LinkedList<Object>());
    }
    consumersByEvent.get(event).add(consumer);
  }

  public void publish(final Object event) {
    if (event == null) return;
    if (!consumersByEvent.containsKey(event.getClass())) return;
    for (Iterator<Object> iterator = consumersByEvent.get(event.getClass()).iterator(); iterator.hasNext();) {
      final Object consumer = iterator.next();
      try {
        methodsByConsumerByEvent.get(event.getClass()).get(consumer.getClass()).invoke(consumer, event);
      } catch (Exception e) {
        log().error(new StringBuilder().append("unable to publish due to ").append(e)
            .append(", removing consumer: ").append(consumer)
            .append("), unable to handle bus: ").append(event));
        iterator.remove();
      }
    }
  }

  private static Log log() {
    return LogFactory.getLog(EventBus.class);
  }
}
