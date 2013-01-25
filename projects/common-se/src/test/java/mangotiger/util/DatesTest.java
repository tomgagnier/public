package mangotiger.util;

import java.util.Date;

import junit.framework.TestCase;

import mangotiger.time.Times;

/**
 * @author Tom Gagnier
 */
public class DatesTest extends TestCase {
  Times times;

  public void testMidnight() throws Exception {
    final long time = new Date().getTime();
    final String date = new Date(Times.midnight(time)).toString();
    assertTrue(date.matches(".* 00:00:00 .*"));
  }
}