package interview.matrix;

import static interview.matrix.Matrix.*;
import java.util.*;

public class Main {

  private static List<Set<Cell>> regionsIn(Set<Cell> cells) {
    List<Set<Cell>> regions = new ArrayList<Set<Cell>>();
    while (!cells.isEmpty()) {
      Set<Cell> region = region(cells);
      regions.add(region);
      cells.removeAll(region);
    }
    return regions;
  }

  private static Set<Cell> region(Set<Cell> cells) {
    TreeSet<Cell> region = new TreeSet<Cell>();
    Cell seed = cells.iterator().next();
    region.add(seed);
    return region;
  }

  public static void main(String[] args) {
  }

}

