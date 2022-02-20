package ch2

// kotlin 의 if 는 자바와 다르게 식(expression)이지 문(statement)이 아니다.
// 또한, 타입 추론 기능때문에 리턴 타입을 생략해도 된다.
fun max(a: Int, b: Int) = if (a > b) a else b

// 하지만 블록이 본문인 함수는 리턴 값을 생략할 수 없다.
// 이렇게 설계한 이유는 아주 긴 함수에 return 문이 여럿 들어있는 경우
// 반환 타입을 꼭 명시함으로써 함수가 어떤 타입의 값을 반환하고 어디서 그런 값을 반환하는지 더 쉽게 알아볼 수 있다.
fun maxWithBlockStatement(a: Int, b: Int): Int {
    return if (a > b) a else b
}

fun main() {
    println("max = ${max(3, 5)}")
}
