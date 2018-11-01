abstract class TrueBoolean {
 def ifThenElse[T](t: =>T,e: => T): T

 def && (x: => TrueBoolean)= ifThenElse(x,false)

 def || (x: => TrueBoolean) = ifThenElse(true,x)

 def unary_! = ifThenElse(false,true)

 def == (x: => TrueBoolean) = ifThenElse(x,x.unary_!)

 def != (x: => TrueBoolean) = ifThenElse(x.unary_!,x)

}


abstract class Nat {
 def iszero: Boolean
 def predecessor: Nat
 def successor = new Succ(this)
 def +(that: Nat): Nat
 def -(that: Nat): Nat
}

object Zero extends Nat{
 override def iszero: Boolean = true

 override def predecessor = throw new Error("0.predecessor")

 override def +(that: Nat) = that

 override def -(that: Nat) = if(that.iszero) this else throw new Error("negative number")
}

class Succ(n: Nat) extends Nat{
 override def iszero = false

 override def predecessor = n


 override def +(that: Nat) = new Succ(n + that)

 override def -(that: Nat) = if(that.iszero) this else n - that.predecessor
}
