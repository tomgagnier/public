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
public class Files {
  public static final File[] EMPTY = new File[0];

  private Files() {}

  /** A better file list - it returns an empty array instead of null when no children exist. */
  public static File[] list(final File root, final FileFilter filter) {
    final File[] files = root.listFiles(filter);
    return files == null ? EMPTY : files;
  }

  /** A better file list - it returns an empty array instead of null when no children exist. */
  public static File[] list(final File root) {
    final File[] files = root.listFiles();
    return files == null ? EMPTY : files;
  }

  public static File toFile(final String classpath, final Class clazz) {
    final String pckage = clazz.getPackage().getName().replace('.', '/');
    return new File(classpath, pckage);
  }

}
