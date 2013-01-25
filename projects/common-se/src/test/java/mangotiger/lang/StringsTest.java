/*
 * $Source: $
 *
 * Copyright (C) 2002-2005, Thomas Robert Gagnier, Jr., All Rights Reserved.
 * Unauthorized use, disclosure or reproduction of this source code is strictly
 * prohibited by United States copyright law and international treaty provisions.
 * Use of source code requires an appropriate source license.
 */
package mangotiger.lang;

import junit.framework.TestCase;

/** @author Tom Gagnier */
public class StringsTest extends TestCase {
  public void testSplit() {
    assertSplit("[]", null);
    assertSplit("[]", "");
    assertSplit("[hello]", "hello");
    assertSplit("[hello, world!]", "hello world! ");
  }

  private static void assertSplit(final String expected, final String input) {
    assertEquals(expected, Strings.split(input, " +").toString());
  }

  public void testLast() {
    assertEquals("lastName", Strings.last("my.primaryName.lastName"));
    assertEquals("lastName", Strings.last("lastName"));
    assertEquals("lastName", Strings.last("primaryName.lastName"));
    assertEquals("lastName", Strings.last(".lastName"));
  }

  public void testTruncate() {
    assertEquals("", Strings.truncate("1234567890", 0));
    assertEquals("1", Strings.truncate("1234567890", 1));
    assertEquals("12", Strings.truncate("1234567890", 2));
    assertEquals("123", Strings.truncate("1234567890", 3));
    assertEquals("123456789", Strings.truncate("1234567890", 9));
    assertEquals("1234567890", Strings.truncate("1234567890", 10));
  }

  public void testFirst() {
    assertEquals("my", Strings.first("my.primaryName.lastName"));
    assertEquals("lastName", Strings.first("lastName"));
    assertEquals("primaryName", Strings.first("primaryName.lastName"));
    assertEquals("", Strings.first(".lastName"));
  }

  public void testDecapitalize() {
    assertEquals("helloWorld", Strings.decapitalize("helloWorld"));
    assertEquals("helloWorld", Strings.decapitalize("HelloWorld"));
    assertEquals("hElloWorld", Strings.decapitalize("HElloWorld"));
    assertNull(Strings.decapitalize(null));
    assertEquals("", Strings.decapitalize(""));
  }

  public void testCapitalize() {
    assertEquals("HelloWorld", Strings.capitalize("helloWorld"));
    assertEquals("HelloWorld", Strings.capitalize("HelloWorld"));
    assertEquals("HElloWorld", Strings.capitalize("hElloWorld"));
    assertNull(Strings.decapitalize(null));
    assertEquals("", Strings.capitalize(""));
  }
}