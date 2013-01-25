package mangotiger.lang;

/**
 * A collection of helpers for char.
 * @author tom_gagnier@yahoo.com
 */
public final class Characters {
  private Characters() {}

  /**
   * Reverse the endian order of a given character.
   * @param c The character to reverseEndian.
   * @return The byte reversed character.
   */
  public static char reverseEndian(final char c) {
    return (char)((c & Bytes.MASKS[0]) << Bytes.SHIFTS[1] | (c & Bytes.MASKS[1]) >> Bytes.SHIFTS[1]);
  }
}
