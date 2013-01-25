package mangotiger.util;

import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;

import junit.framework.TestCase;

/** @author tom_gagnier@yahoo.com */
@SuppressWarnings({"ClassWithoutToString", "unchecked"})
public class PermutationsTest extends TestCase {
  public void test0() {
    final String[] expecteds = {"[1, 2, 3, 4]", "[1, 2, 4, 3]", "[1, 3, 2, 4]", "[1, 3, 4, 2]", "[1, 4, 2, 3]",
                                "[1, 4, 3, 2]", "[2, 1, 3, 4]", "[2, 1, 4, 3]", "[2, 3, 1, 4]", "[2, 3, 4, 1]",
                                "[2, 4, 1, 3]", "[2, 4, 3, 1]", "[3, 1, 2, 4]", "[3, 1, 4, 2]", "[3, 2, 1, 4]",
                                "[3, 2, 4, 1]", "[3, 4, 1, 2]", "[3, 4, 2, 1]", "[4, 1, 2, 3]", "[4, 1, 3, 2]",
                                "[4, 2, 1, 3]", "[4, 2, 3, 1]", "[4, 3, 1, 2]", "[4, 3, 2, 1]"};
    assertExpectedPermutations(expecteds, new Permutations<String>("1", "2", "3", "4"));
  }

  public void test1() {
    final String[] expecteds = {"[3, 4, 1, 2]", "[3, 4, 2, 1]", "[4, 1, 2, 3]", "[4, 1, 3, 2]", "[4, 2, 1, 3]",
                                "[4, 2, 3, 1]", "[4, 3, 1, 2]", "[4, 3, 2, 1]"};
    assertExpectedPermutations(expecteds, new Permutations<String>("3", "4", "1", "2"));
  }

  public void test2() {
    final String[] expecteds = {"[1, 2, 4, 5, 5]", "[1, 2, 5, 4, 5]", "[1, 2, 5, 5, 4]", "[1, 4, 2, 5, 5]",
                                "[1, 4, 5, 2, 5]", "[1, 4, 5, 5, 2]", "[1, 5, 2, 4, 5]", "[1, 5, 2, 5, 4]",
                                "[1, 5, 4, 2, 5]", "[1, 5, 4, 5, 2]", "[1, 5, 5, 2, 4]", "[1, 5, 5, 4, 2]",
                                "[2, 1, 4, 5, 5]", "[2, 1, 5, 4, 5]", "[2, 1, 5, 5, 4]", "[2, 4, 1, 5, 5]",
                                "[2, 4, 5, 1, 5]", "[2, 4, 5, 5, 1]", "[2, 5, 1, 4, 5]", "[2, 5, 1, 5, 4]",
                                "[2, 5, 4, 1, 5]", "[2, 5, 4, 5, 1]", "[2, 5, 5, 1, 4]", "[2, 5, 5, 4, 1]",
                                "[4, 1, 2, 5, 5]", "[4, 1, 5, 2, 5]", "[4, 1, 5, 5, 2]", "[4, 2, 1, 5, 5]",
                                "[4, 2, 5, 1, 5]", "[4, 2, 5, 5, 1]", "[4, 5, 1, 2, 5]", "[4, 5, 1, 5, 2]",
                                "[4, 5, 2, 1, 5]", "[4, 5, 2, 5, 1]", "[4, 5, 5, 1, 2]", "[4, 5, 5, 2, 1]",
                                "[5, 1, 2, 4, 5]", "[5, 1, 2, 5, 4]", "[5, 1, 4, 2, 5]", "[5, 1, 4, 5, 2]",
                                "[5, 1, 5, 2, 4]", "[5, 1, 5, 4, 2]", "[5, 2, 1, 4, 5]", "[5, 2, 1, 5, 4]",
                                "[5, 2, 4, 1, 5]", "[5, 2, 4, 5, 1]", "[5, 2, 5, 1, 4]", "[5, 2, 5, 4, 1]",
                                "[5, 4, 1, 2, 5]", "[5, 4, 1, 5, 2]", "[5, 4, 2, 1, 5]", "[5, 4, 2, 5, 1]",
                                "[5, 4, 5, 1, 2]", "[5, 4, 5, 2, 1]", "[5, 5, 1, 2, 4]", "[5, 5, 1, 4, 2]",
                                "[5, 5, 2, 1, 4]", "[5, 5, 2, 4, 1]", "[5, 5, 4, 1, 2]", "[5, 5, 4, 2, 1]"};
    assertExpectedPermutations(expecteds, new Permutations(1, 2, 4, 5, 5));
  }

  public void test3() {
    final String[] expecteds = {"[5, 4, 5, 1, 2]", "[5, 4, 5, 2, 1]", "[5, 5, 1, 2, 4]", "[5, 5, 1, 4, 2]",
                                "[5, 5, 2, 1, 4]", "[5, 5, 2, 4, 1]", "[5, 5, 4, 1, 2]", "[5, 5, 4, 2, 1]"};
    assertExpectedPermutations(expecteds, new Permutations(5, 4, 5, 1, 2));
  }

  public void test4() {
    final String[] expecteds = {"[5, 5]"};
    final Permutations permutations = new Permutations(5, 5);
    assertExpectedPermutations(expecteds, permutations);
  }

  private static void assertExpectedPermutations(final String[] expecteds, final Iterator<List<String>> i) {
    final List<String> actuals = new LinkedList<String>();
    for (String expected : expecteds) {
      assertTrue(actuals.toString(), i.hasNext());
      final String actual = i.next().toString();
      actuals.add(actual);
      assertEquals(actuals.toString(), expected, actual);
    }
    assertFalse(i.hasNext());
  }

  public void testRemove() throws Exception {
    try {
      final Permutations<Integer> permutations = new Permutations(1, 2, 4, 5, 5);
      permutations.remove();
      fail();
    } catch (Exception e) {
      // expected
    }
  }

  public void testToString() throws Exception {
    final Permutations<Integer> permutations = new Permutations(1, 2, 4, 5, 5);
    assertEquals("Permutations{count=0,list=[1, 2, 4, 5, 5]}", permutations.toString());
  }
}
