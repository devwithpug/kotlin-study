package ch7

fun main() {
    val p = MyPoint(10, 20)
    val (x, y) = p
    println("$x, $y")
}

class MyPoint(
    val x: Int,
    val y: Int
) {
    operator fun component1() = "x[$x]"
    operator fun component2() = "y[$y]"
}
