package mangotiger.util.cli;

import java.util.List;

/**
 * A boolean parameter.  Only the <code>CmdLine.addBoolean(...)</code> method can instantiate members of this class.
 * @author tom_gagnier@yahoo.com
 */
@SuppressWarnings({"ClassWithoutToString"})
public final class BooleanParameter extends Parameter {
  /**
   * A parameter representing a boolean switch.
   * @param name         The name of the parameter switch as used on the command line.
   * @param description  A description of the paramter's function.
   * @param defaultValue The parameter's default value.
   */
  BooleanParameter(final String name, final String description, final boolean defaultValue) {
    super(name, description, false, 0, Boolean.valueOf(defaultValue));
  }

  /**
   * The value as a <code>boolean</code>.
   * @return true if flag is set.
   */
  public boolean value() {
    return (Boolean)getValue();
  }

  /**
   * Assign the value from the list and return the last used index.
   * @param args  an ArrayList of command line arguments.
   * @param index the current command line argument.
   * @return the last index of the assigned argument.
   */
  @Override protected int assign(final List<String> args, final int index) {
    setValue(Boolean.valueOf(!value()));
    return 0;
  }
}

