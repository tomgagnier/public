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
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/** @author Tom Gagnier */
public class FileSet implements Set<File> {
  private final Set<File> files;
  private long lastModified;

  public FileSet() {
    files = new TreeSet<File>();
  }

  public FileSet(final Collection<File> collection) {
    files = new TreeSet<File>(collection);
  }

  public FileSet(final Iterator<File> iterator) {
    files = new TreeSet<File>();
    while (iterator.hasNext()) files.add(iterator.next());
  }

  public void add(final Iterator<File> iterator) {
    while (iterator.hasNext()) {
      files.add(iterator.next());
    }
  }

  public void addRecursively(final File root, final FileFilter filter) {
    for (Iterator<File> i = new RecursiveFileIterator(root); i.hasNext();) {
      final File file = i.next();
      if (filter.accept(file)) {
        add(file);
        final long modified = file.lastModified();
        if (modified > lastModified) lastModified = modified;
      }
    }
  }

  public void addRecursively(final File root) {
    addRecursively(root, FileFilters.ACCEPT_ALL);
  }

  public final Set<File> modified() {
    final Set<File> changed = new TreeSet<File>();
    final long originalLastModified = lastModified;
    for (File file : files) {
      final long modified = file.lastModified();
      if (modified > originalLastModified) {
        changed.add(file);
        lastModified = file.lastModified();
      }
    }
    return changed;
  }

  public int size() {
    return files.size();
  }

  public boolean isEmpty() {
    return files.isEmpty();
  }

  public boolean contains(final Object o) {
    //noinspection SuspiciousMethodCalls
    return files.contains(o);
  }

  public Iterator<File> iterator() {
    return files.iterator();
  }

  public Object[] toArray() {
    return files.toArray();
  }

  public <T>T[] toArray(final T[] a) {
    return files.toArray(a);
  }

  public boolean add(final File file) {
    return files.add(file);
  }

  public boolean remove(final Object file) {
    //noinspection SuspiciousMethodCalls
    return files.remove(file);
  }

  public boolean containsAll(final Collection<?> c) {
    return files.containsAll(c);
  }

  public boolean addAll(final Collection<? extends File> c) {
    return files.addAll(c);
  }

  public boolean retainAll(final Collection<?> c) {
    return files.retainAll(c);
  }

  public boolean removeAll(final Collection<?> c) {
    return files.removeAll(c);
  }

  public void clear() {
    files.clear();
  }
}
