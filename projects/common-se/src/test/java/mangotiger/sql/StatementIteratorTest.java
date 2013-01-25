package mangotiger.sql;

/**
 * @author tom_gagnier@yahoo.com
 */

import java.io.InputStream;

import mangotiger.test.MTestCase;

/** Test StatementIterator. */
@SuppressWarnings({"ClassWithoutToString", "MagicNumber"})
public final class StatementIteratorTest extends MTestCase {
  /** Test single line input. */
  public void testSingleLine() {
    assertIteratorCount(29, newStatementIterator("single-line.sql"));
  }

  /** Test multi line input. */
  public void testMultiLine() {
    assertIteratorCount(17, newStatementIterator("multi-line.sql"));
  }

  private StatementIterator newStatementIterator(final String name) {
    final InputStream in = getClass().getResourceAsStream(name);
    assertTrue(in != null);
    return new StatementIterator(in);
  }

}