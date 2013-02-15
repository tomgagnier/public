object streams {
	def streamRange(low: Int, high: Int): Stream[Int] = {
		print("low = " + low)
		if (low >= high) Stream.empty
		else Stream.cons(low, streamRange(low + 1, high))
	}                                         //> streamRange: (low: Int, high: Int)Stream[Int]
	
	streamRange(1, 10).take(3)                //> low = 1res0: scala.collection.immutable.Stream[Int] = Stream(1, ?)
	streamRange(1, 10).toSet()                //> low = 1low = 2low = 3low = 4low = 5low = 6low = 7low = 8low = 9low = 10res1:
                                                  //|  Boolean = false
}