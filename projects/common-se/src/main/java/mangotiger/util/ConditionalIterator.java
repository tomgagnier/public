package mangotiger.util;

import java.util.Collection;
import java.util.Iterator;

/** A conditional iterator. */
public final class ConditionalIterator implements Iterator {
  private final Iterator iterator;
  private final Acceptor conditional;
  private Object next;

  /**
   * Construct from a collection of session IDs.
   * @param collection a collection of session IDs.
   * @param acceptor   determines if object is collection is skipped during iteration.
   */
  public ConditionalIterator(final Collection collection, final Acceptor acceptor) {
    iterator = collection.iterator();
    conditional = acceptor;
    next = findNextAcceptableObject();
  }

  public void remove() {
    iterator.remove();
  }

  public boolean hasNext() {
    return next != null;
  }

  public Object next() {
    final Object current = next;
    next = findNextAcceptableObject();
    return current;
  }

  private Object findNextAcceptableObject() {
    while (iterator.hasNext()) {
      final Object object = iterator.next();
      if (conditional.accept(object)) {
        return object;
      }
    }
    return null;
  }

  @Override public String toString() {
    return "ConditionalIterator{iterator=" + iterator + ",conditional=" + conditional + ",next=" + next + '}';
  }
}
