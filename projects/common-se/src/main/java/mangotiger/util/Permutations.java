package mangotiger.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/** @author tom_gagnier@yahoo.com */
public class Permutations<T> implements Iterator<List<T>> {
  private int count;
  private final List<T> list;

  public Permutations(final List<T> list) {
    this.list = new ArrayList<T>(list);
  }

  public Permutations(final T ... list) {
    this.list = new ArrayList<T>(list.length);
    for (T t : list) this.list.add(t);
  }

  @Override public String toString() {
    return "Permutations{count=" + count + ",list=" + list + '}';
  }

  public boolean hasNext() {
    return count == 0 || isReverseOrderedList();
  }

  private boolean isReverseOrderedList() {
    for (int i = 0; i < list.size() - 1; ++i) {
      if (compareTo(i, i + 1) < 0) {
        return true;
      }
    }
    return false;
  }

  @SuppressWarnings({"ReturnOfCollectionOrArrayField"})
  public List<T> next() {
    int i = list.size() - 1;
    if (0 >= i || count++ == 0) {
      return list;
    }
    while (true) {
      final int p = i--;
      if (compareTo(i, p) < 0) {
        final int j = reverseFindNotLessThan(i, list.size());
        Collections.swap(list, i, j);
        Collections.reverse(list.subList(p, list.size()));
        break;
      }
      if (i == 0) {
        Collections.reverse(list);
        break;
      }
    }
    return list;
  }

  private int reverseFindNotLessThan(final int begin, final int end) {
    int i = end - 1;
    while (begin != i && compareTo(begin, i) >= 0) {
      --i;
    }
    return i;
  }

  private int compareTo(final int left, final int right) {
    //noinspection unchecked
    return ((Comparable)list.get(left)).compareTo(list.get(right));
  }

  public void remove() {
    throw new UnsupportedOperationException("remove is unsupported");
  }
}
