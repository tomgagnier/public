package mangotiger.util;

public class Arrays {
  private Arrays() {}

  public static String toString(final char[] array) {
    return toString(new ArrayStringizer() {
      public void append(final StringBuffer buffer, final int index) {buffer.append(array[index]);}

      public int size() {return array.length;}
    });
  }

  public static String toString(final int[] array) {
    return toString(new ArrayStringizer() {
      public void append(final StringBuffer buffer, final int index) {buffer.append(array[index]);}

      public int size() {return array.length;}
    });
  }

  public static String toString(final long[] array) {
    return toString(new ArrayStringizer() {
      public void append(final StringBuffer buffer, final int index) {buffer.append(array[index]);}

      public int size() {return array.length;}
    });
  }

  public static String toString(final double[] array) {
    return toString(new ArrayStringizer() {
      public void append(final StringBuffer buffer, final int index) {buffer.append(array[index]);}

      public int size() {return array.length;}
    });
  }

  private interface ArrayStringizer {
    void append(StringBuffer buffer, int index);

    int size();
  }

  private static String toString(final ArrayStringizer arrayStringizer) {
    final StringBuffer buffer = new StringBuffer().append('[');
    for (int i = 0; i < arrayStringizer.size(); ++i) {
      if (i != 0) buffer.append(',');
      arrayStringizer.append(buffer, i);
    }
    return buffer.append(']').toString();
  }
}
