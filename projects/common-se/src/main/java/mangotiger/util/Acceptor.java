package mangotiger.util;

/**
 * A general acceptor.
 * @author tom_gagnier@yahoo.com
 */
public interface Acceptor {
  /**
   * Accept this object.
   * @param object the object to test.
   * @return true if accepted.
   */
  boolean accept(Object object);
}
