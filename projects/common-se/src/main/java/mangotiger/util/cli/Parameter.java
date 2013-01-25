package mangotiger.util.cli;

import java.util.List;

/**
 * A command line parameter.
 * @author tom_gagnier@yahoo.com
 */
abstract class Parameter {
  private final int argumentCount;
  private final boolean array;
  private final Object defaultValue;
  private final String description;
  private boolean initialized;
  private final String name;
  private final boolean required;
  private Object value;

  /**
   * A parameter constructed with its bit switch descriptor.
   * @param name          the name of the parameter switch as used on the command line.
   * @param description   the paramter's function.
   * @param required      is this parameter required?
   * @param argumentCount if negative, an array with at least that many members.  Else exactly that many arguments are
   *                      required.
   * @param defaultValue  the parameter's value.
   */
  protected Parameter(final String name, final String description, final boolean required, final int argumentCount, final Object defaultValue) {
    this.name = name;
    this.description = description;
    this.required = required;
    array = argumentCount < 0;
    this.argumentCount = array ? -argumentCount : argumentCount;
    this.defaultValue = defaultValue;
    reset();
  }

  /**
   * A parameter constructed with its bit switch descriptor. <p/> If default value is null, then the required flag is
   * set true.
   * @param name         the name of the parameter switch as used on the command line.
   * @param description  the paramter's function.
   * @param defaultValue the parameter's value.
   */
  protected Parameter(final String name, final String description, final Object defaultValue) {
    this(name, description, defaultValue == null, 1, defaultValue);
  }

  /**
   * Invoked by <code>CmdLine.parse(String[])</code> to reset all of its asscociated parameters.  That way, multiple
   * command lines can be parsed.
   */
  final void reset() {
    initialized = false;
    setValue(defaultValue);
  }

  /**
   * Set the value of a parameter from a command line.
   * @param args  The command line arguements being processed.
   * @param index The index of the current argument.
   * @return The number of arguments processed.
   */
  final int set(final List<String> args, final int index) {
    initialized = true;
    return assign(args, index);
  }

  /**
   * Assign the value from the list and return the last used index.
   * @param args  an ArrayList of command line arguments.
   * @param index the current command line argument.
   * @return the last index of the assigned argument.
   */
  abstract int assign(List<String> args, int index);

  /**
   * The text representation of the parameter's value.
   * @return the text representation of the parameter's value.
   */
  @Override public final String toString() {
    return value == null ? "null" : value.toString();
  }

  final String getName() {
    return name;
  }

  private String getDescription() {
    return description;
  }

  final Object getValue() {
    return value;
  }

  final void setValue(final Object value) {
    this.value = value;
  }

  final boolean isRequired() {
    return required;
  }

  private String paddedName(final int pad) {
    final int justify = pad - getName().length();
    if (justify < 1) {
      return getName();
    }
    final StringBuffer buffer = new StringBuffer(pad);
    buffer.append(getName());
    for (int i = 0; i < justify; ++i) {
      buffer.append(' ');
    }
    return buffer.toString();
  }

  final String getUsage(final int maxNameLen) {
    return new StringBuffer()
        .append(paddedName(maxNameLen))
        .append(' ')
        .append(getDescription())
        .append(getDescription().charAt(getDescription().length() - 1) == '.' ? "" : ".")
        .append("  ")
        .append(isRequired() ? "Required" : "Optional")
        .append(".  ")
        .append(argumentCount == 0 ? "No" : array ? "Zero or more" : "One")
        .append(" argument")
        .append(argumentCount == 0 || array ? "s" : "")
        .append('.')
        .toString();
  }

  final String getTerseUsage() {
    return (isRequired() ? "{" : "[") + '-' + getName() + (array ? " ..." : "") + (isRequired() ? "}" : "]");
  }

  final boolean isInitialized() {
    return initialized;
  }

  static boolean isParameter(final String s) {
    return s.length() > 0 && (s.charAt(0) == '-' || s.charAt(0) == '/');
  }

  static boolean isFile(final String s) {
    return s.length() > 0 && s.charAt(0) == '@';
  }
}
