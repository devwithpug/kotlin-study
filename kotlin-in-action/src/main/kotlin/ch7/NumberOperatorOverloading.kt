package ch7

data class Point(
    val x: Int,
    val y: Int
)

operator fun Point.plus(other: Point): Point {
    return Point(x + other.x, y + other.y)
}

operator fun Point.times(scale: Double): Point {
    return Point((x * scale).toInt(), (y * scale).toInt())
}

fun main() {
    val p1 = Point(10, 20)
    val p2 = Point(30, 40)
    println(p1 + p2) // Point(x=40, y=60)
    var p3 = Point(0, 0)
    p3 += p1
    p3 += p2
    println(p3) // Point(x=40, y=60)
    println(p3 * 1.5) // Point(x=60, y=90)

    val list = mutableListOf(1)
    list += 1
    println(list)
}
