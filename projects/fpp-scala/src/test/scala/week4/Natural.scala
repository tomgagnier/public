package week4

abstract class Natural {
  def isZero: Boolean
  def predecessor: Natural
  def successor: Natural
  def + (that: Natural)
  def - (that: Natural)

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
  def successor = new Successor(this)
  def + (that: Natural) = that
  def - (that: Natural) =
    if (that.isZero) Zero
    else throw new UnsupportedOperationException
}

class Successor(val predecessor: Natural) extends Natural {  
  val isZero = false
  def successor = new Successor(this)
  def + (that: Natural) = {
    def iterate(value: Natural, count: Natural): Natural =
      if (count.isZero) value
      else iterate(value.successor, count.predecessor)
    iterate(this, that)
  }
  def - (that: Natural) = {
    def iterate(value: Natural, count: Natural): Natural =
      if (count.isZero) value
      else iterate(value.predecessor, count.predecessor)
    iterate(this, that)
  }
  
}