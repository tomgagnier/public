package interview.matrix;

import static java.lang.Math.*;

public class Cell implements Comparable<Cell> {

  public static Predicate<Cell> withIntensityGreaterThan(final int intensity) {
    return new Predicate<Cell>() {
      public boolean evaluate(Cell cell) {
        return cell.intensity > intensity;
      }
    };
  }

  public final int row;
  public final int column;
  public final int intensity;

  public static Cell cell(int row, int column, int intensity) {
    return new Cell(row, column, intensity);
  }

  private Cell(int row, int column, int intensity) {
    this.row       = row;
    this.column    = column;
    this.intensity = intensity;
  }

  public boolean adjacentTo(Cell that) {
    return abs(this.row - that.row) < 2 && abs(this.column - that.column) < 2;
  }

  public String toString() {
    return "(" + row + ',' + column + ',' + intensity + ')';
  }

  public boolean equals(Object obj) {
    return obj instanceof Cell && compareTo((Cell)obj) == 0;
  }

  public int hashCode() {
    return intensity + 17 * (column + 17 * row);
  }

  public int compareTo(Cell that) {
    return this.row == that.row ? this.column == that.column ?
        this.intensity - that.intensity :
        this.column    - that.column :
        this.row       - that.row;
  }
}
