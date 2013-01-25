package mangotiger.lang.reflect;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author tom_gagnier@yahoo.com
 */
public final class Introspectors {

  private final Map metadatas = new TreeMap();

  public final void set(final Object object, final String property, final Object value) {
    instance(object).set(object, property, value);
  }

  public final Object get(final Object object, final String property) {
    final Introspector introspector = instance(object);
    return introspector.get(object, property);
  }

  private Introspector instance(final Object object) {
    final Class clazz = object.getClass();
    final String name = clazz.getName();
    if (!metadatas.containsKey(name)) {
      final Introspector introspector = new Introspector(clazz);
      metadatas.put(name, introspector);
    }
    return (Introspector)metadatas.get(name);
  }
}

