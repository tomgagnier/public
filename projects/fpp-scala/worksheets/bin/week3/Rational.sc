package week3

object RationalTest {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet
  
  val r1 = new Rational(1, 3)                     //> r1  : week3.Rational = 1/3
  val r2 = new Rational(5, 7)                     //> r2  : week3.Rational = 5/7
  val r3 = new Rational(3, 2)                     //> r3  : week3.Rational = 3/2
  
  
  r1 + r2                                         //> res0: week3.Rational = 22/21
  r1 + r3                                         //> res1: week3.Rational = 11/6
  r1 - r2 - r3                                    //> res2: week3.Rational = -79/42
  r1 + r1                                         //> res3: week3.Rational = 2/3

  r1 < r2                                         //> res4: Boolean = true
  
  r1.max(r2)                                      //> res5: week3.Rational = 5/7
}

class Rational(x: Int, y: Int) {
  require(y != 0)
  
  def this(x: Int) = this(x, 1)
  
	private def gcd(a: Int, b: Int): Int = if (b == 0) a else gcd(b, a % b)
	private val g = gcd(x, y)
	
	val numerator   = x / g
	val denominator = y /g
	
	def unary_- = new Rational(-numerator, denominator)
	def +(that: Rational) = new Rational(this.numerator * that.denominator + that.numerator * this.denominator,
	                                     this.denominator * that.denominator)
	def -(that: Rational) = this + -that
	def <(that: Rational) = this.numerator * that.denominator < that.numerator * this.denominator
	
	def max(that: Rational) = if (this < that) that else this
	
	override def toString = numerator + "/" + denominator
}
	 