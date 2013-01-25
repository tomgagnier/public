package mangotiger.math;

import junit.framework.TestCase;

/**
 * Test Fibonacci.
 * @author tom_gagnier@yahoo.com
 */
public final class FibonacciTest extends TestCase {
  /** Test Fibonacci. */
  public static void testFibonacci() {
    final Fibonacci f = new Fibonacci();
    final long[] expect = {0, 1, 1, 2, 3, 5, 8, 13};
    for (long anExpect : expect) {
      final long n = f.next();
      assertEquals(anExpect, n);
      assertEquals(Long.toString(anExpect), f.toString());
    }
  }
}
