/*
 * $Source: $
 *
 * Copyright (C) 2002-2005, Thomas Robert Gagnier, Jr., All Rights Reserved.
 * Unauthorized use, disclosure or reproduction of this source code is strictly
 * prohibited by United States copyright law and international treaty provisions.
 * Use of source code requires an appropriate source license.
 */
package mangotiger.poker.channel;

import org.apache.commons.logging.LogFactory;

/** @author Tom Gagnier */
@SuppressWarnings({"PublicMethodNotExposedInInterface"})
public class EventLogger {

  public void on(final Object object) {
    LogFactory.getLog(EventLogger.class).info(object);
  }

  @Override public String toString() {
    return "EventLogger{}";
  }
}
