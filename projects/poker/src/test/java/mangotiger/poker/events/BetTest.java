/*
 * $Source: $
 *
 * Copyright (C) 2002-2005, Thomas Robert Gagnier, Jr., All Rights Reserved.
 * Unauthorized use, disclosure or reproduction of this source code is strictly
 * prohibited by United States copyright law and international treaty provisions.
 * Use of source code requires an appropriate source license.
 */
package mangotiger.poker.events;

import junit.framework.TestCase;

/** @author Tom Gagnier */
@SuppressWarnings({"ClassWithoutToString"})
public class BetTest extends TestCase {

  Bet bet;

  public void testEquals() throws Exception {
    assertTrue(new Bet("1", 1).equals(new Bet("1", 1)));
    assertFalse(new Bet("1", 1).equals(new Bet("1", 2)));
    assertFalse(new Bet("1", 1).equals(new Bet("2", 1)));
    assertFalse(new Bet("1", 1).equals(new Bet("2", 2)));
  }
}