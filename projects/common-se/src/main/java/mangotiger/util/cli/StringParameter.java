package mangotiger.util.cli;

import java.util.List;

/**
 * A text parameter.  Only the <code>CmdLine.addString(...)</code> method class can instantiate members of this class.
 * @author tom_gagnier@yahoo.com
 */
@SuppressWarnings({"ClassWithoutToString"})
public final class StringParameter extends Parameter {
  StringParameter(final String name, final String description, final String defaultValue) {
    super(name, description, defaultValue);
  }

  /**
   * The value of the text parameter.
   * @return the value as a <code>String</code>.
   */
  public String value() {
    return (String)getValue();
  }

  /**
   * Assign the value from the list and return the last used index.
   * @param args  an ArrayList of command line arguments.
   * @param index the current command line argument.
   * @return the last index of the assigned argument.
   */
  @Override protected int assign(final List<String> args, final int index) {
    setValue(args.get(index + 1));
    return 1;
  }
}

