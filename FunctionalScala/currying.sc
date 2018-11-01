import java.util.function.DoubleUnaryOperator

// sum of numbers from a to b
def sum(f:Int => Int): (Int,Int) => Int = {
   def sumF(a: Int,b: Int) : Int =
     if(a > b) 0
     else f(a) + sumF(a+1,b)
  sumF
}

//test func
sum(x=>x)(4,6)

// product of numbers from a to b
def product(f: Int => Int)(a: Int, b: Int) : Int =
  if(a > b) 1
  else f(a)*product(x => x)(a+1,b)

//test func
product(x=>x)(1,3)
def fact(n: Int) = product(x => x)(1,n)
fact(5)

//generic function to do both sum and product
def mapReduce(f:Int => Int) (combine : (Int,Int) => Int)(zero : Int)(a: Int,b: Int) : Int = {
  if(a > b) zero
  else combine(f(a),mapReduce(f)(combine)(zero)(a+1,b))
}

// test product using generic function
mapReduce(x=>x)((x,y) => x*y)(1)(1,5)

//test sum using generic functionsbt
mapReduce(x=> x)((x,y)=>x+y)(0)(1,10)

// sqrt of a number
import math.abs
val tolerance = 0.0001

def isCloseEnough(x: Double, y: Double) =
  abs((x-y)/y)/x < tolerance

def fixedPoint(f:Double => Double)(firstGuess: Double) = {
  def iterate(guess: Double) : Double = {
    val next = f(guess)
    if(isCloseEnough(guess,next)) next
    else iterate(next)
  }
  iterate(firstGuess)
}

def averageDamp(f: Double=> Double)(x: Double) = (x + f(x))/2

def sqrt(x : Double) = fixedPoint(averageDamp(y => x/y))(1)
sqrt(2)