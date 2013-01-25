package mangotiger.util.cli;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Command Line Interface; <p/> CmdLine performs the following functions: <ul> <li>Parse command line arguments.
 * <li>Print descriptive usage text. <li>Validate arguments. <li>Accept parameters from a text file. <li>Accept a raw
 * argument list. </ul> </p>
 * @author tom_gagnier@yahoo.com
 */
public final class CmdLine {
  private List<String> arguments;
  private List<String> cmdLine;
  private final String cmdDescription;
  private List<String> errors;
  private final String cmdName;
  private int maxParameterNameLength;
  private final SortedMap<String, Parameter> parameters = new TreeMap<String, Parameter>();
  private final BooleanParameter help;
  private static final int MAX_BUFFER_SIZE = 2048;

  /**
   * A command line used to invoke an application.
   * @param main        The class of the main program.
   * @param description Descriptive description text.
   */
  public CmdLine(final Class main, final String description) {
    if (main == null || description == null) {
      throw new IllegalArgumentException("null argument");
    }
    cmdName = main.getName();
    cmdDescription = description;
    help = addBoolean("?", "Print usage text.");
  }

  /**
   * A list of all arguments not associated with a parameter on the command line. <p/> The initial order of arguments is
   * preserved.
   * @return a List.
   */
  public List getArguments() {
    return new ArrayList<String>(arguments);
  }

  /**
   * A list of strings describing parse errors.
   * @return A list of strings describing parse errors.
   */
  public List getErrors() {
    return new ArrayList<String>(errors);
  }

  /**
   * A usage message. <p/> The message is constructed from the CmdLine and Parameter descriptions.
   * @return a String.
   */
  String getUsage() {
    final StringBuffer usage = new StringBuffer(cmdName);
    for (Object o : parameters.values()) {
      usage.append(' ').append(((Parameter)o).getTerseUsage());
    }
    usage.append("\n\t").append(cmdDescription);
    for (Object o1 : parameters.values()) {
      final Parameter p = (Parameter)o1;
      usage.append("\n\t-");
      usage.append(p.getUsage(getMaxParameterNameLength()));
    }
    return usage.toString();
  }

  /**
   * Parse a command line and set the parameter and argument values.
   * @param args The command line array from main().
   * @throws IOException if unable to read file containing arguments
   */
  public void parse(final String ... args) throws IOException {
    reset();
    setCmdLine(args);
    int i = 0;
    for (; i < cmdLine.size(); ++i) {
      if (Parameter.isParameter(cmdLine.get(i))) {
        i += addParameter(i);
      } else {
        arguments.add(cmdLine.get(i));
      }
    }

    // Help has been requested.
    if (help.value()) {
      throw new CliException(this);
    }

    // Have all required parameters been set?
    for (Object o : parameters.values()) {
      final Parameter parameter = (Parameter)o;
      if (parameter.isRequired() && !parameter.isInitialized()) {
        errors.add(new StringBuilder().append("\n\tRequired parameter switch missing (-")
            .append(parameter.getName())
            .append(").").toString());
      }
    }

    // Throw an exception listing the errors discovered while parsing.
    if (errors.size() > 0) {
      throw new CliException(this);
    }
  }

  private int addParameter(final int i) {
    final String paramName = cmdLine.get(i).substring(1).toLowerCase();
    final Parameter parameter = parameters.get(paramName);
    if (parameter == null) {
      errors.add("\n\tUnrecognized switch name (" + cmdLine.get(i) + ").");
      return i;
    }
    return parameter.set(cmdLine, i);
  }

  private void setCmdLine(final String[] args) throws IOException {
    // Check each argument to determine if parameters are
    // being added from a file using the @ option.
    cmdLine = new ArrayList<String>(args.length);
    for (int i = 0; i < args.length; ++i) {
      if (Parameter.isFile(args[i])) {
        readFileParameters(args, i);
      } else {
        cmdLine.add(args[i]);
      }
    }
  }

  private void readFileParameters(final String[] args, final int i) throws IOException {
    final String filename = args[i].substring(1);
    FileReader reader = null;
    try {
      reader = new FileReader(filename);
      final char[] buf = new char[MAX_BUFFER_SIZE];
      boolean newArg = true;
      for (int bytesRead = reader.read(buf, 0, buf.length);
           bytesRead != -1; bytesRead = reader.read(buf, 0, buf.length)) {
        for (int j = 0; j < bytesRead; ++j) {
          final char c = buf[j];
          if (Character.isISOControl(c) || Character.isWhitespace(c)) {
            newArg = true;
          } else {
            if (newArg) {
              cmdLine.add("");
            }
            // Concatenate character on last command line argument.
            final int last = cmdLine.size() - 1;
            cmdLine.set(last, new StringBuilder().append(cmdLine.get(last)).append(c).toString());
            newArg = false;
          }
        }
      }
    } finally {
      if (reader != null) {
        reader.close();
      }
    }
  }

