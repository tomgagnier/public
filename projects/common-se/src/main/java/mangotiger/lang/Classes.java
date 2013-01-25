package mangotiger.lang;

import java.lang.reflect.Constructor;

/**
 * An object helper class.
 * @author tom_gagnier@yahoo.com
 */
public final class Classes {
  private static final Object[] NO_ARGUMENTS = new Object[]{};

  /**
   * A reflection classes that combines all the possible exceptions into an IllegalArgumentException.
   * @param className the name of the class.
   * @return a new using the specified class and arguments.
   * @throws IllegalArgumentException if unable to create the object.
   */
  public static Object forName(final String className) {
    return forName(className, NO_ARGUMENTS);
  }

  /**
   * A reflection classes that combines all the possible exceptions into an IllegalArgumentException.
   * @param className the name of the class.
   * @param arguments the actual arguments to use.
   * @return a new using the specified class and arguments.
   * @throws IllegalArgumentException if unable to create the object.
   */
  public static Object forName(final String className, final Object[] arguments) throws IllegalArgumentException {
    try {
      final Class[] types = new Class[arguments.length];
      for (int i = 0; i < types.length; ++i) {
        types[i] = arguments[i].getClass();
      }
      final Class aClass = Class.forName(className);
      final Constructor constructor = aClass.getConstructor(types);
      return constructor.newInstance(arguments);
    } catch (Exception e) {
      throw new IllegalArgumentException(className + ": " + e.getMessage(), e);
    }
  }

  private Classes() {}
}
