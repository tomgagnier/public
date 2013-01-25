package mangotiger.util;

/**
 * @author tom_gagnier@yahoo.com
 */

import java.util.Arrays;

import junit.framework.TestCase;

/** Test ConditionalIterator. */
public final class ConditionalIteratorTest extends TestCase {
  ConditionalIterator iterator;
  private static final Acceptor IF_EVEN = new Acceptor() {
    public boolean accept(final Object object) {
      try {
        return Integer.parseInt(object.toString()) % 2 == 0;
      } catch (NumberFormatException e) {
        return false;
      }
    }
  };

  /** Test ConditionalIterator. */
  public void testConditionalIterator() {
    final String[] strings = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    iterator = new ConditionalIterator(Arrays.asList(strings), IF_EVEN);
    assertTrue(iterator.hasNext());
    assertEquals("2", iterator.next().toString());
    assertTrue(iterator.hasNext());
    assertEquals("4", iterator.next().toString());
    assertTrue(iterator.hasNext());
    assertEquals("6", iterator.next().toString());
    assertTrue(iterator.hasNext());
    assertEquals("8", iterator.next().toString());
    assertTrue(iterator.hasNext());
    assertEquals("10", iterator.next().toString());
    assertFalse(iterator.hasNext());
  }
}