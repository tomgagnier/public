package mangotiger.test;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import junit.framework.TestCase;

/** @author Tom Gagnier */
@SuppressWarnings({"ClassWithoutToString"}) public final class NameIteratorTest extends TestCase {
  public static void test() {
    final Iterator nameIterator = new NameIterator();
    final Set<String> names = new TreeSet<String>();
    while (nameIterator.hasNext()) {
      final String name = (String)nameIterator.next();
      log().info(name);
      final boolean isNew = names.add(name);
      assertTrue(isNew);
    }
    final String name = (String)nameIterator.next();
    assertTrue(name, names.contains(name));
  }

  private static Log log() {
    return LogFactory.getLog(NameIteratorTest.class);
  }
}
