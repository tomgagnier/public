package mangotiger.lang;

import junit.framework.TestCase;

/**
 * Test @see Integers.
 * @author tom_gagnier@yahoo.com
 */
@SuppressWarnings({"ClassWithoutToString", "MagicNumber"})
public final class IntegersTest extends TestCase {
  /** Test Integers.toUnsigned(). */
  public static void testToUnsigned() {
    assertEquals(128, Integers.toUnsigned((byte)-128));
    assertEquals(255, Integers.toUnsigned((byte)-1));
    assertEquals(0, Integers.toUnsigned((byte)0));
    assertEquals(1, Integers.toUnsigned((byte)1));
    assertEquals(127, Integers.toUnsigned((byte)127));
    assertEquals(32767, Integers.toUnsigned(Short.MAX_VALUE));
    assertEquals(32768, Integers.toUnsigned(Short.MIN_VALUE));
    assertEquals(65534, Integers.toUnsigned((short)-2));
    assertEquals(65535, Integers.toUnsigned((short)-1));
    assertEquals(0, Integers.toUnsigned((short)0));
    assertEquals(42, Integers.toUnsigned((char)42));
    assertEquals(65535, Integers.toUnsigned((char)-1));
  }
}
