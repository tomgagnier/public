package mangotiger.lang.reflect;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import junit.framework.TestCase;

/**
 * Test the {@link Introspector} class.
 * @author tom_gagnier@yahoo.com
 */
@SuppressWarnings({"ClassWithoutToString"})
public final class IntrospectorTest extends TestCase {
  final Introspector introspector = new Introspector(Introspectee.class);
  final Introspectee introspectee = new Introspectee();

  public void testIntrospector() throws Exception {
    log().info(introspector.toString());
    assertEquals("mangotiger.lang.reflect.IntrospectorTest$Introspectee[" +
                 "\n\tboolean0[boolean, int]" +
                 "\n\tboolean1[java.lang.Boolean, java.lang.Integer]" +
                 "\n\tboolean2[boolean]" +
                 "\n\tbooleanPrimitive[boolean]" +
                 "\n\tcollection[java.util.Collection]" +
                 "\n\tdefaultValue[boolean]" +
                 "\n\tmyBigDecimal[java.math.BigDecimal]" +
                 "\n\tmyInt[int]" +
                 "\n\tmyInteger[java.lang.Integer]" +
                 "\n\tmyLong[java.lang.Long]" +
                 ']',
                 introspector.toString());
  }

  public void testBoolean() {
    introspector.set(introspectee, "boolean0", Boolean.TRUE);
    assertTrue(introspectee.isBoolean0);
    introspector.set(introspectee, "boolean0", 0);
    assertFalse(introspectee.isBoolean0);
    introspector.set(introspectee, "boolean1", Boolean.TRUE);
    assertTrue(introspectee.isBoolean1);
    introspector.set(introspectee, "boolean1", 0);
    assertFalse(introspectee.isBoolean1);
    assertFalse(introspectee.defaultValue);
    assertFalse(introspectee.isBoolean0);
  }

  public void testLong() {
    assertNull(introspectee.myLong);
    final Long longValue = 42L;
    introspector.set(introspectee, "myLong", longValue);
    assertEquals(longValue, introspectee.myLong);
  }

  public static void testFindMatchingMethod() throws Exception {
    final Map<String, Method> setters = new TreeMap<String, Method>();
    final Method setCollection = Introspectee.class.getMethod("setCollection", new Class[]{Collection.class});
    final Method setBooleanPrimative = Introspectee.class.getMethod("setBooleanPrimitive", new Class[]{Boolean.TYPE});
    setters.put(Collection.class.getName(), setCollection);
    setters.put(Boolean.TYPE.getName(), setBooleanPrimative);
    assertEquals(setCollection, Introspector.findMatchingMethod(setters, Set.class));
  }

  public void testSetPrimatives() {
    assertFalse(introspectee.booleanPrimitive);
    introspector.set(introspectee, "BooleanPrimitive", Boolean.TRUE);
    assertTrue(introspectee.booleanPrimitive);
    introspector.set(introspectee, "booleanPrimitive", null);
    assertFalse(introspectee.booleanPrimitive);
    introspector.set(introspectee, "myInt", 3);
    assertEquals(3, introspectee.myInt);
    introspector.set(introspectee, "myInt", null);
    assertEquals(0, introspectee.myInt);
  }

  private static Log log() {
    return LogFactory.getLog(IntrospectorTest.class);
  }

  static final class Introspectee {
    boolean isBoolean0;
    boolean isBoolean1;
    boolean boolean2;
    int myInt;
    Integer myInteger;
    Long myLong;
    BigDecimal myBigDecimal;
    boolean defaultValue;
    boolean booleanPrimitive;
    Collection collection;

    public boolean isBoolean2() {
      return boolean2;
    }

    public void setBoolean2(final boolean boolean2) {
      this.boolean2 = boolean2;
    }

    public boolean isDefaultValue() {
      return defaultValue;
    }

    public void setDefaultValue(final boolean defaultValue) {
      this.defaultValue = defaultValue;
    }

    public void setIsBoolean0(final boolean aBoolean) {
      isBoolean0 = aBoolean;
    }

    public void setMyBigDecimal(final BigDecimal myBigDecimal) {
      this.myBigDecimal = myBigDecimal;
    }

    public void setMyInt(final int myInt) {
      this.myInt = myInt;
    }

    public void setMyInteger(final Integer myInteger) {
      this.myInteger = myInteger;
    }

    public void setMyLong(final Long myLong) {
      this.myLong = myLong;
    }

    public void setIsBoolean0(final int integer) {
      setIsBoolean0(integer != 0);
    }

    public void setIsBoolean1(final Integer integer) {
      setIsBoolean1(integer == 0 ? Boolean.FALSE : Boolean.TRUE);
    }

    public void setIsBoolean1(final Boolean aBoolean) {
      isBoolean1 = aBoolean != null && aBoolean;
    }

    public void setBooleanPrimitive(final boolean booleanPrimitive) {
      this.booleanPrimitive = booleanPrimitive;
    }

    public void setCollection(final Collection collection) {
      //noinspection AssignmentToCollectionOrArrayFieldFromParameter
      this.collection = collection;
    }
  }
}