package interview.matrix;

import static interview.matrix.Cell.*;
import static interview.matrix.Matrix.*;
import junit.framework.TestCase;

public class MatrixTest extends TestCase {

  public void testToString() throws Exception {
    assertEquals("", matrix().toString());

    assertEquals("  2 3\n4   6", matrix(cell(0, 1, 2), cell(0, 2, 3), cell(1, 0, 4), cell(1, 2, 6)).toString());
  }

  public void testEquals() throws Exception {
    assertEquals(matrix(), matrix());

    assertEquals(matrix(cell(0, 1, 2), cell(0, 2, 3), cell(1, 0, 4), cell(1, 2, 6)),
                 matrix(cell(0, 1, 2), cell(0, 2, 3), cell(1, 0, 4), cell(1, 2, 6)));
  }

  public void testBuilder() {
    assertEquals(matrix(cell(0, 0, 1), cell(0, 1, 2), cell(0, 2, 3)),
                 row(1, 2, 3));

    assertEquals(matrix(cell(0, 0, 1), cell(0, 1, 2), cell(0, 2, 3),
                        cell(1, 0, 4), cell(1, 1, 5), cell(1, 2, 6)),
                 row(1, 2, 3).add(row(4, 5, 6)));

  }

}
