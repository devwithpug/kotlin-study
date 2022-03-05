package ch6

// Any: 자바의 Object

fun main() {
    val answer: Any = 42 // 42 는 참조 타입에 박싱된다.
}

// Unit: 자바의 void

fun f(): Unit {} // Unit 은 생략 가능하다.

interface Processor<T> {
    fun process(): T
}

class NoReturnProcessor : Processor<Unit> {
    override fun process() {
        // return 을 명시할 필요가 없다.
    }
}

// Nothing: 이 함수는 결코 정상적으로 끝나지 않는다.

fun fail(message: String): Nothing = throw IllegalStateException(message)

fun foo() {
    val company = Company("company", null)
    val address = company.address ?: fail("No address")
}
