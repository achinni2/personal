trait List[T] {
  def isEmpty : Boolean
  def head : T
  def tail : List[T]
}

class Cons[T](val head: T,val tail: List[T]) extends List[T]{
  def isEmpty = false
}

class Nil[T] extends List[T]{
  def isEmpty: Boolean = true
  def head: Nothing = throw new NoSuchElementException("Nil.head")
  def tail: Nothing = throw new NoSuchElementException("Nil.tail")
}

object List {
  // List(1,2) = List.apply(1,2)
  def apply[T](x1: T, x2: T) : List[T] = new Cons(x1, new Cons(x2, new Nil))
  def apply[T]() = new Nil
}

def singleton[T](elem: T) = new Cons[T](elem, new Nil[T])

singleton[Int](1)
singleton[Boolean](true)
singleton(2)

def nth[T](index: Int,list: List[T]): T = {
  if (list.isEmpty) throw new IndexOutOfBoundsException("Empty")
  if(index == 0) list.head
  else nth(index-1,list.tail)
}

val list = new Cons(1,new Cons(2,new Cons(3, new Nil)))
nth(2,list)
nth(4,list)
