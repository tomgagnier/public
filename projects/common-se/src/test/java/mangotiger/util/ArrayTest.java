/*
 * $Source: $
 *
 * Copyright (C) 2002-2005, Thomas Robert Gagnier, Jr., All Rights Reserved.
 * Unauthorized use, disclosure or reproduction of this source code is strictly
 * prohibited by United States copyright law and international treaty provisions.
 * Use of source code requires an appropriate source license.
 */
package mangotiger.util;

import junit.framework.TestCase;

/** @author Tom Gagnier */
@SuppressWarnings({"ClassWithoutToString", "unchecked"})
public class ArrayTest extends TestCase {

  public void testAsList() throws Exception {
    assertEquals("[1, 2, 3]", new Array(1, 2, 3).toString());
    // Removed because of compiler warning.
    //    final Integer[] integers = null;
    //    assertEquals("[]", new Array().toString());
  }
}