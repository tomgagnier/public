package mangotiger.util;

/**
 * The JVM memory statistics.
 * @author tom_gagnier@yahoo.com
 */
public final class MemoryStatistics {
  private static final MemoryStatistics instance = new MemoryStatistics();

  /**
   * An instance MemoryStatistics.
   * @return An instance MemoryStatistics.
   */
  public static MemoryStatistics instance() {
    return instance;
  }

  @Override public String toString() {
    final Runtime runtime = Runtime.getRuntime();
    final StringBuffer buf = new StringBuffer(getClass().getName());
    final long freeKb = runtime.freeMemory() >> 10;
    final long totalKb = runtime.totalMemory() >> 10;
    buf.append("{free=").append(freeKb);
    buf.append("Kb, used=").append(totalKb - freeKb);
    buf.append("Kb, total=").append(totalKb);
    buf.append("Kb}");
    return buf.toString();
  }
}
