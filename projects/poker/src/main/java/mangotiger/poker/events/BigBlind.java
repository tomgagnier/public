package mangotiger.poker.events;

/** @author Tom Gagnier */
@SuppressWarnings({"ClassWithoutToString"})
public final class BigBlind extends PlayerAmount {

  @Parameters({Type.player, Type.amount})
  public BigBlind(final String player, final int amount) {
    super(player, amount);
  }
}
