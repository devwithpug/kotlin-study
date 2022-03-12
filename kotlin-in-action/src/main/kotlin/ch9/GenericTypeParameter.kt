package ch9

val authors = listOf("Dmitry", "Svetlana") // 코틀린 컴파일러는 타입 인자도 추론할 수 있다.

fun <T> List<T>.slice(indices: IntRange): List<T> {
    TODO("함수의 타입 파라미터 T가 수신 객체와 반환 타입에 쓰인다.")
}

fun <T, R> List<T>.sliceTest(indices: IntRange): List<R> {
    TODO("두 가지 타입 인자를 받는 제네릭 메서드")
}

interface MyList<T> {
    operator fun get(index: Int): T
}

class StringList: MyList<String> {
    override fun get(index: Int): String {
        TODO("Not yet implemented")
    }
}

class MyArrayList<T>: MyList<T> {
    override fun get(index: Int): T {
        TODO("Not yet implemented")
    }
}

fun main() {
    // val rawList: List // 컴파일 에러: 코틀린에서는 raw 타입 사용을 허용하지 않는다.

    val letters = ('a'..'z').toList()
    letters.slice<Char>(0..2) // 메서드를 호출할 때 타입 인자를 명시적으로 선언할 수 있다.
    letters.slice(0..2) // 타입 인자 생략 : 코틀린 컴파일러가 알아서 추론해준다.

    letters.sliceTest<Char, Int>(0..2) // 코틀린 컴파일러가 타입 인자를 추론할 수 없는 경우 반드시 명시해야 한다.

}
