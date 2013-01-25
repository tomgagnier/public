package mangotiger.poker.game;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import junit.framework.TestCase;
import mangotiger.poker.events.Ante;
import mangotiger.poker.events.Bankroll;
import mangotiger.poker.events.Bet;
import mangotiger.poker.events.BigBlind;
import mangotiger.poker.events.Call;
import mangotiger.poker.events.Collect;
import mangotiger.poker.events.GameEnd;
import mangotiger.poker.events.Raise;
import mangotiger.poker.events.SmallBlind;

/** @author tom.gagnier@acs-inc.com */
@SuppressWarnings({"MagicNumber", "ClassWithoutToString", "FeatureEnvy"})
public class PotTest extends TestCase {

  static final String a = "a";
  static final String b = "b";
  static final String c = "c";
  Pot pot = new Pot();

  private static Log log() {
    return LogFactory.getLog(PotTest.class);
  }

  @Override protected void setUp() throws Exception {
    super.setUp();
    log().info(pot);
    pot.on(new Bankroll(a, 1000));
    pot.on(new Bankroll(b, 2000));
    pot.on(new Bankroll(c, 4000));
  }

  public void testRaise() {
    pot.on(new SmallBlind(a, 50));
    log().info(pot);
    pot.on(new BigBlind(b, 100));
    log().info(pot);
    pot.on(new Raise(c, 100));
    log().info(pot);
    pot.on(new Collect(c, 250));
    assertPotValid();
    assertEquals(950.0F / 150, pot.m(a));
    assertEquals(1900.0F / 150, pot.m(b));
    assertEquals(4150.0F / 150, pot.m(c));
    assertEquals(950.0F / (950.0F + 1900.0F + 4150.0F), pot.q(a));
    assertEquals(1900.0F / (950.0F + 1900.0F + 4150.0F), pot.q(b));
    assertEquals(4150.0F / (950.0F + 1900.0F + 4150.0F), pot.q(c));
  }

  public void testReRaise() {
    pot.on(new SmallBlind(a, 50));
    log().info(pot);
    pot.on(new BigBlind(b, 100));
    log().info(pot);
    pot.on(new Raise(c, 100));
    log().info(pot);
    pot.on(new Raise(a, 100));
    log().info(pot);
    pot.on(new Collect(c, 500));
    assertPotValid();
  }

  public void testSimple() throws Exception {
    pot.on(new Ante(a, 25));
    log().info(pot);
    pot.on(new Ante(b, 25));
    log().info(pot);
    pot.on(new Ante(c, 25));
    log().info(pot);
    pot.on(new SmallBlind(a, 50));
    log().info(pot);
    pot.on(new BigBlind(b, 100));
    log().info(pot);
    pot.on(new Call(c, 100));
    log().info(pot);
    pot.on(new Call(a, 50));
    log().info(pot);
    pot.on(new Bet(a, 100));
    log().info(pot);
    pot.on(new Call(b, 100));
    log().info(pot);
    pot.on(new Call(c, 100));
    log().info(pot);
    pot.on(new Collect(a, 675));
    assertPotValid();
  }

  public void testZeroEntrants() {
    assertPotValid();
  }

  private void assertPotValid() {
    // The pot resets itself on a game end event, regardless of consistency.
    // It logs an illegal state, but does not throw an exception.
    // So we check here, in the test.
    log().info(pot);
    pot.returnUncalledBetOrRaise();
    log().info(pot);
    assertEquals(0, pot.getTotal());
    pot.on(new GameEnd());
    log().info(pot);
    assertEquals(0, pot.getTotal());
  }
}