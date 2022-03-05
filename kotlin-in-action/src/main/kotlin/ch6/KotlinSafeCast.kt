package ch6

class Foo(
    val name: String
) {
    override fun equals(other: Any?): Boolean {
        val otherFoo = other as? Foo ?: return false
        return otherFoo.name == this.name
    }
}
