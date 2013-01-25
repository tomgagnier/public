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

/** @author Tom Gagnier */
public class FileFilters {
  private FileFilters() {}

  public static final FileFilter ACCEPT_ALL = new FileFilter() {
    public boolean accept(final File pathname) {
      return true;
    }
  };
  public static final FileFilter ACCEPT_DIRECTORY = new FileFilter() {
    public boolean accept(final File file) {
      return file.isDirectory();
    }
  };
  public static final FileFilter ACCEPT_FILES = new FileFilter() {
    public boolean accept(final File file) {
      return file.isFile();
    }
  };
}
