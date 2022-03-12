package ch9

fun main() {
    val value = listOf("A", "B", "C")
    if (value is List<String>) {
        println(value)
    }

    printSum(listOf(1, 2, 3)) // 정상적으로 동작한다.
    // printSum(setOf(1, 2, 3)) // 의도한대로 예외가 발생한다.
    // printSum(listOf("1", "2", "3")) // 의도하지 않은 ClassCastException 이 발생한다.

    println(testReify<String>("String"))
    println(testReify<Int>(1))
    println(testReify<String>(2))
}

fun test(value: Any) {
    // if (value is List<String>) { } // 컴파일 에러
}

fun testWithStarProjection(value: Any) {
    if (value is List<*>) {
    }
}

fun printSum(c: Collection<*>) {
    val intList = c as? List<Int>
        ?: throw IllegalArgumentException()
    println(intList.sum())
}

inline fun <reified T> testReify(value: Any) = value is T
