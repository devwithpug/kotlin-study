package ch3

fun String.lastChar(): Char = this[length - 1]

fun main() {
    println("Kotlin".lastChar())
    println(Foo().bar())
}

class Foo {
    fun bar() = "hello"
}

fun Foo.bar() = "extension function"
