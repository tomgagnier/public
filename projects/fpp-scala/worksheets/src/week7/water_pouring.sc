package week7

object water_pouring {


  val problem = new Pouring(Vector(4, 9))         //> problem  : week7.Pouring = week7.Pouring@423e5d1

  problem.moves                                   //> res0: scala.collection.immutable.IndexedSeq[Product with Serializable with we
                                                  //| ek7.water_pouring.problem.Move] = Vector(Empty(0), Empty(1), Fill(0), Fill(1)
                                                  //| , Pour(0,1), Pour(1,0))
  problem.solutions(6)                            //> res1: Stream[week7.water_pouring.problem.Path] = Stream(Fill(1) Pour(1,0) Em
                                                  //| pty(0) Pour(1,0) Empty(0) Pour(1,0) Fill(1) Pour(1,0)--> Vector(4, 6), ?)
  
}