/*
 * $Source: $
 *
 * Copyright (C) 2002-2005, Thomas Robert Gagnier, Jr., All Rights Reserved.
 * Unauthorized use, disclosure or reproduction of this source code is strictly
 * prohibited by United States copyright law and international treaty provisions.
 * Use of source code requires an appropriate source license.
 */
package mangotiger.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import junit.framework.TestCase;

public final class InputStreamsTest extends TestCase {
  private static final int DEMON_LOVER = 23;

  public void testToString() throws IOException {
    final InputStream is = getClass().getResourceAsStream("quotations.txt");
    assertNotNull(is);
    final String actual = InputStreams.toString(is);
    final String expect = "The best minds are not in government. If any were, business would hire them away. - Ronald Reagan";
    assertEquals(expect, actual);
  }

  public void testToLines() throws IOException {
    final InputStream is = getClass().getResourceAsStream("kubla-khan.txt");
    assertNotNull(is);
    final List<String> lines = InputStreams.toLines(is, "UTF-8");
    assertEquals("By woman wailing for her demon-lover!", lines.get(DEMON_LOVER));
    assertEquals("And drunk the milk of Paradise.", lines.get(lines.size() - 1));
  }
}
