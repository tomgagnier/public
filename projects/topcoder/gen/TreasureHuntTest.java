import mangotiger.topcoder.TopCoderTestCase;

/**
 * Problem Statement
 * 
 * Hurray! You have found a map to a hidden treasure on an island somewhere
 * in the Carribean. It's a classic pirate treasure map, with a big X marking
 * the spot of the treasure. It also has instructions describing how to walk
 * to reach the X, like "north 2 paces", "east 1 pace", etc. However, the
 * instructions lack one vital piece of information: It doesn't say where
 * on the island you should start from! Since you suspect the treasure is
 * buried deep and that the X on the map is only a rough estimation, you
 * might have to dig in a lot of places before finding the treasure. So before
 * you start to dig, you pick up your laptop computer in order to determine
 * where on the island the treasure is most likely to be buried.
 * 
 * You assume that the intended start location is somewhere on the beach,
 * and that if you follow the walking instructions, you should never have
 * to walk across water. If several such starting positions exist, the position
 * that will cause the treasure to be closest (Euclidean distance) to the
 * estimated treasure position is considered most likely (see example 0).
 * If there is a tie, select the northernmost position of these. If there
 * is still a tie, select the westernmost position of these.
 * 
 * The island will be given as a String[], where a dot ('.') represents water,
 * uppercase letter 'O' represents a part of the island, and uppercase letter
 * 'X' marks the estimated location of the treasure (this position is of
 * course also part of the island). A square is only considered to be a beach
 * if it's part of the island and is adjacent (in any of the four cardinal
 * directions) to a water square. Squares outside the map are considered
 * to be water squares as well (they are part of the ocean). Below is an
 * example of a valid island.
 * 
 * {"..OOOO..",
 * 
 * ".OOOO...",
 * 
 * "OOXOOOOO",
 * 
 * "OOOOOOO.",
 * 
 * ".OOOO...",
 * 
 * "..OOO..."}
 * 
 * The island will always be connected. This means that if you stand somewhere
 * on the island, it will be possible to reach every part of the island by
 * only walking on land in any of the four cardinal directions. Also, there
 * will be no lakes, which means that from any water square it will be possible
 * to go in cardinal directions on other water squares until the edge of
 * the map is reached.
 * 
 * The walking instructions will be given as a String[] where each element
 * is in the form (quotes for clarity only) "<direction> <paces>". Direction
 * will be either 'N' (decreasing y), 'S' (increasing y), 'E' (increasing
 * x) or 'W' (decreasing x). Paces will be an integer between 1 and 9, inclusive.
 * Walking one pace corresponds to moving one square.
 * 
 * Create a class TreasureHunt containing the method findTreasure which takes
 * a String[] island and a String[] instructions describing the island and
 * the instructions, respectively, in the formats above. The method should
 * return a int[] containing the coordinates on the map where the treasure
 * is most likely to be buried. The first element should be the x-coordinate
 * (the first column being 0) and the second element the y-coordinate (the
 * first row being 0). If the treasure can't be on the island, return a int[]
 * containing zero elements (see example 4).
 *
 * int[] findTreasure(String[] island, String[] instructions)
 * 
 * (be sure your method is public)
 * 
 * Notes
 * 
 * All land cells that are on the edge of the grid are considered to be a
 * beach since they're adjacent to squares outside the grid, which are water
 * (see example 2).
 * 
 * The Euclidean distance between two points x1,y1 and x2,y2 is sqrt((x1-x2)*(x1-x2)
 * + (y1-y2)*(y1-y2)).
 * 
 * East is the positive x-direction and North is the negative y-direction.
 * 
 * Constraints
 * 
 * island will contain between 1 and 50 elements, inclusive.
 * 
 * Each element in island will contain between 1 and 50 characters, inclusive.
 * 
 * All elements in island will contain the same number of characters.
 * 
 * The characters in each element in island will be '.', 'O' or 'X'.
 * 
 * Exactly one character in island will be a 'X'.
 * 
 * The island will be connected.
 * 
 * The island will not contain any lakes.
 * 
 * instructions will contain between 1 and 50 elements, inclusive.
 * 
 * Each element in instructions will be in the form "<direction> <paces>"
 * where direction is 'N', 'S', 'E' or 'W' and paces is an integer between
 * 1 and 9 (without leading zeros), inclusive.
 */
