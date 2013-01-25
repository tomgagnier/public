package mangotiger.util.cli;

import java.util.LinkedList;
import java.util.List;

/**
 * A text parameter array.  Only the <code>CmdLine.addStringArray(...)</code> method class can instantiate members of
 * this class.
 * @author tom_gagnier@yahoo.com
 */
@SuppressWarnings({"ClassWithoutToString"})
public final class StringArrayParameter extends Parameter {
  StringArrayParameter(final String name, final String description, final List defaultValues, final int argumentCount) {
    super(name, description, false, argumentCount, defaultValues);
  }

  /**
   * The value of the text array.
   * @return the value as a <code>String[]</code>.
   */
  public String[] value() {
    final List list = (List)getValue();
    return (String[])list.toArray(new String[list.size()]);
  }

  @Override int assign(final List<String> args, final int index) {
    final List<Object> values = new LinkedList<Object>();
    int i = index + 1;
    while (i < args.size() && !isParameter(args.get(i))) {
      values.add(args.get(i++));
    }
    setValue(values);
    return i - index - 1;
  }
}

