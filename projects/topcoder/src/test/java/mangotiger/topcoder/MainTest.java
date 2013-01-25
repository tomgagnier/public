package mangotiger.topcoder;

import java.io.IOException;

import junit.framework.TestCase;

/**
 * Test the Preparer.
 * @author tom_gagnier@yahoo.com
 */
@SuppressWarnings({"ClassWithoutToString"})
public final class MainTest extends TestCase {
  private static final String OUTPUT_DIRECTORY = "target/generated-test-sources";
  private static final String PREFIX = "src/test/resources/mangotiger/topcoder/";

  public static void test250() throws IOException {
    write("250.txt");
  }

  public static void test500() throws IOException {
    write("500.txt");
  }

  private static void write(final String file) throws IOException {
    Main.main(new String[]{PREFIX + file, OUTPUT_DIRECTORY});
  }
}
