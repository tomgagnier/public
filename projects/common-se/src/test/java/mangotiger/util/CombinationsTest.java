package mangotiger.util;

import junit.framework.TestCase;

/** @author tom_gagnier@yahoo.com */
@SuppressWarnings({"ClassWithoutToString", "MagicNumber"})
public class CombinationsTest extends TestCase {
  public void testThreeElements() {
    final String[] expecteds = {"[]", "[1]", "[2]", "[1, 2]", "[3]", "[1, 3]", "[2, 3]", "[1, 2, 3]"};
    final Combinations<String> combinations = new Combinations<String>("1", "2", "3");
    assertExpectedCombinations(expecteds, combinations);
  }

  public void testFourElements() {
    final String[] expecteds = {"[]", "[1]", "[2]", "[1, 2]", "[3]", "[1, 3]", "[2, 3]", "[1, 2, 3]", "[4]", "[1, 4]",
                                "[2, 4]", "[1, 2, 4]", "[3, 4]", "[1, 3, 4]", "[2, 3, 4]", "[1, 2, 3, 4]"};
    final Combinations<String> combinations = new Combinations<String>("1", "2", "3", "4");
    assertExpectedCombinations(expecteds, combinations);
  }

  static void assertExpectedCombinations(final String[] expecteds, final Combinations combinations) {
    for (String expected : expecteds) {
      assertTrue(combinations.hasNext());
      assertEquals(expected, combinations.next().toString());
    }
  }

  public void testToString() throws Exception {
    final Combinations<String> combinations = new Combinations<String>("1", "2", "3", "4");
    assertEquals("Combinations{list=[1, 2, 3, 4],combination=[],rest=[],size=16,index=0}", combinations.toString());
    combinations.next();
    assertEquals("Combinations{list=[1, 2, 3, 4],combination=[],rest=[1, 2, 3, 4],size=16,index=1}",
                 combinations.toString());
  }

  public void testRemove() throws Exception {
    try {
      final Combinations<String> combinations = new Combinations<String>("1", "2");
      combinations.next();
      combinations.remove();
      fail();
    } catch (UnsupportedOperationException e) {
      assertEquals("remove is unsupported", e.getMessage());
    }
  }
}
