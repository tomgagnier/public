package mangotiger.topcoder;

import java.io.File;
import java.io.IOException;

/**
 * Parses the problem statement file to produce a ProblemStatement.
 * @author tom_gagnier@yahoo.com
 */
final class Parser {
  private static final String TEST_CASE = "^[0-9]\\)";
  private static final String DEFINITION = "^Definition";
  private static final String CLASS = "^Class:";
  private static final String METHOD = "^Method:";
  private static final String RETURNS = "^Returns:";
  private static final String SIGNATURE = "^Method signature:";
  private static final String NOTES = "^Notes|^Constraints";
  private static final String EXAMPLES = "^Examples";

  private Parser() {
    // Intentionally empty.
  }

  /**
   * Parses the problem statement file to produce a ProblemStatement.
   * @param problemStatementFile the original problem statement file.
   * @return the problem statement.
   * @throws IOException if unable to open file
   */
  public static Problem parse(final File problemStatementFile) throws IOException {
    final Lines lines = new Lines(problemStatementFile);
    final Problem problem = new Problem();
    problem.setStatement(lines.between(0, DEFINITION));
    problem.setClassname(lines.after(CLASS));
    problem.setMethodname(lines.after(METHOD));
    problem.setReturnType(lines.after(RETURNS));
    problem.setSignature(lines.after(SIGNATURE));
    final int index = lines.index() + 1;
    if (lines.find(NOTES) == lines.end()) {
      lines.setIndex(index);
    }
    problem.setNotes(lines.between(index, EXAMPLES));
    int begin = lines.find(TEST_CASE);
    while (begin != lines.end()) {
      lines.next();
      final int end = lines.find(TEST_CASE);
      final Lines exampleLines = lines.between(begin, end);
      final Example example = new Example(exampleLines);
      problem.add(example);
      begin = end;
    }
    return problem;
  }
}
