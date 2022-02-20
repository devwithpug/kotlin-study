package ch2

import java.lang.Exception

enum class Color(
    private val r: Int,
    private val g: Int,
    private val b: Int
) {
    RED(255, 0, 0),
    BLUE(0, 0, 255),
    GREEN(0, 255, 0),
    YELLOW(255, 255, 0),
    ORANGE(255, 165, 0); // 반드시 세미콜론을 사용해야 함.

    fun rgb() = (r * 256 + g) * 256 + b
}

fun getMnemonic(color: Color) =
    when (color) {
        Color.RED -> "Richard"
        Color.BLUE -> "Battle"
        Color.GREEN -> "Gave"
        Color.YELLOW -> "York"
        Color.ORANGE -> "Of"
    }

fun getWarmth(color: Color) =
    when (color) {
        Color.RED, Color.ORANGE, Color.YELLOW -> "warm"
        Color.GREEN -> "neutral"
        Color.BLUE -> "cold"
    }

fun mix(c1: Color, c2: Color) =
    when (setOf(c1, c2)){
        setOf(Color.RED, Color.YELLOW) -> Color.YELLOW
        setOf(Color.YELLOW, Color.BLUE) -> Color.GREEN
        else -> throw Exception("Dirty Color")
    }

// when 분기 조건에서 여러 Set 인스턴스를 생성하면 가비지 객체가 증가하므로
// 메모리 효율상 좋지 않다. 따라서 아래와 같이 성능 개선이 가능하다.
fun mixOptimized(c1: Color, c2: Color) =
    when {
        (c1 == Color.RED && c2 == Color.YELLOW) ||
        (c1 == Color.YELLOW && c2 == Color.RED) ->
            Color.ORANGE

        // 생략

        else -> throw Exception("Dirty Color")
    }

fun main() {
    println(Color.BLUE.rgb())
    println(getMnemonic(Color.ORANGE))
    println(mix(Color.BLUE, Color.YELLOW))
}
