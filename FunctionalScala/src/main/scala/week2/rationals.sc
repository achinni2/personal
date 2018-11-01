class Rational(x: Int, y: Int) {
  require(y != 0,"denominator must not be zero") // assert() can also be used instead
  def this(x : Int) = this(x,1)
  private def gcd(a: Int, b: Int): Int = if(b==0) a else gcd(b,a%b)
  private val g = gcd(x,y)
  val numer = x/g
  val denom = y/g

  def neg = new Rational(-numer,denom)

  def add(that: Rational) =
    new Rational(numer*that.denom + that.numer * denom,denom*that.denom)

  def sub(that: Rational) = add(that.neg)

  def less(that: Rational) = numer*that.denom < that.numer*denom

  def max(that: Rational) = if(this.less(that)) that else this

  override def toString = numer + "/" + denom

}
val x = new Rational(1,2)
val y = new Rational(1,4)
x.add(y)
x.sub(y)
x.less(y)
x.max(y)
