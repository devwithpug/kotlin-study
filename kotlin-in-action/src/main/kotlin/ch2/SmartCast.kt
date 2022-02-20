package ch2


interface Expr
class Num(val value: Int): Expr
class Sum(val left: Expr, val right: Expr): Expr

// (1 + 2) + 4 연산은 아래와 같이 표현된다.
// Sum(Sum(Num(1), Num(2)), Num(4))

fun eval(e: Expr): Int =
    when (e) {
        is Num -> e.value
        is Sum -> eval(e.right) + eval(e.left)
        else -> throw IllegalArgumentException("Unknown expression")
    }

fun main() {
    val expr = Sum(Sum(Num(1), Num(2)), Num(4))
    println("result = ${eval(expr)}")
}
