package ch4

data class DataClass(
    val foo: String,
    val bar: String
)

fun main() {
    val dataClass = DataClass("foo", "bar")
    dataClass.foo
    dataClass.bar
    dataClass.toString()
    dataClass.hashCode()
}
