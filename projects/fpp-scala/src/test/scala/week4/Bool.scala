package week4

abstract class Bool {
  
  def ifThenElse[T](t: => T, e: => T): T
  
  def && (x: => Bool): Bool = ifThenElse(x, False)  
  def || (x: => Bool): Bool = ifThenElse(True, x)  
  def unary_! : Bool = ifThenElse(False, True)
  
  def != (x:Bool): Bool = ifThenElse(x, !x)
  def == (x:Bool): Bool = ifThenElse(!x, x)
  
  def < (that:Bool): Bool = ifThenElse(False, that)
  
  object False extends Bool {
    def ifThenElse[T](t: => T, e: => T): T = e
  }
  
  object True extends Bool {
    def ifThenElse[T](t: => T, e: => T): T = t
  }
  
}
