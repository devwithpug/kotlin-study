package ch6

fun <T> printHashCode(t: T) {
    println(t?.hashCode()) // t 가 nullable 하므로 safe call 을 사용해야 한다.
}

fun <T : Any> printHashCodeOnlyNotNull(t: T) { // 이제 T 는 not null 타입이다.
    println(t.hashCode())
}

fun main() {
    printHashCode(null)
    // printHashCodeOnlyNotNull(null) // 컴파일되지 않는다.
}
