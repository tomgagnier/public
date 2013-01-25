package mangotiger.topcoder;

import java.io.File;
import java.io.IOException;

/**
 * Prepares both a Solution and SolutionTest for a TopCoder competition.
 * @author tom_gagnier@yahoo.com
 */
public final class Main {
  @Override public String toString() {
    return "Main{}";
  }

  /**
   * Prepares both a Solution and SolutionTest for a TopCoder competition from a problem statement.
   * @param args - the TopCoder problem statement file and the output directory for the java files.
   * @throws IOException if unable to open file
   */
  public static void main(final String[] args) throws IOException {
    final File inputFile = new File(args[0]);
    final File outputDirectory = new File(args[1]);
    final Problem problemStatement = Parser.parse(inputFile);
    final Writer tw = new Writer(outputDirectory, problemStatement);
    tw.write();
  }
}
