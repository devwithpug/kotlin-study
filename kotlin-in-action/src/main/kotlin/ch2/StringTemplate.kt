package ch2

fun main() {
    val name = "pug.gg"
    println("hello $name")
    // 결과: hello pug.gg

    // 예약어를 문자열에 넣으려면 '\' 이스케이프를 사용
    println("50000\$(bucks)")
    // 결과: 5000$(bucks)

    // 중괄호로 둘러싼 식 안에서 큰 따옴표를 사용할 수도 있다.
    println("Hello, ${if (name.length > 1) name else "Player"}")
}
