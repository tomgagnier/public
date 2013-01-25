package mangotiger.lang.reflect;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import mangotiger.text.Strings;
import mangotiger.util.regex.Replacer;

/**
 * A class introspector helper.  Allows use of sets and gets based on property names.
 * @author tom_gagnier@yahoo.com
 */
public final class Introspector {
  private static final Object[] ZERO_OBJECTS = new Object[]{};
  private static final Replacer TO_NAME = new Replacer("^([gs]et(Is)?|is)_?([A-Z$_].*)", "$3");
  private final Class clazz;
  private final Map<String, Map<String, Method>> settersByTypeByName = new TreeMap<String, Map<String, Method>>();
  private final Map<String, Method> gettersByName = new TreeMap<String, Method>();

  private static Log log() {
    return LogFactory.getLog(Introspector.class);
  }

  /**
   * Construct a class introspector.
   * @param clazz the class to introspect.
   */
  public Introspector(final Class clazz) {
    this.clazz = clazz;
    final Method[] methods = clazz.getMethods();
    for (final Method method : methods) {
      final String methodName = method.getName();
      final Class[] parameterTypes = method.getParameterTypes();
      if (parameterTypes.length >= 2) {
        continue;
      }
      final String propertyName = toProperty(methodName);
      if (propertyName.equals(methodName)) {
        continue;
      }
      if (parameterTypes.length == 0) {
        gettersByName.put(propertyName, method);
      } else {
        if (!settersByTypeByName.containsKey(propertyName)) {
          settersByTypeByName.put(propertyName, new TreeMap<String, Method>());
        }
        settersByTypeByName.get(propertyName).put(parameterTypes[0].getName(), method);
      }
    }
  }

  private static String toProperty(final String methodName) {
    return Strings.toLeadingLowerCase(TO_NAME.execute(methodName));
  }

  @Override public String toString() {
    final StringBuffer buffer = new StringBuffer(clazz.getName()).append('[');
    for (Object o : settersByTypeByName.keySet()) {
      buffer.append("\n\t");
      final String property = (String)o;
      final Map setters = settersByTypeByName.get(property);
      buffer.append(property).append(setters.keySet());
    }
    return buffer.append(']').toString();
  }

  /**
   * The value of thie object's property.
   * @param object   the object to introspect
   * @param property the property name
   * @return The value of thie object's property.
   * @throws IllegalStateException if unable to get value
   */
  public Object get(final Object object, final String property) {
    final String key = Strings.toLeadingLowerCase(property);
    final Method getter = gettersByName.get(key);
    if (getter == null) {
      final String message = "no getter for property " + property + " (are metadata and import schema in sync?):\n" +
                             this;
      log().error(message);
      throw new IllegalStateException(message);
    }
    try {
      return getter.invoke(object, ZERO_OBJECTS);
    } catch (Exception e) {
      final String message = "unable to invoke getter for property " + property + " on " + this;
      log().error(message, e);
      final IllegalStateException exception = new IllegalStateException(message);
      exception.setStackTrace(e.getStackTrace());
      throw exception;
    }
  }

  /**
   * Set the object's attribute value.
   * @param object   the object to modify
   * @param property the property name
   * @param value    the new value
   * @throws IllegalStateException if unable to set value
   */
  public void set(final Object object, final String property, final Object value) {
    final Map<String, Method> setters = getSetters(property);
    if (setters == null) return;
    if (invokeSetter(object, setters, value)) {
      return;
    } else if (value instanceof Boolean) {
      if (invokeSetter(object, setters, value, Boolean.TYPE)) return;
      final Integer integer = Boolean.TRUE.equals(value) ? 1 : 0;
      if (invokeSetter(object, setters, integer)) return;
      if (invokeSetter(object, setters, integer, Integer.TYPE)) return;
    } else if (value instanceof Integer) {
      if (invokeSetter(object, setters, value, Integer.TYPE)) return;
      if (invokeSetter(object, setters, (long)((Integer)value).intValue())) return;
    } else if (value instanceof Long) {
      if (invokeSetter(object, setters, new BigDecimal((Long)value))) return;
    }
    final String message = "no conversion found for '" + property + "' from " + value.getClass() + " using " +
                           setters.keySet();
    log().error(message);
    throw new IllegalStateException(message);
  }

  private Map<String, Method> getSetters(final String property) {
    final String key = Strings.toLeadingLowerCase(property);
    final Map<String, Method> setters = settersByTypeByName.get(key);
    if (setters == null) {
      final String message = "no setter for property " + property + " on " + this + "\nfrom\n" + methodList();
      log().warn(message);
    }
    return setters;
  }

  private String methodList() {
    final SortedSet<String> signatures = new TreeSet<String>();
    final Method[] methods = clazz.getMethods();
    for (final Method method : methods) {
      signatures.add(method.toString());
    }
    final StringBuffer buffer = new StringBuffer();
    for (String signature : signatures) {
      buffer.append('\t').append(signature).append('\n');
    }
    return buffer.toString();
  }

  private static boolean invokeSetter(final Object object, final Map<String, Method> setters, final Object value, final Class type) {
    final Method method = method(type, setters);
    if (method == null) return false;
    try {
      method.invoke(object, new Object[]{value == null ? defaultValue(method.getParameterTypes()[0]) : value});
      return true;
    } catch (Exception e) {
      final String message = "unable to invoke (" + method + ") on object: " + object;
      log().error(message, e);
      final IllegalStateException exception = new IllegalStateException(message);
      exception.setStackTrace(e.getStackTrace());
      throw exception;
    }
  }

  private static Method method(final Class type, final Map<String, Method> setters) {
    final Method method;
    if (type == null) {
      final Iterator<Method> iterator = setters.values().iterator();
      if (!iterator.hasNext()) return null;
      method = setters.values().iterator().next();
    } else {
      method = findMatchingMethod(setters, type);
    }
    return method;
  }

  private static Object defaultValue(final Class parameterType) {
    if (Boolean.TYPE.equals(parameterType)) {
      return Boolean.FALSE;
    } else if (Character.TYPE.equals(parameterType)) {
      return (char)0;
    } else if (Short.TYPE.equals(parameterType)) {
      return (short)0;
    } else if (Integer.TYPE.equals(parameterType)) {
      return 0;
    } else if (Long.TYPE.equals(parameterType)) {
      return 0L;
    } else if (Float.TYPE.equals(parameterType)) {
      return 0.0F;
    } else if (Double.TYPE.equals(parameterType)) {
      //noinspection MagicNumber
      return 0.0D;
    } else {
      return null;
    }
  }

  static Method findMatchingMethod(final Map<String, Method> setters, final Class clazz) {
    if (setters.containsKey(clazz.getName())) {
      return setters.get(clazz.getName());
    }
    for (Method method : setters.values()) {
      final Class type = method.getParameterTypes()[0];
      if (type.isAssignableFrom(clazz)) {
        return method;
      }
    }
    return null;
  }

  private static boolean invokeSetter(final Object object, final Map<String, Method> setters, final Object value) {
    return invokeSetter(object, setters, value, value == null ? null : value.getClass());
  }
}