@SuppressWarnings({"ClassWithoutToString", "ClassWithoutPackageStatement", "MagicNumber"})
public class TreasureHuntTest extends TopCoderTestCase {
  TreasureHunt treasureHunt = new TreasureHunt();

  /**
   * The treasure can't be buried where the 'X' is, because then we would have
   * to walk on water.
   */
  public void test0() throws Exception {
    final int[] expect = new int[] { 3,  2 };
    final String[] island = new String[]{"..OOOO..",".OOOO...","OOXOOOOO","OOOOOOO.",".OOOO...","..OOO..."};
    final String[] instructions = new String[]{"W 3","S 1","E 2"};
    final int[] actual = treasureHunt.findTreasure(island, instructions);
    assertEquals(expect, actual);
  }

  /**
   * Notice that you must start from the beach.
   */
  public void test1() throws Exception {
    final int[] expect = new int[] { 3,  4 };
    final String[] island = new String[]{".......",".OOOOO.",".OOOOO.",".OOXOO.",".OOOOO.",".OOOOO.","......."};
    final String[] instructions = new String[]{"N 1"};
    final int[] actual = treasureHunt.findTreasure(island, instructions);
    assertEquals(expect, actual);
  }

  public void test2() throws Exception {
    final int[] expect = new int[] { 1,  0 };
    final String[] island = new String[]{"OOOOOOOOOOO.","OX..........","OOOOOOOOOOO."};
    final String[] instructions = new String[]{"W 2"};
    final int[] actual = treasureHunt.findTreasure(island, instructions);
    assertEquals(expect, actual);
  }

  public void test3() throws Exception {
    final int[] expect = new int[] { 3,  1 };
    final String[] island = new String[]{"....OO.","..OOXOO","OOOO...",".OOOOOO","...OOOO",".OOOOO.","..OOO.."};
    final String[] instructions = new String[]{"N 1","E 1","N 4"};
    final int[] actual = treasureHunt.findTreasure(island, instructions);
    assertEquals(expect, actual);
  }

  public void test4() throws Exception {
    final int[] expect = new int[] { };
    final String[] island = new String[]{"X"};
    final String[] instructions = new String[]{"N 1","E 1","S 1","W 1"};
    final int[] actual = treasureHunt.findTreasure(island, instructions);
    assertEquals(expect, actual);
  }

  public void test5() throws Exception {
    final int[] expect = new int[] { 10,  6 };
    final String[] island = new String[]{".................O..","..OO.......OOOOO.O..","..OOO..OOO.OOOOOOOO.","..OOOOOOOOOOOOOOOOO.","..OOOOOOOOOOOOOOOOO.","...OOOOOOOOOOOOOOOO.","OO.OOOOOOOXOOOOOO...",".OOOOOOO..OOOOOOOO..","OOOOOOOOO..OOOOOOOO.","OOOOOOOOO..OOOOOOO..",".OOOOOOOOO..........","OOOOOOOOOOOOOOOOOO..","..OOOOOOOOOOOOOOO...","OOOOOOOOOOOOOOOOO...",".OOOOOOOOOOOOOOOOOOO","OOOOOOOOOOOOOOOOOO..","..OOOOOOOOOOOOOOOOO.","OOOOO.OOOOOO..OOO...","O..OO.OOOO.....OOO..","O......O.OO......OO."};
    final String[] instructions = new String[]{"N 2","E 3","N 4","E 2","S 1","W 2","E 9","E 2","N 3","W 5","N 1","W 6","N 6","S 1","S 1","E 7"};
    final int[] actual = treasureHunt.findTreasure(island, instructions);
    assertEquals(expect, actual);
  }

}
