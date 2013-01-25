package mangotiger.lang.reflect;

/**
 * @author tom_gagnier@yahoo.com
 */

import java.lang.reflect.Field;
import java.util.List;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import junit.framework.TestCase;

/** Test the ClassLoaders class. */
@SuppressWarnings({"ClassWithoutToString"})
public final class ClassLoadersTest extends TestCase {
  /** Test the classes iterator. */
  public void testClasses() {
    final List classes = classes(ClassLoader.getSystemClassLoader());
    for (Object clazz : classes) {
      log().debug(clazz);
    }
  }

  private static List classes(final ClassLoader cl) {
    try {
      Class c = cl.getClass();
      while (!c.equals(ClassLoader.class)) {
        c = c.getSuperclass();
      }
      final Field classesField = c.getDeclaredField("classes");
      classesField.setAccessible(true);
      return (Vector)classesField.get(cl);
    } catch (NoSuchFieldException e) {
      log().error(e);
    } catch (IllegalAccessException e) {
      log().error(e);
    }
    return null;
  }

  private static Log log() {
    return LogFactory.getLog(ClassLoadersTest.class);
  }
}