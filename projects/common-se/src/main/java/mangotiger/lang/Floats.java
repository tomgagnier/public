package mangotiger.lang;

/**
 * A collection Float utilities.
 * @author tom_gagnier@yahoo.com
 */
public final class Floats {
  /**
   * Convert a delimited set of floats to an array.
   * @param floatArray     a string representation of a float array.
   * @param regexDelimiter the delimiter represented by a regular expression.
   * @return a delimited set of floats to an array.
   */
  public static float[] toArray(final String floatArray, final String regexDelimiter) {
    final String[] strings = floatArray.split(regexDelimiter);
    final float[] floats = new float[strings.length];
    for (int i = 0; i < floats.length; ++i) {
      floats[i] = Float.parseFloat(strings[i]);
    }
    return floats;
  }

  private Floats() {
  }
}
