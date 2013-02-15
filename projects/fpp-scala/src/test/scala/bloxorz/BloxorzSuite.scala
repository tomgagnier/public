package bloxorz

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class BloxorzSuite extends FunSuite {

  val level = """ooo-------
                |oSoooo----
                |ooooooooo-
                |-ooooooooo
                |-----ooToo
                |------ooo-""".stripMargin

  val terrain = Terrain(level)

  test("terrain toString") {
    assert(level === terrain.toString)
  }

  test("terrain start") {
    assert(terrain.start === Position(1, 4))
  }

  test("terrain target") {
    assert(terrain.target === Position(7, 1))
  }

  test("terrain holds") {
    assert(!terrain.holds(Block(1, -1)))
    assert(!terrain.holds(Block(1,  0)))
    assert(!terrain.holds(Block(1,  1)))
    assert( terrain.holds(Block(1,  2)))
    assert( terrain.holds(Block(1,  3)))
    assert( terrain.holds(Block(1,  4)))
    assert( terrain.holds(Block(1,  5)))
    assert(!terrain.holds(Block(1,  6)))
    assert(!terrain.holds(Block(1,  7)))
  }

  test("standing block") {
    val block: Block = Block(0, 0)

    assert( block.standing)
    assert(!block.horizontal)
    assert(!block.vertical)

    assert(block + Left  === Block(-2,  0, -1,  0))
    assert(block + Right === Block( 1,  0,  2,  0))
    assert(block + Up    === Block( 0,  1,  0,  2))
    assert(block + Down  === Block( 0, -2,  0, -1))
  }

  test("horizontal block moves") {
    val block: Block = Block(0, 0, 1, 0)

    assert(!block.standing)
    assert( block.horizontal)
    assert(!block.vertical)

    assert(block + Left  === Block(-1,  0))
    assert(block + Right === Block( 2,  0))
    assert(block + Up    === Block( 0,  1, 1,  1))
    assert(block + Down  === Block( 0, -1, 1, -1))
  }

  test("vertical block moves") {
    val block: Block = Block(0, 0, 0, 1)

    assert(!block.standing)
    assert(!block.horizontal)
    assert( block.vertical)

    assert(block + Left  === Block(-1,  0, -1, 1))
    assert(block + Right === Block( 1,  0,  1, 1))
    assert(block + Up    === Block( 0,  2))
    assert(block + Down  === Block( 0, -1))
  }

  test("solver pathsToGoal") {
    assert(7 === Solver(terrain).pathsToGoal.head.moves.size)
  }
}
