/** @author tom_gagnier@yahoo.com */
@SuppressWarnings({"ClassWithoutToString", "ClassWithoutPackageStatement"})
public class CheatCode {
  public int[] matches( final String keyPresses, final  String[] codes) {
    final CheatCodeImpl impl = new CheatCodeImpl(keyPresses, codes);
    return impl.result();
  }
  static class CheatCodeImpl {
    final String keyPresses;
    final String[] codes;
    int[] result;

    CheatCodeImpl(final String keyPresses, final String[] codes) {
      this.keyPresses = keyPresses;
      this.codes = codes;
    }

    int[] result() {
      return result;
    }
}
}

