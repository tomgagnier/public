package mangotiger.poker;
/**
 * @author tom.gagnier@acs-inc.com
 */

import java.io.File;

import junit.framework.TestCase;
import mangotiger.poker.channel.EventChannelImpl;
import mangotiger.poker.game.TexasHoldem;
import mangotiger.poker.pokerstars.PokerStars;
import mangotiger.poker.statistics.Statistician;

@SuppressWarnings({"ClassWithoutToString", "PublicMethodNotExposedInInterface"})
public class AnalystTest extends TestCase {

  private static final File FILE = new File(
      "poker/src/test/mangotiger/poker/pokerstars/HH20050909 T12350072 No Limit Hold'em  $15 + $1.txt");
  final EventChannel channel = new EventChannelImpl();
  final Analyst analyst = new Analyst(channel, new PokerStars());
  final Adder adder = new Adder(channel);

  public void testParse() throws Exception {
    analyst.parse(FILE);
  }

  public static class Adder {

    Statistician statistician = new Statistician();

    public Adder(final EventChannel channel) {
      //noinspection ThisEscapedInObjectConstruction
      channel.subscribe(this);
    }

    public void on(TexasHoldem holdem) {
      statistician.add(holdem.statistican());
      System.out.println(statistician);
    }

  }
}