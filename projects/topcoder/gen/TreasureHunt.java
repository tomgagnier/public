/** @author tom_gagnier@yahoo.com */
@SuppressWarnings({"ClassWithoutToString", "ClassWithoutPackageStatement"})
public class TreasureHunt {
  public int[] findTreasure( final String[] island, final  String[] instructions) {
    final TreasureHuntImpl impl = new TreasureHuntImpl(island, instructions);
    return impl.result();
  }
  static class TreasureHuntImpl {
    final String[] island;
    final String[] instructions;
    int[] result;

    TreasureHuntImpl(final String[] island, final String[] instructions) {
      this.island = island;
      this.instructions = instructions;
    }

    int[] result() {
      return result;
    }
}
}

