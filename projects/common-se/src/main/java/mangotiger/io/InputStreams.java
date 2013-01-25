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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * A collection of helper methods for an InputStream.
 * @author tom_gagnier@yahoo.com
 */
public final class InputStreams {
  private static final int BUFFER_SIZE = 1024;

  private InputStreams() {
    // Intentionally empty.
  }

  interface Reader {
    char[] getCharBuffer();

    void read(char[] bytes, int charsRead);
  }

  private static void read(final InputStream is, final Reader reader) throws IOException {
    final char[] chars = reader.getCharBuffer();
    final InputStreamReader isr = new InputStreamReader(is);
    for (int charsRead = isr.read(chars); charsRead != -1; charsRead = isr.read(chars)) {
      reader.read(chars, charsRead);
    }
  }

  /**
   * Convert the contents of an InputStream to a String.
   * @param inputStream the InputStream.
   * @return the contents of the inputStream.
   */
  public static String toString(final InputStream inputStream) throws IOException {
    final StringBuffer stringBuffer = new StringBuffer();
    final char[] chars = new char[BUFFER_SIZE];
    final Reader reader = new Reader() {
      public char[] getCharBuffer() {
        return chars;
      }

      public void read(final char[] chars, final int charsRead) {
        stringBuffer.append(chars, 0, charsRead);
      }
    };
    read(inputStream, reader);
    return stringBuffer.toString();
  }

  /**
   * Convert an input stream to an array of lines.
   * @param is       the input stream to convert
   * @param encoding the encodeing of the input stream
   * @return an array of lines
   */
  public static List<String> toLines(final InputStream is, final String encoding) throws IOException {
    return toLines(new InputStreamReader(is, encoding));
  }

  /**
   * Convert an input stream to an array of lines.
   * @param is the input stream to convert
   * @return an array of lines
   */
  public static List<String> toLines(final InputStream is) throws IOException {
    return toLines(new InputStreamReader(is));
  }

  private static List<String> toLines(final InputStreamReader in) throws IOException {
    final List<String> lines = new LinkedList<String>();
    final BufferedReader reader = new BufferedReader(in);
    for (String line = reader.readLine(); line != null; line = reader.readLine()) {
      lines.add(line);
    }
    return new ArrayList<String>(lines);
  }

  /**
   * Convert a UTF-16 encoded stream to an array of lines.
   * @param utf16Stream the sream to convert.
   * @return an array of lines
   */
  public static List<String> utf16InputStreamToLines(final InputStream utf16Stream) throws IOException {
    return toLines(utf16Stream, "UTF-16");
  }
}