object PatternMatching {
  trait Exp {
  	override def toString(): String = this match {
  		case Number(n) => n.toString
  		case Sum(left, right) => left.toString + " + " + left.toString
    }
  }
  
  case class Number(val n:Int) extends Exp {}
  
  case class Sum(val left:Exp, val right:Exp) extends Exp {}

  val one = Number(1)
  val two = Number(2)
  
  val sum = Sum(one, two)
}