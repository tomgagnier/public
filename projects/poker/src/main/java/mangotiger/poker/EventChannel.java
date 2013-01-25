/*
 * $Source: $
 *
 * Copyright (C) 2002-2005, Thomas Robert Gagnier, Jr., All Rights Reserved.
 * Unauthorized use, disclosure or reproduction of this source code is strictly
 * prohibited by United States copyright law and international treaty provisions.
 * Use of source code requires an appropriate source license.
 */
package mangotiger.poker;

/** @author Tom Gagnier */
public interface EventChannel {

  void publish(Object event);

  void subscribe(Object subscriber);

  void cancel(final Object subscriber);
}
