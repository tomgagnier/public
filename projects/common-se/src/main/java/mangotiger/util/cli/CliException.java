package mangotiger.util.cli;

/**
 * An exception thrown when a command line arguments do not match the specified grammar.
 * @author tom_gagnier@yahoo.com
 */
public final class CliException extends RuntimeException {
  CliException(final CmdLine cmdLine) {
    super(toMessage(cmdLine));
  }

  private static String toMessage(final CmdLine cmdLine) {
    final StringBuffer buffer = new StringBuffer();
    if (cmdLine.getErrors().size() > 0) {
      buffer.append("\nErrors processing command line:\n");
      for (Object o : cmdLine.getErrors()) {
        buffer.append(o).append('\n');
      }
    }
    return buffer.append('\n').append(cmdLine.getUsage()).toString();
  }

  @Override public String toString() {
    return "CliException{super=" + super.toString() + '}';
  }
}
