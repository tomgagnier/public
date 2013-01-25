/*
 * $Source: $
 *
 * Copyright (C) 2002-2005, Thomas Robert Gagnier, Jr., All Rights Reserved.
 * Unauthorized use, disclosure or reproduction of this source code is strictly
 * prohibited by United States copyright law and international treaty provisions.
 * Use of source code requires an appropriate source license.
 */
package mangotiger.poker.channel;

import static org.easymock.EasyMock.*;

import junit.framework.TestCase;
import mangotiger.poker.EventChannel;
import mangotiger.poker.events.Bet;
import mangotiger.poker.events.PlayerSeated;
import mangotiger.poker.events.Type;

/** @author Tom Gagnier */
@SuppressWarnings({"ClassWithoutToString", "MagicNumber"})
public class EventParserImplTest extends TestCase {

  EventChannel channel = createMock(EventChannel.class);
  EventParserImpl parser = new EventParserImpl(channel);

  public void testBet() {
    final Bet event = new Bet("Big Guy77", 150);
    channel.publish(event);
    replay(channel);
    parser.add(Bet.class, "([^:]+): bet ([^ ]+).*", Type.player, Type.amount);
    parser.parse("Big Guy77: bet 150");
    verify(channel);
  }

  public void testSeat() {
    final PlayerSeated event = new PlayerSeated("Big Guy77", 9);
    channel.publish(event);
    replay(channel);
    parser.add(PlayerSeated.class,
                    "Seat ([0-9]+): (.*) \\(([0-9]+) in chips\\) *(is sitting (out))?",
                    Type.seat, Type.player, Type.amount, Type.description, Type.status);
    parser.parse("Seat 9: Big Guy77 (1500 in chips)");
    verify(channel);
  }
}
