package ch5

fun alphabet(): String {
    val result = StringBuilder()
    for (letter in 'A'..'Z') {
        result.append(letter)
    }
    result.append("\nDone!")
    return result.toString()
}

fun alphabetUsingWith() = with(StringBuilder()) {
    for (letter in 'A'..'Z') {
        append(letter)
    }
    append("\nDone!") // this 를 생략하고 메서드를 호출할 수 있다.
    toString() // 람다에서 값을 반환한다.
}

fun alphabetWithApply() = StringBuilder().apply {
    for (letter in 'A'..'Z') {
        append(letter)
    }
    append("\nDone!")
}.toString()

fun alphabetWithBuildString() = buildString {
    for (letter in 'A'..'Z') {
        append(letter)
    }
    append("\nDone!")
}

fun main() {
    println(alphabet())
}
