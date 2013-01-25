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
@SuppressWarnings({"ClassWithoutToString"})
public class ClassesTest extends TestCase {
  private static final String CLASS_NAME = "mangotiger.lang.ClassesTest$Foo";

  public void testForName() throws Exception {
    Classes.forName(CLASS_NAME);
    Classes.forName(CLASS_NAME, new Object[] {1});
    try {
      Classes.forName(CLASS_NAME, new Object[] {1,2});
      fail("expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // expected
    }
  }

  public static class Foo {
    final int i;
    public Foo() {i = 0;}
    public Foo(final Integer i) {this.i = i;}
  }
}