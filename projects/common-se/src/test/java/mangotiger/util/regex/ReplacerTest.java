package mangotiger.util.regex;

import junit.framework.TestCase;

/**
 * @author tom_gagnier@yahoo.com
 */
public class ReplacerTest extends TestCase {
  Replacer replacer;

  public void testReplacerLongBoolean() {
    replacer = new Replacer("getIs[$_]?([A-Z].*)", "$1");
    assertEquals("True", replacer.execute("getIsTrue"));
    assertEquals("getIssue", replacer.execute("getIssue"));
  }

  public void testReplacerTarget() {
    replacer = new Replacer("[gs]et[Tt]\\$.*", "Target");
    assertEquals("Target", replacer.execute("setT$Foo"));
    assertEquals("getIssue", replacer.execute("getIssue"));
  }

  public void testReplacerSource() {
    replacer = new Replacer("[gs]et[Ss]\\$.*", "Source");
    assertEquals("Source", replacer.execute("sets$Foo"));
    assertEquals("getIssue", replacer.execute("getIssue"));
  }

  public void testAccessor() {
    replacer = new Replacer("[gs]et[$_]?([A-Z].*)", "$1");
    assertEquals("Name", replacer.execute("setName"));
    assertEquals("Name", replacer.execute("getName"));
    assertEquals("isName", replacer.execute("isName"));
  }

  public void test() {
    replacer = new Replacer("([gs]et(Is)?|is)[_$]?([A-Z_$].*)", "$3");
    assertEquals("Issue", replacer.execute("getIssue"));
    assertEquals("Issue", replacer.execute("setIssue"));
    assertEquals("Issue", replacer.execute("isIssue"));
    assertEquals("Issue", replacer.execute("getIsIssue"));
    assertEquals("Issue", replacer.execute("setIsIssue"));
    assertEquals("_issue", replacer.execute("get_issue"));
    assertEquals("_issue", replacer.execute("set_issue"));

  }

}