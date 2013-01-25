/*
 * $Source: $
 *
 * Copyright (C) 2002-2005, Thomas Robert Gagnier, Jr., All Rights Reserved.
 * Unauthorized use, disclosure or reproduction of this source code is strictly
 * prohibited by United States copyright law and international treaty provisions.
 * Use of source code requires an appropriate source license.
 */
package mangotiger.io;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * A chained list of file filters.
 * @author Tom Gagnier
 */
public class ChainedFileFilter implements FileFilter {
  final List<FileFilter> filters;

  public ChainedFileFilter(final FileFilter ...filters) {
    this.filters = new ArrayList<FileFilter>(filters.length);
    for (FileFilter filter : filters) {
      this.filters.add(filter);
    }
  }

  public boolean accept(final File pathname) {
    for (FileFilter fileFilter : filters) {
      if (!fileFilter.accept(pathname)) return false;
    }
    return true;
  }
}
