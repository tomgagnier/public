package mangotiger.lang;

import junit.framework.TestCase;

/**
 * Test of @see Characters.
 * @author tom_gagnier@yahoo.com
 */
@SuppressWarnings({"ClassWithoutToString"})
public final class CharactersTest extends TestCase {
  /** Test Characters.reverseEndian(). */
  public static void testReversEndian() {
    assertEquals('\ufeff', Characters.reverseEndian('\ufffe'));
  }
}
