package ch9

import ch8.joinToString

fun printContents(list: List<Any>) {
    println(list.joinToString())
}

fun addAnswer(list: MutableList<Any>) {
    list.add(12)
}

fun test(i: Int) {
    val n: Number = i // Int 가 Number 의 하위 타입이므로 컴파일된다.
    fun f(s: String) {}
    // f(i) // Int 가 String 의 하위 타입이 아니므로 컴파일되지 않는다.
}

interface Producer<out T> { // 클래스가 T에 대해 공변적이라고 선언한다.
    fun produce(): T
}

fun main() {
    printContents(listOf("foo", "bar"))

    val strings = mutableListOf("foo", "bar")
    // addAnswer(strings) // 컴파일 에러 Type mismatch

    var x: String // String 클래스의 인스턴스를 저장하는 변수
    var y: String? // 같은 String 클래스 이름을 nullable 타입에도 쓸 수 있다. 즉, 모든 코틀린 클래스는 적어도 둘 이상의 타입을 구성할 수 있다.
}

open class Animal {
    fun feed() {}
}

class Herd<out T : Animal> {
    val size: Int get() = 0
    operator fun get(i: Int): T { TODO() }
    // fun add(animal: T) {
    //
    // }
}

fun feedAll(animals: Herd<Animal>) {
    for (i in 0 until animals.size) {
        animals[i].feed()
    }
}

class Cat : Animal() {
    fun cleanLitter() {}
}

fun takeCareOfCats(cats: Herd<Cat>) {
    for (i in 0 until cats.size) {
        cats[i].cleanLitter()
    }
    feedAll(cats) // 공변적이므로 캐스팅하지 않아도 된다.
}

interface Consumer<in T> {
    fun typeParam(element: T) {}
    // fun returnTypeParam(): T // 컴파일 에러: out 위치에서 타입 파라미터를 사용할 수 없다.
}

fun <T> copyData(
    source: MutableList<out T>,
    destination: MutableList<T>
) {
    for (item in source) {
        destination.add(item)
    }
}
