/*
 * $Source: $
 *
 * Copyright (C) 2002-2005, Thomas Robert Gagnier, Jr., All Rights Reserved.
 * Unauthorized use, disclosure or reproduction of this source code is strictly
 * prohibited by United States copyright law and international treaty provisions.
 * Use of source code requires an appropriate source license.
 */
package mangotiger.text;

import java.util.ArrayList;
import java.util.List;

/** @author Tom Gagnier */
public class TextTable {
  private List<Row> rows = new ArrayList<Row>();
  private List<Integer> widths = new ArrayList<Integer>();

  public Row row() {
    final Row row = new Row();
    rows.add(row);
    return row;
  }

  private void setWidth(final int column, final int width) {
    while (widths.size() <= column) widths.add(0);
    if (widths.get(column) < width) widths.set(column, width);
  }

  public String toString() {
    final StringBuilder s = new StringBuilder();
    for (Row row : rows) {
      for (int i = 0; i < row.cells.size(); ++i) {
        if (i != 0) s.append("  ");
        final Cell cell = row.cells.get(i);
        final int pad = widths.get(i) - cell.value.length();
        switch (cell.justify) {
        case center:
          for (int j = 0; j < pad / 2; ++j) s.append(' ');
          s.append(cell);
          for (int j = pad / 2; j < pad; ++j) s.append(' ');
          break;
        case left:
          s.append(cell);
          for (int j = 0; j < pad; ++j) s.append(' ');
          break;
        case right:
          for (int j = 0; j < pad; ++j) s.append(' ');
          s.append(cell);
          break;
        }
      }
      s.append('\n');
    }
    s.setLength(s.length() - 1);
    return s.toString();
  }

  public class Row {
    private final List<Cell> cells = new ArrayList<Cell>();

    public Row add(final Object cell, final Justify justify) {
      final String s = cell.toString();
      setWidth(cells.size(), s.length());
      cells.add(new Cell(s, justify));
      return this;
    }

    public Row addLeft(final Object cell) {
      return add(cell, Justify.left);
    }

    public Row addCentered(final Object cell) {
      return add(cell, Justify.center);
    }

    public Row addRight(final Object cell) {
      return add(cell, Justify.right);
    }
  }

  static class Cell {
    final String value;
    final Justify justify;

    Cell(final String value, final Justify justify) {
      this.value = value;
      this.justify = justify;
    }

    public String toString() {
      return value;
    }
  }
}
