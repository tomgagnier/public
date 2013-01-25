package mangotiger.util;

import java.io.IOException;
import java.io.InputStream;

import junit.framework.TestCase;

/**
 * Test @see Config.
 * @author tom_gagnier@yahoo.com
 */
@SuppressWarnings({"ClassWithoutToString"})
public final class ConfigTest extends TestCase {
  private final Config config = Config.instance();
  private static final String RESOURCE = "/mangotiger.util.Config.properties";

  /** Test the default config resource. */
  public void todoTestDefault() {
    config.loadResource(RESOURCE);
    assertEquals("sa", config.get("mangotiger.db.user"));
    assertEquals("x", config.get("x", "x"));
  }

  /** Test a bad resource. */
  public void testBadResource() {
    assertFalse(config.loadResource("bad resource"));
  }

  /** Test a null resource. */
  public void testNullResource() {
    assertTrue(config.get("null resource") == null);
  }

  /** Test an IOException. */
  public static void testIOException() {
    final Config mockConfig = new Config() {
      @Override void loadProperties(final InputStream in) throws IOException {
        throw new IOException();
      }
    };
    mockConfig.loadResource(RESOURCE);
  }

  /** Test default value. */
  public void testDefaultGet() {
    final String defaultString = "default value";
    assertEquals(defaultString, config.get("unknown", defaultString));
    final String missingPropertyName = "missing.property.name";
    assertEquals(defaultString, config.get(getClass(), missingPropertyName, defaultString));
    final int defaultInt = 42;
    assertEquals(defaultInt, config.getInt(getClass(), missingPropertyName, defaultInt));
  }
}
