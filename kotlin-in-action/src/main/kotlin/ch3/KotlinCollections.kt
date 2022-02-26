package ch3

import java.time.LocalDate

// 코틀린은 자신만의 컬렉션 기능을 제공하지 않는다. (자바 컬렉션을 사용)
fun main() {
    val list = listOf("first", "second", "third")
    list.last()

    val numbers = setOf(1, 14, 2)
    numbers.maxOrNull() // max() 는 deprecated 되어서 없어졌다. - kotlin 1.4

    // joining
    val joined = list.joinToString("/", postfix = "]", prefix = "[")
    println(joined)

    val joinToString = list.joinToString() // default parameter values
    println(joinToString)

    for ((index, element) in list.withIndex()) {
        println("$index : $element")
    }
}

// 최상위 변수
var topVariable: Int = 0
const val SOME_CONST_VALUE = 100

// 최상위 함수
fun topFunction() {
    topVariable += SOME_CONST_VALUE
}

fun sumWithVararg(vararg elements: Int): Int = elements.sum()

fun spreadOperator(args: Array<Int>): List<Int> {
    return listOf(*args)
}

val map = mapOf(1 to "one", 2 to "two", 3 to "three")
val map2 = mapOf(Pair(1, "one"), Pair(2, "two"), Pair(3, "three"))

val pair = 10 to "ten"

infix fun LocalDate.to(toDate: LocalDate) = this.rangeTo(toDate)

val startDate: LocalDate = LocalDate.of(2022, 2, 20)

val range: ClosedRange<LocalDate> = startDate to LocalDate.now()
