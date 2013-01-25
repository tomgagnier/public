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
@SuppressWarnings({"ClassWithoutToString"})
public final class Bet extends PlayerAmount {

  @Parameters({Type.player, Type.amount})
  public Bet(final String player, final int amount) {
    super(player, amount);
  }
}
