package mangotiger.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Loads the properties file.
 * @author tom_gagnier@yahoo.com
 */
public class Config {
  private static final Log LOG = LogFactory.getLog(Config.class);
  private final Properties properties = new Properties();
  private static final int CONFIG_TO_STRING_SIZE = 2048;
  private static final Config instance = new Config();
  private final List resources = new LinkedList();

  Config() {
    loadResource(this);
    LOG.info(list());
  }

  /**
   * The Config instance.
   * @return The Config instance.
   */
  public static Config instance() {
    return instance;
  }

  /**
   * Load the ccnfiguration properties. <p/> Resource names should be absolute, starting with a '/'.
   * @param resource the name of the property resource.
   * @return true if successfully loaded.
   */
  public final boolean loadResource(final String resource) {
    resources.add(resource);
    final InputStream in = Config.class.getResourceAsStream(resource);
    if (in == null) {
      LOG.warn('\'' + resource + "' not found.");
      return false;
    }
    try {
      loadProperties(in);
      return true;
    } catch (IOException e) {
      LOG.error(resource, e);
    }
    return false;
  }

  void loadProperties(final InputStream in) throws IOException {
    properties.load(in);
  }

  /**
   * Load a resource using the object's class name as the root of the resource name. <p/> For example, an object in
   * mangotiger.util.Object would have a resource file "/mangotiger.util.Object.properties".
   * @param object The object whose properties file is being loaded.
   */
  private void loadResource(final Object object) {
    loadResource('/' + object.getClass().getName() + ".properties");
  }

  /**
   * A sorted string representation of properties.
   * @return a case-insensitive sorted list of properties.
   */
  private String list() {
    final SortedSet ss = getPropertyKeySet();
    final StringBuffer buffer = new StringBuffer(CONFIG_TO_STRING_SIZE);
    buffer.append('\n');
    for (Iterator i = ss.iterator(); i.hasNext();) {
      final String key = (String)i.next();
      buffer.append(key).append('=').append(isSecret(key) ? "****" : properties.getProperty(key));
      if (i.hasNext()) {
        buffer.append('\n');
      }
    }
    return buffer.toString();
  }

  private SortedSet getPropertyKeySet() {
    final Comparator c = new Comparator() {
      public int compare(final Object left, final Object right) {
        final String lhs = ((String)left).toLowerCase();
        final String rhs = ((String)right).toLowerCase();
        return lhs.compareTo(rhs);
      }
    };
    final SortedSet propertyKeySet = new TreeSet(c);
    propertyKeySet.addAll(properties.keySet());
    return propertyKeySet;
  }

  private static boolean isSecret(final String key) {
    return key.toLowerCase().indexOf("password") != -1;
  }

  /**
   * Get a property  value.
   * @param propertyName The name of the property.
   * @return the value of the property, or null if not found.
   */
  public final String get(final String propertyName) {
    final String property = properties.getProperty(propertyName);
    if (property == null) {
      LOG.warn(propertyName + " not found");
    }
    return property;
  }

  /**
   * Get a property  value.
   * @param propertyName The name of the property.
   * @param defaultValue The default value if the property does not exist.
   * @return the value of the property, or defaultValue if not found.
   */
  public final String get(final String propertyName, final String defaultValue) {
    return properties.getProperty(propertyName, defaultValue);
  }

  /**
   * Get a property  value.
   * @param aClass       The class whose package name to use as a prefix.
   * @param propertyName The name of the property.
   * @return the value of the property, or null if not found.
   */
  public final String get(final Class aClass, final String propertyName) {
    return get(toKey(aClass, propertyName));
  }

  /**
   * Get a property  value.
   * @param aClass       The class whose package name to use as a prefix.
   * @param propertyName The name of the property.
   * @param defaultValue The default value of the property if none configured.
   * @return the value of the property, or null if not found.
   */
  public final String get(final Class aClass, final String propertyName, final String defaultValue) {
    return get(toKey(aClass, propertyName), defaultValue);
  }

  private static String toKey(final Class aClass, final String name) {
    final String prefix = aClass.getPackage().getName();
    final StringBuffer key = new StringBuffer(prefix.length() + name.length() + 1);
    key.append(prefix);
    if (key.length() > 0) {
      key.append('.');
    }
    key.append(name);
    return key.toString();
  }

  /**
   * Set a property value.
   * @param key   the property key.
   * @param value the property value.
   */
  public final void set(final String key, final String value) {
    properties.setProperty(key, value);
  }

  @Override public final String toString() {
    return resources.toString();
  }

  /**
   * Get an integer.
   * @param key          the integer's look up key (name).
   * @param defaultValue the value to find if not found or a parse error occurs.
   * @return the integer's value.
   */
  private int getInt(final String key, final int defaultValue) {
    final String value = get(key);
    try {
      if (value != null) {
        return Integer.parseInt(value);
      }
    } catch (NumberFormatException e) {
      LOG.error(key + " -> " + value, e);
    }
    return defaultValue;
  }

  /**
   * Get an integer.
   * @param aClass       a class that shares the integer's package prefix.
   * @param key          the integer's look up key (name).
   * @param defaultValue the value to find if not found or a parse error occurs.
   * @return the integer's value.
   */
  public final int getInt(final Class aClass, final String key, final int defaultValue) {
    return getInt(toKey(aClass, key), defaultValue);
  }
}