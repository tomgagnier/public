package mangotiger.topcoder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A TopCoder problem statement parameter.
 * @author tom_gagnier@yahoo.com
 */
final class Parameter {

  private final String type;
  private final String name;

  private Parameter(final String declaration) {
    final String[] splitDeclaration = declaration.split(" +");
    type = splitDeclaration[0];
    name = splitDeclaration[1];
  }

  public String getType() {
    return type;
  }

  public String getName() {
    return name;
  }

  static Parameter[] parseSignature(final String signature) {
    final Pattern pattern = Pattern.compile(".*[(](.*)[)]");
    final Matcher matcher = pattern.matcher(signature);
    matcher.matches();
    final String allsigs = matcher.group(1);
    final String[] args = allsigs.split(" *, *");
    final Parameter[] parameters = new Parameter[args.length];
    for (int i = 0; i < args.length; ++i) {
      parameters[i] = new Parameter(args[i]);
    }
    return parameters;
  }

  @Override public String toString() {
    return new StringBuffer().append(type).append(' ').append(name).toString();
  }
}
