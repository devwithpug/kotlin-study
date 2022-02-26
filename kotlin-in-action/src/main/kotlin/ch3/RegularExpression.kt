package ch3


fun main() {
    println("12.345-6.A".split(".")) // 자바에서는 문자열을 Regex 로 인식하지만 코틀린은 여러가지 타입의 인자를 지원하고 있다.
}

// path = /Users/pug/kotlin/test.kt
fun parsePath(path: String) {
    val directory = path.substringBeforeLast("/")
    val fullName = path.substringAfterLast("/")
    val fileName = fullName.substringBeforeLast(".")
    val extension = fullName.substringAfterLast(".")
}

fun parsePathWithRegex(path: String) {
    val regex = """(.+)/(.+)\.(.+)""".toRegex()
    val matchResult = regex.matchEntire(path)
    if (matchResult != null) {
        val (directory, filename, extension) = matchResult.destructured
    }
}
