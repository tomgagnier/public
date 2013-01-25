package mangotiger.poker.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import mangotiger.poker.events.Ante;
import mangotiger.poker.events.Bankroll;
import mangotiger.poker.events.Bet;
import mangotiger.poker.events.BigBlind;
import mangotiger.poker.events.Call;
import mangotiger.poker.events.Collect;
import mangotiger.poker.events.GameEnd;
import mangotiger.poker.events.PlayerAmount;
import mangotiger.poker.events.Raise;
import mangotiger.poker.events.SmallBlind;
import mangotiger.poker.events.NewGame;

/** @author tom.gagnier@acs-inc.com */
@SuppressWarnings({"PublicMethodNotExposedInInterface"})
public class Pot {

  private final Map<String, Integer> contributions = new TreeMap<String, Integer>();
  private final Map<String, Integer> payouts = new TreeMap<String, Integer>();
  private final Map<String, Integer> bankrolls = new TreeMap<String, Integer>();
  private int posts;

  /**
   * The ratio of a player's bankroll to the sum of the blinds and antes. In other words, how many rounds can the player
   * survive without making a bet.
   */
  public float m(final String player) {
    return 1.0F * bankroll(player) / posts;
  }

  public void on(final NewGame event) {
    posts = 0;
    contributions.clear();
    payouts.clear();
    bankrolls.clear();
  }

  public void on(final Collect event) {
    final String player = event.getPlayer();
    payouts.put(player, payouts.get(player) + event.getAmount());
  }

  public void on(final Call event) {
    final String player = event.getPlayer();
    contributions.put(player, contributions.get(player) + event.getAmount());
  }

  public void on(final Raise event) {
    contributions.put(event.getPlayer(), maxContribution() + event.getAmount());
  }

  private int maxContribution() {
    int maxContribution = 0;
    for (int contribution : contributions.values()) {
      maxContribution = Math.max(maxContribution, contribution);
    }
    return maxContribution;
  }

  public void on(final Bet event) {
    contribute(event);
  }

  private int contribute(final PlayerAmount event) {
    final String player = event.getPlayer();
    final int amount = event.getAmount();
    contributions.put(player, contributions.get(player) + amount);
    return amount;
  }

  public void on(final BigBlind event) {
    posts += contribute(event);
  }

  public void on(final SmallBlind event) {
    posts += contribute(event);
  }

  public void on(final Ante event) {
    posts += contribute(event);
  }

  public void on(final Bankroll event) {
    final String player = event.getPlayer();
    bankrolls.put(player, event.getAmount());
    contributions.put(player, 0);
    payouts.put(player, 0);
  }

  public void on(final GameEnd event) {
    returnUncalledBetOrRaise();
    adjustBankrolls();
    final int total = getTotal();
    if (total != 0) {
      throw new IllegalStateException("pot should be zero, not " + total);
    }
  }

  void returnUncalledBetOrRaise() {
    if (contributions.size() < 2) {
      contributions.clear();
    } else {
      final List<String> players = playersSortedByContribution();
      final String player = players.get(0);
      final int refund = contributions.get(player) - contributions.get(players.get(1));
      contributions.put(player, contributions.get(player) - refund);
    }
  }

  private List<String> playersSortedByContribution() {
    final List<String> players = new ArrayList<String>(contributions.keySet());
    for (int i = 0; i < players.size() - 1; ++i) {
      for (int j = i + 1; j < players.size(); ++j) {
        final String first = players.get(i);
        final String second = players.get(j);
        if (contributions.get(second) > contributions.get(first)) {
          players.set(j, first);
          players.set(i, second);
        }
      }
    }
    return players;
  }

  private void adjustBankrolls() {
    for (String player : bankrolls.keySet()) {
      bankrolls.put(player, bankroll(player) - contributions.get(player) + payouts.get(player));
    }
  }

  public int getTotal() {
    final int totalContributions = sum(contributions.values());
    final int totalPayouts = sum(payouts.values());
    return totalContributions - totalPayouts;
  }

  /** The ratio of a player bankroll to the average stack size. */
  public float q(final String player) {
    return 1.0F * bankroll(player) / sum(bankrolls.values());
  }

  public Integer bankroll(final String player) {
    return bankrolls.get(player);
  }

  private static int sum(final Collection<Integer> integers) {
    int total = 0;
    for (int contribution : integers) {
      total += contribution;
    }
    return total;
  }

  @Override public String toString() {
    return "Pot" +
           "{posts=" + posts +
           ",bankrolls=" + bankrolls +
           ",contributions=" + contributions +
           ",payouts=" + payouts +
           '}';
  }
}
