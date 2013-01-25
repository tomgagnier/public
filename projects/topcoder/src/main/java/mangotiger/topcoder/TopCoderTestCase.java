package mangotiger.topcoder;

import java.util.Arrays;

import junit.framework.TestCase;

/** Adds missing JUnit assertions for the generated test cases. */
public abstract class TopCoderTestCase extends TestCase {
  private static final double EPSILON = 1.0E-6;

  protected static void assertEquals(final int[] expect, final int[] actual) {
    assertEquals(Arrays.toString(expect), Arrays.toString(actual));
  }

  protected final void assertEquals(final double expect, final double actual) {
    assertEquals(getEpsilon(), expect, actual);
  }

  /** The size of the neighborhood around a double for the purpose of an equality comparison. */
  protected static final double getEpsilon() {
    return EPSILON;
  }
}
