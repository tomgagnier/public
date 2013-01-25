package mangotiger.math;

/**
 * A Fibonacci number sequence.
 * @author tom_gagnier@yahoo.com
 */
final class Fibonacci {
  private long seq0 = 1;
  private long seq1;

  /**
   * The next number in the sequence.
   * @return The next number in the sequence.
   */
  public long next() {
    final long next = seq1;
    seq1 = seq0 + seq1;
    seq0 = next;
    return next;
  }

  @Override
  public String toString() {
    return Long.toString(seq0);
  }
}

