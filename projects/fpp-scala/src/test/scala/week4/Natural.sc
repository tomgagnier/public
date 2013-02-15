// Peano numbers

object Natural {

	Zero                                      //> res0: Natural.Zero.type = 0
	val one = Zero.successor                  //> one  : Natural.Successor = 1
	val two = one.successor                   //> two  : Natural.Successor = 2
	val three = two.successor                 //> three  : Natural.Successor = 3
	val four = three.successor                //> four  : Natural.Successor = 4
	
	four - one                                //> res1: Natural.Natural = 3
	four - three                              //> res2: Natural.Natural = 1
	four - four                               //> res3: Natural.Natural = 0
	
	abstract class Natural {
	  def isZero: Boolean
	  def predecessor: Natural
	  def successor = new Successor(this)
	  def + (that: Natural): Natural
	  def - (that: Natural): Natural
	
	  override def toString: String = {
	    def iterate(value: Natural, count: Int): Int =
	      if (value.isZero) count
	      else iterate(value.predecessor, count + 1)
	    
	    iterate(this, 0).toString
	  }
	}
	
	object Zero extends Natural {
	  val isZero = true
	  def predecessor = throw new UnsupportedOperationException
	  def + (that: Natural) = that
	  def - (that: Natural) = if (that.isZero) Zero else throw new Error("negative")
	}
	
	class Successor(val predecessor: Natural) extends Natural {
	  val isZero = false
	  def + (that: Natural) = new Successor(predecessor + that)
	  def - (that: Natural): Natural = if (that.isZero) this else predecessor - that.predecessor
	}
}