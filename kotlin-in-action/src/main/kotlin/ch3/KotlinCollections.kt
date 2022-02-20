package ch3


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
}

// 최상위 변수
var topVariable: Int = 0
const val SOME_CONST_VALUE = 100

// 최상위 함수
fun topFunction() {
    topVariable += SOME_CONST_VALUE
}
