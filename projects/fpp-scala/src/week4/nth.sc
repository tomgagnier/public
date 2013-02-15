import week4._

object nth {
	def nth[Type](n: Int, xs: List[Type]): Type =
	  if (xs.isEmpty) throw new IndexOutOfBoundsException
	  else if (n == 0) xs.head
	  else nth(n - 1, xs.tail)                //> nth: [Type](n: Int, xs: week4.List[Type])Type
	  
	val list = new Cons(1, new Cons(2, new Cons(3, new Nil)))
                                                  //> list  : week4.Cons[Int] = week4.Cons@79a5f739
	
	nth(2, list)                              //> res0: Int = 3
	
	
}