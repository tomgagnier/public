package interview.matrix;

import static interview.matrix.Cell.cell;
import static java.util.Arrays.*;

import java.util.*;

public class Matrix {
  public final SortedSet<Cell> cells;

  public static Matrix row(int... intensities) {
    SortedSet<Cell> cells = new TreeSet<Cell>();
    for (int i = 0; i < intensities.length; ++i)
      cells.add(cell(0, i, intensities[i]));
    return new Matrix(cells);
  }

  public static Matrix matrix(Cell... cells) {
    return new Matrix(new TreeSet<Cell>(asList(cells)));
  }

  private Matrix(SortedSet<Cell> cells) {
    this.cells = cells;
  }

  public boolean equals(Object object) {
    return object instanceof Matrix && equals((Matrix) object);
  }

  public boolean equals(Matrix that) {
    return this.cells.equals(that.cells);
  }

  public int hashCode() {
    return cells.hashCode();
  }

  public String toString() {
    StringBuilder builder = new StringBuilder();
    int row = 0;
    int column = 0;
    for (Cell cell : cells) {
      while (row < cell.row) {
        builder.append('\n');
        column = 0;
        ++row;
      }
      while (column < cell.column) {
        builder.append(column == 0 ? "" : " ").append(' ');
        ++column;
      }
      builder.append(column == 0 ? "" : " ").append(cell.intensity);
      ++column;

    }
    return builder.toString();
  }

  public Matrix add(Matrix matrix) {
    SortedSet<Cell> newCells = new TreeSet<Cell>(cells);
    for (Cell cell : matrix.cells)
      newCells.add(cell(cell.row + cells.last().row + 1, cell.column, cell.intensity));
    return new Matrix(newCells);
  }
}
