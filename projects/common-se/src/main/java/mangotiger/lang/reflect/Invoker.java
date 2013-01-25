package mangotiger.lang.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A helper class to invoking named methods via reflection.
 * @author tom_gagnier@yahoo.com
 */
public final class Invoker {
  private static final Log LOG = LogFactory.getLog(Invoker.class);
  private static final Object[] NO_ARGS = new Object[]{};
  private static final Class[] NO_ARG_TYPES = new Class[]{};
  private final Object instance;

  /**
   * Create an invoker from an object.
   * @param instance an object instance.
   */
  public Invoker(final Object instance) {
    this.instance = instance;
  }

  /**
   * Invoke a method on the wrapped object.
   * @param methodName    the method to invoke.
   * @param argumentTypes the method argument types.
   * @param arguments     the method arguments.
   * @return the result of the method.
   */
  Object invoke(final String methodName, final Class[] argumentTypes, final Object[] arguments) {
    try {
      final Class aClass = instance.getClass();
      final Method m = aClass.getMethod(methodName, argumentTypes);
      return m.invoke(instance, arguments);
    } catch (NoSuchMethodException e) {
      LOG.error("", e);
      throw new IllegalArgumentException(e.getMessage(), e);
    } catch (IllegalAccessException e) {
      LOG.error("", e);
      throw new IllegalArgumentException(e.getMessage(), e);
    } catch (InvocationTargetException e) {
      LOG.error("", e);
      throw new IllegalArgumentException(e.getMessage(), e);
    }
  }

  /**
   * Invoke a method on the wrapped object.
   * @param methodName the method to invoke.
   * @return the result of the method.
   */
  public Object invoke(final String methodName) {
    return invoke(methodName, NO_ARG_TYPES, NO_ARGS);
  }

  @Override
  public String toString() {
    return "Invoker{instance=" + instance + '}';
  }
}
