package mangotiger.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Combinations<T> implements Iterator<List<T>> {
  private final List<T> list;
  private final List<T> combination;
  private final List<T> rest;
  private final int size;
  private int index;

  public Combinations(final List<T> list) {
    this.list = new ArrayList<T>(list);
    size = pow(2, list.size());
    combination = new ArrayList<T>(size);
    rest = new ArrayList<T>(size);
  }

  public Combinations(final T ... args) {
    this(new Array<T>(args).asList());
  }

  public boolean hasNext() {
    final boolean hasNext = index < size;
    index %= size;
    return hasNext;
  }

  @SuppressWarnings({"ReturnOfCollectionOrArrayField"})
  public List<T> next() {
    combination.clear();
    rest.clear();
    int i = index;
    ++index;
    for (T t : list) {
      if (i % 2 == 1) {
        combination.add(t);
      } else {
        rest.add(t);
      }
      i >>= 1;
    }
    return combination;
  }

  public void remove() {
    throw new UnsupportedOperationException("remove is unsupported");
  }

  public int size() {
    return size;
  }

  public int index() {
    return index;
  }

  @SuppressWarnings({"ReturnOfCollectionOrArrayField"})
  public List rest() {
    return rest;
  }

  private static int pow(final int base, final int exponent) {
    int pow = 1;
    for (int i = 0; i < exponent; ++i) {
      pow *= base;
    }
    return pow;
  }

  @Override public String toString() {
    return "Combinations{list=" + list + ",combination=" + combination + ",rest=" + rest() + ",size=" + size() +
           ",index=" + index() + '}';
  }
}
