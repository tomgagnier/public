package mangotiger.util;

import java.util.Iterator;

/**
 * Iterator helpers.
 * @author tom_gagnier@yahoo.com
 */
public interface Iterators {
  /** An empty iterator. */
  Iterator EMPTY = new Iterator() {
    public void remove() {}

    public boolean hasNext() {return false;}

    public Object next() {return null;}

    ;
  };
}
