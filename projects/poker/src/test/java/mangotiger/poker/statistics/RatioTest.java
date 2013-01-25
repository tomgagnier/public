/*
 * $Source: $
 *
 * Copyright (C) 2002-2005, Thomas Robert Gagnier, Jr., All Rights Reserved.
 * Unauthorized use, disclosure or reproduction of this source code is strictly
 * prohibited by United States copyright law and international treaty provisions.
 * Use of source code requires an appropriate source license.
 */
package mangotiger.poker.statistics;

import junit.framework.TestCase;

/** @author Tom Gagnier */
@SuppressWarnings({"ClassWithoutToString", "FeatureEnvy", "MagicNumber"})
public class RatioTest extends TestCase {

  final Ratio ratio = new Ratio("ratio");

  @Override protected void setUp() throws Exception {
    super.setUp();
    assertExpectedRatio("0/0");
  }

  public void testNo() throws Exception {
    ratio.no();
    assertExpectedRatio("0/1");
  }

  private void assertExpectedRatio(final String expected) {
    assertEquals(expected, ratio.toString());
  }

  public void testYes() throws Exception {
    ratio.yes();
    assertExpectedRatio("1/1");
  }

  public void testUpdate() throws Exception {
    ratio.update(false);
    assertExpectedRatio("0/1");
    ratio.update(true);
    assertExpectedRatio("1/2");
  }

  public void testToPercent() throws Exception {
    assertEquals(-1, ratio.toPercent());
    ratio.yes().no();
    assertEquals(50, ratio.toPercent());
  }

  public void testAdd() throws Exception {
    ratio.add(new Ratio("ratio"));
    assertExpectedRatio("0/0");
    ratio.add(new Ratio("ratio").yes());
    assertExpectedRatio("1/1");
    ratio.add(new Ratio("ratio").no());
    assertExpectedRatio("1/2");
    try {
      ratio.add(new Ratio("different").no());
      fail("expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertEquals("ratio names do not match ('ratio' != 'different')", e.getMessage());
    }
  }
}