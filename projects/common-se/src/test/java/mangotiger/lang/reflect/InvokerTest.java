package mangotiger.lang.reflect;

/**
 * @author tom_gagnier@yahoo.com
 */

import junit.framework.TestCase;

/** Test Invoker. */
@SuppressWarnings({"ClassWithoutToString"})
public final class InvokerTest extends TestCase {
  Invoker invoker;

  @Override protected void setUp() throws Exception {
    super.setUp();
    final SomeClass someClass = new SomeClass();
    invoker = new Invoker(someClass);
  }

  /** Test invoking with argument list. */
  public void testWithArguments() {
    final Object result = invoker.invoke("returnIntegerArgs", new Class[]{Integer.class}, new Object[]{1});
    assertEquals(1, result);
  }

  static final class SomeClass {
    public void returnVoidNoArgs() {
    }

    public int returnIntNoArgs() {
      return 1;
    }

    public Integer returnIntegerNoArgs() {
      return 1;
    }

    public Integer returnIntegerArgs(final Integer i) {
      return i;
    }
  }
}