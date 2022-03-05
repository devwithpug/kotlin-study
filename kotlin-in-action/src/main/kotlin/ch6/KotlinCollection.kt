package ch6

fun <T> copyElements(
    source: Collection<T>,
    target: MutableCollection<T>
) {
    for (item in source) {
        target.add(item)
    }
}

fun main() {
    // val source = listOf(3, 5, 7)
    // val target = mutableListOf(1)

    val source: Collection<Int> = arrayListOf(3, 5, 7)
    val target: MutableCollection<Int> = arrayListOf(1)
    copyElements(source, target)
    // 책에서는 컴파일 에러가 난다고 하지만 현재 최신 kotlin 1.5 버전에서는 정상 동작한다.
    // 참조: https://github.com/Kotlin/kotlinx.collections.immutable/blob/master/proposal.md
    println(target)
}
