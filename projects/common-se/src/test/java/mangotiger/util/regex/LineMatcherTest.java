/*
 * $Source: $
 *
 * Copyright (C) 2002-2005, Thomas Robert Gagnier, Jr., All Rights Reserved.
 * Unauthorized use, disclosure or reproduction of this source code is strictly
 * prohibited by United States copyright law and international treaty provisions.
 * Use of source code requires an appropriate source license.
 */
package mangotiger.util.regex;

import junit.framework.Assert;
import junit.framework.TestCase;

/** @author Tom Gagnier */
public class LineMatcherTest extends TestCase {
  public void testMatches() {
    final LineMatcher matcher = new LineMatcher("test", "one two", "([a-z]+) ([a-z]+)");
    assertFalse(matcher.matches("hello world!"));
    Assert.assertEquals("{}", matcher.attributes().toString());
    assertTrue(matcher.matches("hello world"));
    Assert.assertEquals("{one=hello, two=world}", matcher.attributes().toString());
  }
}