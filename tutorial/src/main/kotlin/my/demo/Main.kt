package my.demo


abstract class Foo {
    abstract fun bar()
}

fun main() {
    val foo = object : Foo() {
        override fun bar() {
            TODO("Not yet implemented")
        }
    }
    foo.bar()
}


