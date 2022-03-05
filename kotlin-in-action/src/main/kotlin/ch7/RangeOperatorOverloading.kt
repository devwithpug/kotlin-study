package ch7

import java.time.LocalDate

fun main() {
    val now = LocalDate.now()
    val closedRange = now..now.plusDays(10)
    // now.rangeTo(now.plusDays(10)) 으로 변환된다.

    val n = 9
    0..(n + 1) // 0..n+1 이라고 써도 되지만 괄호를 치면 뜻이 명확해진다

    (0..n).forEach { print(it) } // 0..n.forEach {} 와 같이는 컴파일 할 수 없다. 우선순위 때문!

    for (date in closedRange) {
        println(date)
    }
}

operator fun ClosedRange<LocalDate>.iterator(): Iterator<LocalDate> =
    object : Iterator<LocalDate> {
        var current = start
        override fun hasNext() = current <= endInclusive
        override fun next() = current.apply { current = plusDays(1) }
    }