  private void reset() {
    arguments = new LinkedList<String>();
    errors = new LinkedList<String>();
    for (Object o : parameters.values()) {
      ((Parameter)o).reset();
    }
  }

  /**
   * String representation of the CmdLine state variables.
   * @return the name of the application and its current switch and argument settings.
   */
  @Override public String toString() {
    return new StringBuffer().append(cmdName).append(parameters).append(arguments).toString();
  }

  private int getMaxParameterNameLength() {
    if (maxParameterNameLength == 0) {
      for (Object o : parameters.values()) {
        final int length = ((Parameter)o).getName().length();
        maxParameterNameLength = maxParameterNameLength < length ? length : maxParameterNameLength;
      }
    }
    return maxParameterNameLength;
  }

  private Parameter add(final Parameter parameter) {
    parameters.put(parameter.getName(), parameter);
    return parameter;
  }

  /**
   * Add a new boolean parameter.
   * @param name         the parameter switch as used on the command line.
   * @param description  the paramter's function.
   * @param defaultValue the paramter's default value.
   * @return the new boolean parameter.
   */
  public BooleanParameter addBoolean(final String name, final String description, final boolean defaultValue) {
    return (BooleanParameter)add(new BooleanParameter(name, description, defaultValue));
  }

  /**
   * Add a new boolean parameter with a default value of false.
   * @param name        the parameter switch as used on the command line.
   * @param description the paramter's function.
   * @return the newly added boolean parameter.
   */
  public BooleanParameter addBoolean(final String name, final String description) {
    return addBoolean(name, description, false);
  }

  /**
   * Add a new integer value.
   * @param name         the name of the parameter switch as used on the command line.
   * @param description  a description of the paramter's function.
   * @param defaultValue the parameter's default values if none assigned .
   * @return a new integer parameter.
   */
  public IntegerParameter addInteger(final String name, final String description, final int defaultValue) {
    return (IntegerParameter)add(new IntegerParameter(name, description, defaultValue));
  }

  /**
   * Add a new integer value. <p/> Without a default value, an exception will be thrown during parsing if the parameter
   * is not present in the command line arguments..
   * @param name        the name of the parameter switch as used on the command line.
   * @param description a description of the paramter's function.
   * @return a new integer parameter.
   */
  public IntegerParameter addInteger(final String name, final String description) {
    return (IntegerParameter)add(new IntegerParameter(name, description));
  }

  /**
   * Add a new text parameter.
   * @param name         the parameter switch as used on the command line.
   * @param description  the paramter's function.
   * @param defaultValue the paramter's default value.
   * @return a newly created text parameter.
   */
  public StringParameter addString(final String name, final String description, final String defaultValue) {
    return (StringParameter)add(new StringParameter(name, description, defaultValue));
  }

  /**
   * Add a new text parameter. <p/> Without a default value, an exception will be thrown during parsing if the parameter
   * is not present in the command line arguments..
   * @param name        the parameter switch as used on the command line.
   * @param description the paramter's function.
   * @return a newly created text parameter.
   */
  public StringParameter addString(final String name, final String description) {
    return (StringParameter)add(new StringParameter(name, description, null));
  }

  /**
   * Add a new text array parameter.
   * @param name         the parameter switch as used on the command line.
   * @param description  the paramter's function.
   * @param defaultValue the paramter's default value.
   * @return a newly created text array parameter.
   */
  private StringArrayParameter addStringArray(final String name, final String description, final List defaultValue) {
    return (StringArrayParameter)add(new StringArrayParameter(name, description, defaultValue, -1));
  }

  /**
   * Add a new text array parameter.
   * @param name         the parameter switch as used on the command line.
   * @param description  the paramter's function.
   * @param defaultValue the paramter's default value.
   * @return a newly created text array parameter.
   */
  public StringArrayParameter addStringArray(final String name, final String description, final String[] defaultValue) {
    return addStringArray(name, description, Arrays.asList(defaultValue));
  }

  /**
   * Add a new text array parameter. <p/> Without a default value, an exception will be thrown during parsing if the
   * parameter is not present in the command line arguments..
   * @param name        the parameter switch as used on the command line.
   * @param description the paramter's function.
   * @return a newly created text array parameter.
   */
  public StringArrayParameter addStringArray(final String name, final String description) {
    return (StringArrayParameter)add(new StringArrayParameter(name, description, null, -1));
  }
}
