package mangotiger.util.cli;

import java.util.List;

/**
 * An integer parameter.  Only the <code>CmdLine.addInteger(...)</code> method class can instantiate members of this
 * class.
 * @author tom_gagnier@yahoo.com
 */
@SuppressWarnings({"ClassWithoutToString"})
public final class IntegerParameter extends Parameter {
  IntegerParameter(final String name, final String description, final int defaultValue) {
    super(name, description, defaultValue);
  }

  IntegerParameter(final String name, final String description) {
    super(name, description, true, 1, null);
  }

  /**
   * The value of the parameter switch.
   * @return the value as an <code>int</code>.
   */
  public int value() {
    return (Integer)getValue();
  }

  @Override protected int assign(final List<String> args, final int index) {
    setValue(new Integer(args.get(index + 1).toString()));
    return 1;
  }
}

