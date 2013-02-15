package funsets

import collection.immutable.IndexedSeq

/** 2. Purely Functional Sets. */
object FunSets {

  /** We represent a set by its characteristic function, i.e. its `contains` predicate. */
  type Set = Int => Boolean

  /** Indicates whether a set contains a given element. */
  def contains(set: Set, element: Int): Boolean =
    set(element)

  /** Returns the set of the one given element. */
  def singletonSet(element: Int): Set =
    (candidate) => candidate == element

  /** Returns the union of the two given sets, the sets of all elements that are in either set1 or set2. */
  def union(set1: Set, set2: Set): Set =
    (candidate) => contains(set1, candidate) || contains(set2, candidate)

  /** Returns the intersection of the two given sets, the set of all elements that are both in set1 and set2. */
  def intersect(set1: Set, set2: Set): Set =
    (candidate) => contains(set1, candidate) && contains(set2, candidate)

  /** Returns the difference of the two given sets, the set of all elements of set1 that are not in set2. */
  def diff(set1: Set, set2: Set): Set =
    (candidate) => contains(set1, candidate) && !contains(set2, candidate)

  /** Returns the subset of set for which predicate holds. */
  def filter(set: Set, predicate: Int => Boolean): Set =
    (candidate) => contains(set, candidate) && predicate(candidate)

  /** The bounds for `forall` and `exists` are +/- 1000. */
  val bound = 1000

  /** Returns whether all bounded integers within set satisfy predicate. */
  def forall(set: Set, predicate: Int => Boolean): Boolean = {
    def iterate(candidate: Int): Boolean =
      if (candidate > bound) true
      else if (contains(set, candidate) && !predicate(candidate)) false
      else iterate(candidate + 1)
    iterate(-bound)
  }

  /** Returns whether there exists a bounded integer within set that satisfies predicate. */
  def exists(set: Set, predicate: Int => Boolean): Boolean = {
    !forall(set, (candidate) => !predicate(candidate))
  }

  /** Returns a set transformed by applying function to each element of set. */
  def map(set: Set, function: Int => Int): Set = {
    def combine(candidate: Int, accumulator: Set) =
      if (contains(set, candidate)) union(accumulator, singletonSet(function(candidate)))
      else accumulator

    def iterate(candidate: Int, accumulator: Set): Set =
      if (candidate > bound) accumulator
      else iterate(candidate + 1, combine(candidate, accumulator))

    iterate(-bound, (emptySet: Int) => false)
  }

  def elements(set: Set): IndexedSeq[Int] =
    for (i <- -bound to bound if contains(set, i)) yield i

  def toString(set: Set): String =
    elements(set).mkString("{", ",", "}")

  def printSet(set: Set) {
    println(toString(set))
  }
}
