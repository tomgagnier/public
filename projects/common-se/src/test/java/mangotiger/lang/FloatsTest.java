package mangotiger.lang;

import junit.framework.TestCase;

/**
 * Test Floats.
 * @author tom_gagnier@yahoo.com
 */
@SuppressWarnings({"ClassWithoutToString", "MagicNumber"}) public final class FloatsTest extends TestCase {
  private static final double EPSILON = 0.000001;

  /** Test toArray(). */
  public static void testToArray() {
    final float[] floats = Floats.toArray("1.0;1.4;2.0", ";");
    assertEquals(1.0F, floats[0], EPSILON);
    assertEquals(1.4F, floats[1], EPSILON);
    assertEquals(2.0F, floats[2], EPSILON);
  }
}
