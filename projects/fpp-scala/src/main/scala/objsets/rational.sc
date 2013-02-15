object rational {

  val r1 = new Rational(1, 3)
  val r2 = new Rational(5, 7)
  val r3 = new Rational(3, 2)
  
  println(r1 - r2 - r3)

}

class Rational(val numerator: Int, val denominator: Int) {
	def unary_- = new Rational(-numerator, denominator)
	def +(that: Rational) = new Rational(this.numerator * that.denominator + that.numerator * this.denominator,
	                                     this.denominator * that.denominator)
	def -(that: Rational) = this + -that
	
	override def toString = numerator + "/" + denominator
}
	 