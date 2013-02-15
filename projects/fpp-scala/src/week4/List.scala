package week4

trait List[Type] {
  def isEmpty: Boolean
  def head: Type
  def tail: List[Type]
}

class Cons[Type](val head: Type, val tail: List[Type]) extends List[Type] {
  def isEmpty = false
}

class Nil[Type] extends List[Type]{
  def isEmpty = true
  def head: Nothing = throw new NoSuchElementException("Nil.head")
  def tail: Nothing = throw new NoSuchElementException("Nil.tail")
}