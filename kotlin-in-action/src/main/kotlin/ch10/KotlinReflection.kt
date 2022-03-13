package ch10

import kotlin.reflect.KFunction
import kotlin.reflect.full.*

class Person(
    val name: String,
    val age: Int
) {
    fun bar(x: Int) = println(x)
}

fun foo(x: Int) = println(x)

var counter = 0

fun test() {
    var rr = 1
    // ::rr // Unsupported [References to variables aren't supported yet]
}

fun main() {
    val person = Person("Alice", 29)
    val kClass = person.javaClass.kotlin

    kClass.members.forEach { println(it.name) } // age, name, equals, hashCode, toString

    // kotlin-reflect 에 구현되어 있는 확장 메서드
    kClass.memberProperties.forEach { println(it.name) } // age, name

    val kFunction = ::foo
    kFunction.call(50)
    kFunction.invoke(50)

    val kMutableProperty = ::counter
    kMutableProperty.setter.call(21)
    println(kMutableProperty.get())
}
