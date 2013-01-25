package interview.matrix;

import static interview.matrix.Cell.*;
import junit.framework.TestCase;

public class CellTest extends TestCase {

  public void testAdjacentTo() {
    assertTrue(cell(1, 1, 1).adjacentTo(cell(0, 0, 1)));
    assertTrue(cell(1, 1, 1).adjacentTo(cell(0, 1, 1)));
    assertTrue(cell(1, 1, 1).adjacentTo(cell(0, 2, 1)));
    assertTrue(cell(1, 1, 1).adjacentTo(cell(1, 0, 1)));
    assertTrue(cell(1, 1, 1).adjacentTo(cell(1, 1, 1)));
    assertTrue(cell(1, 1, 1).adjacentTo(cell(1, 2, 1)));

    assertFalse(cell(1, 1, 1).adjacentTo(cell(1, 3, 1)));
    assertFalse(cell(1, 1, 1).adjacentTo(cell(1, -1, 1)));
  }

  public void testCompareTo() {
    assertTrue(cell(0, 0, 0).compareTo(cell(0, 0, 0)) == 0);

    assertTrue(cell(0, 0, 0).compareTo(cell(0, 0, 1)) < 0);
    assertTrue(cell(0, 0, 0).compareTo(cell(0, 1, 0)) < 0);
    assertTrue(cell(0, 0, 0).compareTo(cell(1, 0, 0)) < 0);
  }

  public void testEquals() {
    assertTrue(cell(0, 0, 0).equals(cell(0, 0, 0)));

    assertFalse(cell(0, 0, 0).equals(cell(0, 0, 1)));
    assertFalse(cell(0, 0, 0).equals(cell(0, 1, 0)));
    assertFalse(cell(0, 0, 0).equals(cell(1, 0, 0)));
    assertFalse(cell(0, 0, 0).equals(new Object()));
  }

  public void testToString() {
    assertEquals("(0,1,2)", cell(0, 1, 2).toString());
  }

  public void testHashCode() {
    assertEquals(1 * 17 * 17 + 2 * 17 + 3, cell(1, 2, 3).hashCode());
  }

  public void test() throws Exception {
    assertTrue(withIntensityGreaterThan(4).evaluate(cell(1, 2, 5)));

    assertFalse(withIntensityGreaterThan(5).evaluate(cell(1, 2, 5)));
  }

}
