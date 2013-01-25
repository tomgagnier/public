/*
 * $Source: $
 *
 * Copyright (C) 2002-2005, Thomas Robert Gagnier, Jr., All Rights Reserved.
 * Unauthorized use, disclosure or reproduction of this source code is strictly
 * prohibited by United States copyright law and international treaty provisions.
 * Use of source code requires an appropriate source license.
 */
package mangotiger.poker.events;

/** @author Tom Gagnier */
@SuppressWarnings({"ClassWithoutToString"})
public final class River extends CardsImpl {

  @Parameters({Type.cards})
  public River(final String cards) {
    super(cards);
  }
}
