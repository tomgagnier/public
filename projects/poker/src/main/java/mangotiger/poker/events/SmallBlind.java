package mangotiger.poker.events;

/** @author Tom Gagnier */
@SuppressWarnings({"ClassWithoutToString"})
public final class SmallBlind extends PlayerAmount {

  @Parameters({Type.player, Type.amount})
  public SmallBlind(final String player, final int amount) {
    super(player, amount);
  }
}
