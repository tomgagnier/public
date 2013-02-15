package funsets

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

/**
 * This class is a test suite for the methods in object FunSets. To run
 * the test suite, you can either:
 *  - run the "test" command in the SBT console
 *  - right-click the file in eclipse and chose "Run As" - "JUnit Test"
 */
@RunWith(classOf[JUnitRunner])
class FunSetSuite extends FunSuite {

  /**
   * Link to the scaladoc - very clear and detailed tutorial of FunSuite
   *
   * http://doc.scalatest.org/1.8/index.html#org.scalatest.FunSuite
   *
   * Operators
   *  - test
   *  - ignore
   *  - pending
   */

  /**
   * Tests are written using the "test" operator and the "assert" method.
   */
  test("string take") {
    val message = "hello, world"
    assert(message.take(5) == "hello")
  }

  /**
   * For ScalaTest tests, there exists a special equality operator "===" that
   * can be used inside "assert". If the assertion fails, the two values will
   * be printed in the error message. Otherwise, when using "==", the test
   * error message will only say "assertion failed", without showing the values.
   *
   * Try it out! Change the values so that the assertion fails, and look at the
   * error message.
   */
  test("adding ints") {
    assert(1 + 2 === 3)
  }

  
  import FunSets._

  test("contains is implemented") {
    assert(contains(x => true, 100))
  }
  
  /**
   * When writing tests, one would often like to re-use certain values for multiple
   * tests. For instance, we would like to create an Int-set and have multiple test
   * about it.
   * 
   * Instead of copy-pasting the code for creating the set into every test, we can
   * store it in the test class using a val:
   * 
   *   val s1 = singletonSet(1)
   * 
   * However, what happens if the method "singletonSet" has a bug and crashes? Then
   * the test methods are not even executed, because creating an instance of the
   * test class fails!
   * 
   * Therefore, we put the shared values into a separate trait (traits are like
   * abstract classes), and create an instance inside each test method.
   * 
   */

  trait TestSets {
    val s1 = singletonSet(1)
    val s2 = singletonSet(2)
    val s3 = singletonSet(3)
    
    val s12 = union(s1, s2)
    val s13 = union(s1, s3)
    val s23 = union(s2, s3)
    
    val s123 = union(s12, s3)
    
    val isEven = (input:Int) => input % 2 == 0
    val isOdd  = (input:Int) => input % 2 == 1
    
    val increment = (input: Int) => input + 1
  }

  /**
   * This test is currently disabled (by using "ignore") because the method
   * "singletonSet" is not yet implemented and the test would fail.
   * 
   * Once you finish your implementation of "singletonSet", exchange the
   * function "ignore" by "test".
   */
  test("singletonSet(1) contains 1") {
    
    /**
     * We create a new instance of the "TestSets" trait, this gives us access
     * to the values "s1" to "s3". 
     */
    new TestSets {
      /**
       * The string argument of "assert" is a message that is printed in case
       * the test fails. This helps identifying which assertion failed.
       */
      assert(contains(s1, 1), "Singleton")
    }
  }

  test("union contains all elements") {
    new TestSets {
      val s = union(s1, s2)
      assert(contains(s, 1), "Union 1")
      assert(contains(s, 2), "Union 2")
      assert(!contains(s, 3), "Union 3")
    }
  }
  
  test("intersect") {
    new TestSets {
      assert(intersect(s1, s1)(1))
      assert(intersect(s1, s12)(1))
      assert(intersect(s1, s13)(1))
      assert(intersect(s1, s123)(1))
      
      assert(!intersect(s1, s1)(2))
      assert(!intersect(s1, s12)(2))
      assert(!intersect(s1, s13)(3))
      assert(!intersect(s1, s123)(3))
    }
  }
  
  test("diff") {
    new TestSets {
      assert(!diff(s123, s1)(0))
      assert(!diff(s123, s1)(1))
      assert(diff(s123, s1)(2))
      assert(diff(s123, s1)(3))
    }
  }
  
  test("filter") {
    new TestSets {
      assert(!filter(s123, isEven)(0))
      assert(!filter(s123, isEven)(1))
      assert(filter(s123, isEven)(2))
      assert(!filter(s123, isEven)(3))
    }
  }
  
  test("forall") {
    new TestSets {
      assert(forall(s13,   isOdd), "all odd")      
      assert(forall(s2,    isEven), "all even")
      assert(!forall(s123, isOdd), "mixed")
    }
  }
  
  test("exists") {
    new TestSets {
      assert(exists(s123, isEven))
      assert(exists(s123, isOdd))
      assert(!exists(s13, isEven))
    }
  }
  
  test("map") {
    new TestSets {
      assert(!map(s1, increment)(1))
      assert(map(s1, increment)(2))
    }
  }
}
