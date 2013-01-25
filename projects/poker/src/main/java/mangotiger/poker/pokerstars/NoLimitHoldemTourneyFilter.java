/*
 * $Source: $
 *
 * Copyright (C) 2002-2005, Thomas Robert Gagnier, Jr., All Rights Reserved.
 * Unauthorized use, disclosure or reproduction of this source code is strictly
 * prohibited by United States copyright law and international treaty provisions.
 * Use of source code requires an appropriate source license.
 */
package mangotiger.poker.pokerstars;

import java.io.File;
import java.io.FileFilter;
import java.util.regex.Pattern;

/** @author Tom Gagnier */
public class NoLimitHoldemTourneyFilter implements FileFilter {

  final Pattern pattern = Pattern.compile("HH[0-9]{8} T[0-9]+ No Limit Hold'em.*");
  final long lastModified;

  public NoLimitHoldemTourneyFilter(final long lastModified) {
    this.lastModified = lastModified;
  }

  public NoLimitHoldemTourneyFilter() {
    this(0L);
  }

  public boolean accept(final File pathname) {
    return pathname.lastModified() > lastModified && pattern.matcher(pathname.getName()).matches();
  }

  @Override public String toString() {
    return "NoLimitHoldemTourneyFilter{pattern=" + pattern + ",lastModified=" + lastModified + '}';
  }
}
