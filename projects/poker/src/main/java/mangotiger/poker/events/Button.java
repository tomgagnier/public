/*
 * $Source: $
 *
 * Copyright (C) 2002-2005, Thomas Robert Gagnier, Jr., All Rights Reserved.
 * Unauthorized use, disclosure or reproduction of this source code is strictly
 * prohibited by United States copyright law and international treaty provisions.
 * Use of source code requires an appropriate source license.
 */
package mangotiger.poker.events;

/**
 * The initial bet in a round.
 * @author Tom Gagnier
 */
public final class Button implements Seat {

  private final int seat;

  @Parameters({Type.seat}) public Button(final int seat) {
    this.seat = seat;
  }

  public int getSeat() {
    return seat;
  }

  @Override public boolean equals(final Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    final Button that = (Button)obj;
    return seat == that.seat;
  }

  @Override public int hashCode() {
    return seat;
  }

  @Override public String toString() {
    return "Button{seat=" + seat + '}';
  }
}
