package ch2

import java.util.TreeMap

// while 루프는 자바와 동일하다.

fun main() {
    /**
     * for loop with range
     */
    // 자바의 for 루프 요소가 없다.
    // 이를 range 로 대신하여 표현한다.
    val oneToTen = 1..10 // range

    for (i in oneToTen) {
        println(i)
    }

    val tenDownToOneByStepTwo = 10 downTo 1 step 2
    for (i in tenDownToOneByStepTwo) {
        println(i)
    }

    val oneUntilTen = 1 until 10
    for (i in oneUntilTen) {
        println(i)
    }

    /**
     * Map Iteration
     */
    val binaryReps = TreeMap<Char, String>()

    for (c in 'A'..'F') {
        val binary = Integer.toBinaryString(c.code)
        binaryReps[c] = binary
    }

    for ((letter, binary) in binaryReps) {
        println("$letter = $binary")
    }

    /**
     * List Iteration
     */
    val list = listOf("A", "B", "C")

    for ((idx, element) in list.withIndex()) { // 인덱스와 함께 이터레이션 가능
        println("$idx: $element")
    }

    /**
     * in (contains)
     */
    val range = 1..100
    println(5 in range)
}
