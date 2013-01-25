/*
 * $Source: $
 *
 * Copyright (C) 2002-2005, Thomas Robert Gagnier, Jr., All Rights Reserved.
 * Unauthorized use, disclosure or reproduction of this source code is strictly
 * prohibited by United States copyright law and international treaty provisions.
 * Use of source code requires an appropriate source license.
 */
package mangotiger.util;

import java.util.ArrayList;
import java.util.List;

/**
 * An generic array.  Useful for converting arrays to lists.
 * @author Tom Gagnier
 */
@SuppressWarnings({"PublicMethodNotExposedInInterface", "AssignmentToCollectionOrArrayFieldFromParameter"})
public class Array<T> {

  final T[] array;

  public Array(final T ... array) {
    this.array = array;
  }

  public List<T> asList() {
    if (array == null) return new ArrayList<T>(0);
    final List<T> list = new ArrayList<T>(array.length);
    for (T t : array) list.add(t);
    return list;
  }

  @Override public String toString() {
    return asList().toString();
  }
}
