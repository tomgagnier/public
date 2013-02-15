package recfun

object Main {
  def main(args: Array[String]) {
    printPascalsTriangle(10)
  }

  def printPascalsTriangle(dimension: Int) {
    def row(r: Int) = (0 to r).map {c => pascal(c, r)}

    println("Pascal's Triangle")
    (0 until dimension).foreach(r => println(row(r).foldLeft("") { (a, b) => a + " " + b }))
  }

   // Exercise 1 - Calculate an element in Pascal's Triangle (0 based indexes)
  def pascal(c: Int, r: Int): Int =
    if (c == 0 || c == r) 1
    else pascal(c - 1, r - 1) + pascal(c, r - 1)

  // Exercise 2 - Determine if the parenthesis in a character list are balanced
  def balance(chars: List[Char]): Boolean = {
    def valueOf(c: Char) =
      if (c == ')') -1
      else if (c=='(') 1
      else 0

    def balance(count: Int, remaining: List[Char]) : Int =
      if (count < 0) -1
      else if (remaining.isEmpty) count
      else balance(valueOf(remaining.head) + count, remaining.tail)

    balance(0, chars) == 0
  }

  // Exercise 3 - Count the number of ways to make change for a given amount of money and a list of coin denominations.
  def countChange(money: Int, coins: List[Int]): Int =
    if (money == 0) 1
    else if (money < 0) 0
    else if (coins.isEmpty) 0
    else countChange(money, coins.tail) + countChange(money - coins.head, coins)
}
