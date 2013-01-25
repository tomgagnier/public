package mangotiger.lang;

/**
 * A collection of int helpers.
 * @author tom_gagnier@yahoo.com
 */
@SuppressWarnings({"MagicNumber"})
public final class Integers {
  /**
   * Convert a byte an unsigned integer.
   * @param b the byte to be converted.
   * @return A byte converted to an unsigned integer.
   */
  public static int toUnsigned(final byte b) {
    return (char)b & 0xff;
  }

  /**
   * Convert a char an unsigned integer.
   * @param c the char to be converted.
   * @return A char converted to an unsigned integer.
   */
  public static int toUnsigned(final char c) {
    return c;
  }

  /**
   * Convert a short an unsigned integer.
   * @param s the short to be converted.
   * @return A short converted to an unsigned integer.
   */
  public static int toUnsigned(final short s) {
    return (char)s;
  }

  private Integers() {}
}
