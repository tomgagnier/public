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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * A recursive file iterator. This iterator lists all the root file and ALL of it's children.
 * @author Tom Gagnier
 */
public class RecursiveFileIterator implements Iterator<File> {
  private final List<File> files = new LinkedList<File>();
  private final FileFilter filter;

  public RecursiveFileIterator(final File root, final FileFilter filter) {
    this.filter = filter;
    add(root);
  }

  public RecursiveFileIterator(final File root) {
    this(root, FileFilters.ACCEPT_ALL);
  }

  public RecursiveFileIterator(final String root, final FileFilter filter) {
    this(new File(root), filter);
  }

  public RecursiveFileIterator(final String root) {
    this(new File(root), FileFilters.ACCEPT_ALL);
  }

  private File add(final File root) {
    for (final File file : Files.list(root)) {
      if (filter.accept(file)) {
        files.add(file);
      } else if (file.isDirectory()) {
        add(file);
      }
    }
    return root;
  }

  public boolean hasNext() {
    return !files.isEmpty();
  }

  public File next() {
    return add(files.remove(0));
  }

  public void remove() {
    throw new UnsupportedOperationException();
  }
}