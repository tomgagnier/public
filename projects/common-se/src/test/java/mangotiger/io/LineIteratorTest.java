/*
 * $Source: $
 *
 * Copyright (C) 2002-2005, Thomas Robert Gagnier, Jr., All Rights Reserved.
 * Unauthorized use, disclosure or reproduction of this source code is strictly
 * prohibited by United States copyright law and international treaty provisions.
 * Use of source code requires an appropriate source license.
 */
package mangotiger.io;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

/** @author Tom Gagnier */
public class LineIteratorTest extends TestCase {
  private static final int EXPECTED_LINES = 64;

  public void testLineIterator() throws Exception {
    final List<String> lines = new ArrayList<String>(EXPECTED_LINES);
    final InputStream is = getClass().getResourceAsStream("kubla-khan.txt");
    assertNotNull(is);
    for (LineIterator i = new LineIterator(is); i.hasNext();) {
      lines.add(i.next());
    }
    assertEquals(EXPECTED_LINES, lines.size());
    assertEquals("Kubla Khan", lines.get(0));
    assertEquals("And drunk the milk of Paradise.", lines.get(EXPECTED_LINES - 1));
  }
}