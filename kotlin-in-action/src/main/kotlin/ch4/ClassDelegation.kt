package ch4

class DecoratingCollection<T> : Collection<T> {
    private val innerList = arrayListOf<T>()
    override val size: Int
        get() = TODO("Not yet implemented")

    override fun contains(element: T): Boolean {
        TODO("Not yet implemented")
    }

    override fun containsAll(elements: Collection<T>): Boolean {
        TODO("Not yet implemented")
    }

    override fun isEmpty(): Boolean {
        TODO("Not yet implemented")
    }

    override fun iterator(): Iterator<T> {
        TODO("Not yet implemented")
    }
}

class DelegatingCollection<T>(
    innerList: Collection<T> = arrayListOf()
) : Collection<T> by innerList {
    // 위에서 필수로 구현해야 했던 메서드들을 자동으로 생성해준다.
    // 구현도 알아서 해준다.
}

fun main() {
    val strings = DelegatingCollection(listOf("A", "B", "C"))
    println(strings.contains("B")) // 구현이 되어 있다.
    // 정확히는 arrayListOf() 즉, ArrayList<T> 에게 위임한 것이다.
}
