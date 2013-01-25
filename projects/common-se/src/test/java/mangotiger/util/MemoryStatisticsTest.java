package mangotiger.util;

import junit.framework.TestCase;

/**
 * Test MemoryStatistics.
 * @author tom_gagnier@yahoo.com
 */
public final class MemoryStatisticsTest extends TestCase {

  /** Test instance(). */
  public void testInstance() {
    final MemoryStatistics memoryStatistics = MemoryStatistics.instance();
    System.out.println(memoryStatistics);
  }
}