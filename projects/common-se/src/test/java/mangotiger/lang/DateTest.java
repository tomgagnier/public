package mangotiger.lang;

import java.util.Calendar;
import java.util.Date;

import junit.framework.TestCase;

/**
 * @author Tom Gagnier
 */
public class DateTest extends TestCase {

  private static final long MILLIS_PER_DAY = 1000 * 60 * 60 * 24;

  public void test() {
    Calendar calendar = Calendar.getInstance();
    calendar.set(2005, 5, 28);
    Date now = calendar.getTime();
    calendar.set(2004, 5, 28);
    Date then = calendar.getTime();
    final long millis = now.getTime() - then.getTime();
    assertEquals(365, millis / MILLIS_PER_DAY);
  }

}
