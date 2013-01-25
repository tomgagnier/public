/*
 * $Source: $
 *
 * Copyright (C) 2002-2005, Thomas Robert Gagnier, Jr., All Rights Reserved.
 * Unauthorized use, disclosure or reproduction of this source code is strictly
 * prohibited by United States copyright law and international treaty provisions.
 * Use of source code requires an appropriate source license.
 */
package mangotiger.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

/**
 * Iterate through the lines of a file.
 * @author tom_gagnier@yahoo.com
 */
public final class LineIterator implements Iterator<String> {
  private final BufferedReader in;
  private String line;

  /**
   * Construct a line iterator from an input stream.
   * @param inputStream the line source
   */
  public LineIterator(final InputStream inputStream) throws IOException {
    final InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
    in = new BufferedReader(inputStreamReader);
    line = in.readLine();
  }

  /**
   * Construct a line from a file.
   * @param file the line source.
   */
  public LineIterator(final File file) throws IOException {
    this(new FileInputStream(file));
  }

  /** Remove is unsupported. */
  public void remove() {
    throw new UnsupportedOperationException();
  }

  /**
   * Determine if another line exists.
   * @return true if another line exists.
   */
  public boolean hasNext() {
    return line != null;
  }

  /**
   * The next line.
   * @return The next line.
   */
  public String next() {
    final String next = line;
    try {
      line = in.readLine();
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
    return next;
  }
}