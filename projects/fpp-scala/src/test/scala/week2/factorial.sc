package week2

object week2 {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet
  
  def factorial(n: Int) : Int = {
    def iterate(n: Int, accumulator: Int): Int =
      if (n < 2) accumulator
      else iterate(n - 1, n * accumulator)
		iterate(n, 1)
  }                                               //> factorial: (n: Int)Int
  
  factorial(10)                                   //> res0: Int = 3628800
}