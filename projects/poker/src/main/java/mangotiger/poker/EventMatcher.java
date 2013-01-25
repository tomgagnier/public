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
public interface EventMatcher {

  Object newEvent(String input);

  boolean isValid();

  /** The event name. */
  String getName();
}
