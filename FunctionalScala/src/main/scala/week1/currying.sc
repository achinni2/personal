// sum of numbers from a to b.
def sum(f:Int => Int) : (Int,Int) => Int = {
  def sumF(a:Int ,b:Int): Int =
    if(a > b) 0
    else f(a) + sumF(a+1,b)
  sumF
}

//test
sum (x=>x) (1,10)


// product of numbers from a to b.
def product(f: Int => Int) (a: Int, b: Int): Int =
  if(a > b) 1
  else f(a)*product(f)(a+1,b)

def fact(n: Int) = product(x => x)(1,n)

//test
product(x=>x*x)(1,3)
fact(5)

// generic method from product and sum
def mapReduce(f: Int => Int , combine: (Int,Int) => Int , zero : Int)(a: Int, b: Int): Int =
  if ( a > b) zero
  else combine(f(a),mapReduce(f,combine,zero)(a+1,b))


// product from a to b using generic function
def productGeneric(f: Int => Int) (a: Int, b: Int): Int = mapReduce(f,(x,y)=> x*y,1)(a,b)

// test
productGeneric(x => x)(1,5)






