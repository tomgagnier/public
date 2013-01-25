/*
 * $Source: $
 *
 * Copyright (C) 2002-2005, Thomas Robert Gagnier, Jr., All Rights Reserved.
 * Unauthorized use, disclosure or reproduction of this source code is strictly
 * prohibited by United States copyright law and international treaty provisions.
 * Use of source code requires an appropriate source license.
 */
package mangotiger.poker;

import mangotiger.poker.events.NewGame;

import java.io.File;
import java.util.Iterator;

/** @author Tom Gagnier */
public interface Casino {

  EventParser newEventParser(EventChannel channel);

  Iterator<File> files(long since);

  Iterator<File> files();

  void setTime(long systemTimeMillis);

  Game newGame(NewGame event);

  void addMatchers(EventParser eventParser);
}
