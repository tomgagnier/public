/*
 * $Source: $
 *
 * Copyright (C) 2002-2005, Thomas Robert Gagnier, Jr., All Rights Reserved.
 * Unauthorized use, disclosure or reproduction of this source code is strictly
 * prohibited by United States copyright law and international treaty provisions.
 * Use of source code requires an appropriate source license.
 */
package mangotiger.poker.events;

/** @author Tom Gagnier */
public final class Table {

  private final String table;

  @Parameters({Type.table}) public Table(final String table) {
    this.table = table == null ? "null" : table;
  }

  public String getTable() {
    return table;
  }

  @Override public boolean equals(final Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    final Table that = (Table)obj;
    return table.equals(that.table);
  }

  @Override public int hashCode() {
    return table.hashCode();
  }

  @Override public String toString() {
    return "Table{name=" + table + '}';
  }
}
