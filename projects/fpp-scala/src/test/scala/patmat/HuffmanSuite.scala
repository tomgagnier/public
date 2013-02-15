package patmat

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import patmat.Huffman._

@RunWith(classOf[JUnitRunner])
class HuffmanSuite extends FunSuite {
  val (a, b, c, d, e, f, g, h) = ('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h')

  trait TestTrees {
    val t1 = Fork(Leaf(a, 2), Leaf(b, 3), List(a, b), 5)
    val t2 = Fork(Fork(Leaf(a, 2), Leaf(b, 3), List(a, b), 5), Leaf(d, 4), List(a, b, d), 9)
  }

  test("weight of a larger tree") {
    new TestTrees {
      assert(weight(t1) === 5)
    }
  }

  test("chars of a larger tree") {
    new TestTrees {
      assert(chars(t2) === List(a, b, d))
    }
  }

  test("times") {
    def contentsMatch(expected: List[(Char, Int)], actual: List[(Char, Int)]): Boolean =
      expected.sortBy {
        p => p._1
      }.equals(actual.sortBy {
        element => element._1
      })

    assert(contentsMatch(List((a, 2), (b, 1)), times(List(a, b, a))))
    assert(contentsMatch(List((a, 2), (b, 1), (c, 3)), times(List(c, a, b, c, a, c))))
  }

  test("makeOrderedLeafList for some frequency table") {
    assert(makeOrderedLeafList(List((a, 2), (e, 1), (c, 3))) === List(Leaf(e, 1), Leaf(a, 2), Leaf(c, 3)))
  }

  test("singleton") {
    assert(true === singleton(List(Leaf('a', 1))))

    assert(false === singleton(List(Leaf('a', 1), Leaf('b', 2))))
  }

  test("combine of some leaf list") {
    assert(combine(List(Leaf(e, 1), Leaf(a, 2), Leaf(c, 4))) ===
      List(Fork(Leaf(e, 1), Leaf(a, 2), List(e, a), 3), Leaf(c, 4)))
  }

  test("decode and encode a very short text should be identity") {
    new TestTrees {
      assert(decode(t1, encode(t1)("ab".toList)) === "ab".toList)
    }
  }

  test("quick eecode and encode a very short text should be identity") {
    new TestTrees {
      assert(decode(t1, quickEncode(t1)("ab".toList)) === "ab".toList)
    }
  }

  test("bad request") {
    treeFor("")
  }

  def treeFor(characters: String) {
    until(singleton, combine)(frequenciesFor(characters))
  }

  def frequenciesFor(characters: String): List[Huffman.Leaf] = {
    makeOrderedLeafList(times(characters.toList))
  }
}
