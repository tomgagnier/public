package mangotiger.test;

import java.nio.ByteBuffer;
import java.util.Iterator;

import junit.framework.TestCase;
import mangotiger.lang.Bytes;
import mangotiger.nio.ByteBuffers;

/**
 * This class adds useful assert methods to the JUnit framework.
 * @author tom_gagnier@yahoo.com
 */
@SuppressWarnings({"MethodOverridesStaticMethodOfSuperclass"})
public abstract class MTestCase extends TestCase {
  /**
   * Determine the two arguments are equal.
   * @param expect The expected value.
   * @param actual The actual value.
   */
  protected static void assertEquals(final byte[] expect, final byte[] actual) {
    assertEquals(Bytes.toHex(expect), Bytes.toHex(actual));
  }

  /**
   * Determine the two arguments are equal.
   * @param expect The expected value.
   * @param actual The actual value.
   */
  protected static void assertEquals(final ByteBuffer expect, final ByteBuffer actual) {
    final String expectString = ByteBuffers.describe(expect);
    final String actualString = ByteBuffers.describe(actual);
    final StringBuffer message = new StringBuffer();
    message.append("\nexpect\n").append(expectString);
    message.append("\nactual\n").append(actualString);
    message.append('\n');
    assertEquals(message.toString(), expectString, actualString);
  }

  /**
   * Determine the two arguments are equal.
   * @param expect The expected value already in hex format.
   * @param actual The actual value.
   */
  protected static void assertEquals(final String expect, final ByteBuffer actual) {
    assertEquals(actual.toString(), expect, ByteBuffers.toHex(actual));
  }

  /**
   * Assert that an iterator returns an expected count.
   * @param expect   the number of iterations expected
   * @param iterator the iterator to test
   */
  protected static void assertIteratorCount(final int expect, final Iterator iterator) {
    final StringBuffer diagnostic = new StringBuffer();
    int actual = 0;
    while (iterator.hasNext()) {
      diagnostic.append("\n\t").append(actual).append('\t');
      diagnostic.append(iterator.next());
      ++actual;
    }
    assertEquals(diagnostic.toString(), expect, actual);
  }
}
