package mangotiger.text;

import junit.framework.TestCase;

/**
 * @author Tom Gagnier
 */
public class TestNames extends TestCase {

  public void testDatabasifyName() throws Exception {
    assertDatabasify("case", "case");
    assertDatabasify("camel_case", "camelCase");
    assertDatabasify("camel_case", "CamelCase");
  }

  public void testCamelCaseName() throws Exception {
    assertCamelCase("Case", "case");
    assertCamelCase("CamelCase", "camel_case");
  }

  public void testDisplayName() throws Exception {
    assertDisplayName("", null);
    assertDisplayName("", "  ");
    assertDisplayName("Case", "case");
    assertDisplayName("Case", "Case");
    assertDisplayName("Case Load", "caseLoad");
    assertDisplayName("Case Load", "CaseLoad");
    assertDisplayName("Case Load", " caseLoad  ");
    assertDisplayName("ABC Case", "ABCCase");
    assertDisplayName("ABC Case D", "ABCCaseD");
  }

  private void assertDatabasify(String expect, String camelCaseName) {
    assertEquals(expect, Names.databasifyName(camelCaseName));
  }

  private void assertCamelCase(String expect, String databaseName) {
    assertEquals(expect, Names.camelCaseName(databaseName));
  }

  private void assertDisplayName(String expect, String camelCaseName) {
    assertEquals(expect, Names.displayName(camelCaseName));
  }
}