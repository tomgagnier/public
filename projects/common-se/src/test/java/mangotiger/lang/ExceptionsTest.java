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
public class ExceptionsTest extends TestCase {

  public void testRootCause() throws Exception {
    final Exception[] exceptions = new Exception[9];
    for (int i = 0; i < exceptions.length; ++i) {
      if (i == 0) {
        exceptions[i] = new Exception(Integer.toString(i));
      } else {
        exceptions[i] = new Exception(Integer.toString(i), exceptions[i - 1]);
        assertEquals(exceptions[0], Exceptions.rootCause(exceptions[i]));
      }
    }
  }
}