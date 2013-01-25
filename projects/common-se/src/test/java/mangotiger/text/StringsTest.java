package mangotiger.text;

import junit.framework.TestCase;

/** @author tom_gagnier@yahoo.com */
@SuppressWarnings({"ClassWithoutToString"})
public class StringsTest extends TestCase {
  public void testLast() throws Exception {
    assertEquals("lastName", Strings.last("my.primaryName.lastName"));
    assertEquals("lastName", Strings.last("lastName"));
    assertEquals("lastName", Strings.last("primaryName.lastName"));
    assertEquals("lastName", Strings.last(".lastName"));
  }

  public void testTruncate() throws Exception {
    assertEquals("", Strings.truncate("1234567890", 0));
    assertEquals("1", Strings.truncate("1234567890", 1));
    assertEquals("12", Strings.truncate("1234567890", 2));
    assertEquals("123", Strings.truncate("1234567890", 3));
    assertEquals("123456789", Strings.truncate("1234567890", 9));
    assertEquals("1234567890", Strings.truncate("1234567890", 10));
  }

  public void testFirst() throws Exception {
    assertEquals("my", Strings.first("my.primaryName.lastName"));
    assertEquals("lastName", Strings.first("lastName"));
    assertEquals("primaryName", Strings.first("primaryName.lastName"));
    assertEquals("", Strings.first(".lastName"));
  }

  public void testToLeadingLowerCase() throws Exception {
    assertEquals("helloWorld", Strings.toLeadingLowerCase("helloWorld"));
    assertEquals("helloWorld", Strings.toLeadingLowerCase("HelloWorld"));
    assertEquals("hElloWorld", Strings.toLeadingLowerCase("HElloWorld"));
    assertNull(Strings.toLeadingLowerCase(null));
    assertEquals("", Strings.toLeadingLowerCase(""));
  }

  public void testToLeadingUpperCase() throws Exception {
    assertEquals("HelloWorld", Strings.toLeadingUpperCase("helloWorld"));
    assertEquals("HelloWorld", Strings.toLeadingUpperCase("HelloWorld"));
    assertEquals("HElloWorld", Strings.toLeadingUpperCase("hElloWorld"));
    assertNull(Strings.toLeadingLowerCase(null));
    assertEquals("", Strings.toLeadingUpperCase(""));
  }

  public void testToNullIfWhitespace() throws Exception {
    assertNull(Strings.toNullIfWhitespace(null));
    assertNull(Strings.toNullIfWhitespace(""));
    assertNull(Strings.toNullIfWhitespace(" "));
    assertNull(Strings.toNullIfWhitespace("  "));
    assertNull(Strings.toNullIfWhitespace("   "));
    assertNotNull(Strings.toNullIfWhitespace("s"));
    assertNotNull(Strings.toNullIfWhitespace(" s"));
    assertNotNull(Strings.toNullIfWhitespace(" s "));
    assertNotNull(Strings.toNullIfWhitespace("s "));
  }
}