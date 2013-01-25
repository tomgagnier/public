/*
 * $Source: $
 *
 * Copyright (C) 2002-2005, Thomas Robert Gagnier, Jr., All Rights Reserved.
 * Unauthorized use, disclosure or reproduction of this source code is strictly
 * prohibited by United States copyright law and international treaty provisions.
 * Use of source code requires an appropriate source license.
 */
package mangotiger.poker;

import mangotiger.poker.events.Type;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/** @author Tom Gagnier */
public interface EventParser {

  void add(Class<?> event, String regex, Type... groups);

  void parse(InputStream in) throws IOException;

  void parse(File file) throws IOException;
}
