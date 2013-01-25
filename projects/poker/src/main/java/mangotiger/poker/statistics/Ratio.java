/*
 * $Source: $
 *
 * Copyright (C) 2002-2005, Thomas Robert Gagnier, Jr., All Rights Reserved.
 * Unauthorized use, disclosure or reproduction of this source code is strictly
 * prohibited by United States copyright law and international treaty provisions.
 * Use of source code requires an appropriate source license.
 */
package mangotiger.poker.statistics;

/** @author Tom Gagnier */
@SuppressWarnings({"PublicMethodNotExposedInInterface"}) public class Ratio {

  private static final float ONE_HUNDRED_PERCENT = 100.0F;

  private final String name;
  private int numerator;
  private int denominator;

  public Ratio(final String name) {
    this.name = name;
  }

  public void setNumerator(final int numerator) {
    this.numerator = numerator;
  }

  public void add(final Ratio ratio) {
    if (!name.equals(ratio.name)) {
      throw new IllegalArgumentException("ratio names do not match ('" + name + "' != '" + ratio.name + "')");
    }
    numerator += ratio.numerator;
    denominator += ratio.denominator;
  }

  public int toPercent() {
    return denominator == 0 ? -1 : Math.round(ONE_HUNDRED_PERCENT * numerator / denominator);
  }

  @Override public String toString() {
    return "" + numerator + '/' + denominator;
  }

  public Ratio update(final boolean accept) {
    return accept ? yes() : no();
  }

  public Ratio yes() {
    ++numerator;
    ++denominator;
    return this;
  }

  public Ratio no() {
    ++denominator;
    return this;
  }
}
