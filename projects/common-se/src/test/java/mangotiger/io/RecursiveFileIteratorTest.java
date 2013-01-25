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
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import junit.framework.TestCase;

/** @author Tom Gagnier */
@SuppressWarnings({"ClassWithoutToString"})
public class RecursiveFileIteratorTest extends TestCase {
  private static final File ROOT_DIR = new File(Files.toFile("common-se/src/test", RecursiveFileIteratorTest.class), "test");

  private static final File TEST_DIR = new File(ROOT_DIR, "test");
  private static final File THREE_FILE = new File(ROOT_DIR, "3.txt");
  private static final File ONE_FILE = new File(TEST_DIR, "1.txt");
  private static final File TWO_FILE = new File(TEST_DIR, "2.txt");

  public void testFileIteratorAcceptAll() {
    final Set<File> files = files(ROOT_DIR, FileFilters.ACCEPT_ALL);
    assertTrue(files.contains(TEST_DIR));
    assertTrue(files.contains(THREE_FILE));
    assertTrue(files.contains(ONE_FILE));
    assertTrue(files.contains(TWO_FILE));
    assertEquals(4, files.size());
  }

  public void testFileIteratorAcceptDirs() {
    final Set<File> files = files(ROOT_DIR, FileFilters.ACCEPT_DIRECTORY);
    assertTrue(files.contains(TEST_DIR));
    assertEquals(1, files.size());
  }

  public void testFileIteratorAcceptFiles() {
    final Set<File> files = files(ROOT_DIR, FileFilters.ACCEPT_FILES);
    assertTrue(files.contains(THREE_FILE));
    assertTrue(files.contains(ONE_FILE));
    assertTrue(files.contains(TWO_FILE));
    assertEquals(3, files.size());
  }

  private static Set<File> files(final File root, final FileFilter filter) {
    final Set<File> files = new TreeSet<File>();
    final StringBuffer info = new StringBuffer("Files:");
    final Iterator<File> iterator = new RecursiveFileIterator(root, filter);
    while (iterator.hasNext()) {
      final File file = iterator.next();
      info.append("\n\t").append(file);
      files.add(file);
    }
    log().info(info);
    return files;
  }

  private static Log log() {
    return LogFactory.getLog(RecursiveFileIteratorTest.class);
  }
}