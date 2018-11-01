abstract class IntSet
{
  def incl(x: Int) : IntSet
  def contains(x: Int) : Boolean
  def union(other: IntSet) : IntSet
}

class NonEmpty(i: Int, left: IntSet, right: IntSet) extends IntSet
{
  override def incl(x: Int) = {
    if( x < i) new NonEmpty(i,left incl x ,right)
    else if (x > i) new NonEmpty(i,left,right incl x)
    else this
  }

  override def contains(x: Int) = {
    if (x < i) left contains x
    else if (x > i) right contains x
    else true
  }

  override def union(other: IntSet) =
    ((left union right) union other) incl i

  override def toString = "{" + left + i + right + "}"
}

object Empty extends IntSet
{
  override def incl(x: Int) = new NonEmpty(x,Empty,Empty)

  override def contains(x: Int) = false

  override def toString = "."

  override def union(other: IntSet) = other
}
val t1 = new NonEmpty(3,Empty,Empty)
val t2 = t1 incl 4
val t3 = t1 incl 5
val t4 = t2 union t3
