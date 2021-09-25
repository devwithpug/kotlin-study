import kotlin.IllegalStateException

open class Shape()

class Rectangle {

    var height: Double
    var length: Double

    constructor(height: Double, length: Double) {
        this.height = height
        this.length = length
    }
}

data class CustomerDto(
    var name: String,
    var email: String,
    var addr: String
)

fun main(args: Array<String>) {
    println("Hello World!")

    println(sum(1, 2))
    println(sum2(1, 2))
    sum3(1, 2)


    val a: Int = 1
    val b = 2
    val c: Int
    c = 3

    var x = 5
    x++
    x+=5

    var rect = Rectangle(2.0, 4.0)


    val s1 = "a is $a"
    var a2 = 10
    val s2 = "${s1.replace("is", "was")}, but now is a $a2"

    println("$s1\n$s2")

    println(maxOf(10, 11))

    var items = listOf("A", "B", "C", "D", "E")
    for (item in items) {
        println(item)
    }
    for (index in items.indices) {
        println("item at $index is ${items[index]}")
    }

    var i = 0
    while (i < items.size) {
        println("item at $i is ${items[i]}")
        i++
    }

    val newItems = listOf(1, "1", 1.0, "Hello", 12345, true, false)
    for (newItem in newItems) {
        println(describe(newItem))
    }

    for (i in 1..5) {
        print("$i ")
    }

    if (2*2*2 in 1..10) println(true)

    if (-1 !in 0..newItems.lastIndex) println("out of range")
    if (newItems.size !in newItems.indices) println("out of range")

    println(0..items.lastIndex == items.indices)

    for (step2 in 1..10 step 2) println(step2)
    for (downTo in 10 downTo 0 step 3) println(downTo)

    when {
        true in newItems -> println("true")
    }

    items = listOf("abc", "aa", "acd", "baCaf", "edf", "daa", "Aa", )

    items
        .filter { it.startsWith("a") }
        .sortedBy { it }
        .map { it.uppercase() }
        .forEach { println(it) }


    var nullableValue :Int? = getLengthIfString("ABCDE")
    println("nullableValue = $nullableValue")
    nullableValue = getLengthIfString2(null)
    println("nullableValue = $nullableValue")

    val customerDto = CustomerDto("A", "A@A", "A-a")
    println("customerDto = $customerDto")

    val customerDto2 = CustomerDto("A", "A@A", "A-a")
    val equalCustomerDto = customerDto
    val copyCustomerDto = customerDto.copy()


    println(customerDto == customerDto2)
    println(customerDto !== customerDto2)
    println(customerDto === equalCustomerDto)
    println(customerDto !== copyCustomerDto)

    println(foo())
    val list = mutableListOf(1, 2, 3,4,5,6,7,8,9,10)
    val res = list.filter { it > 3 }
    println(res)

    println(3 in list)
    println(100 !in list)

    println("list : $list")

    list.add(1)
    list[1] = 1

    println("list : $list")

    val mapOf = mapOf("a" to "A", "b" to "B", "c" to "C")
    println(mapOf)

    val mutableMapOf = mutableMapOf("a" to 1)
    mutableMapOf["b"] = 2

    for ((k, v) in mapOf) {
        println("$k : $v")
    }

    for (i in 1 until 10) {
        print(i)
    }
    println()

    var lazyValue = "preValue"
    val p: String by lazy { lazyValue }
    lazyValue = "postValue"
    println("lazy init result : $p")

    println("String.toUpper() : ${"this is a uppercase".toUpper()} ")


    SingletonResource.name += "!"
    println(SingletonResource.name)

    val myObj = object : MyAbstractClass() {
        override fun foo(): Int? {
            println("foo")
            return null
        }

        override fun bar(): String {
            return "bar"
        }
    }
    println("RESULT : ${myObj.foo()}, ${myObj.bar()}")

    var ifNotNull: String? = "not null"

    println(ifNotNull?.length)
    ifNotNull = null
    println(ifNotNull?.length)
    println(ifNotNull?.length ?: "empty")
    try {
        println(ifNotNull?.length ?: throw IllegalStateException("error"))
    } catch (err: IllegalStateException) {
        println(err)
    }

    val value = list.firstOrNull() ?: ""
    println(value)

    val executeIfNotNull: String? = null
    executeIfNotNull?.let {
        println("EXECUTED : $it")
    }

    val mapped = executeIfNotNull?.let { it.uppercase() } ?: "NONE"
    println(mapped)

    println("RED is ${transform("Red")}")

    val elvisResult = elvisWithFoo(1)
    println(elvisResult)

    val intArr = arrayOfMinusOnes(5)
    println(intArr.contentToString())

    val myTurtle = MyTurtle()

    with(myTurtle) {
        penUp()
        penDown()
        turn(10.0)
        forward(20.0)
    }

    val apply = TestClass().apply {
        A = "lateInit"
    }

    println(apply.A)

    var from = 1
    var to = 2
    from = to.also { to = from }

    println("now from $from to $to")

    fun todoFunc(): Any = TODO("만들어야해잉")

    try {
        todoFunc()
    } catch (err: NotImplementedError) {
        println(err)
    }

}

class TestClass {
    lateinit var A: String
}

class MyTurtle {
    fun penDown() = println("UP")
    fun penUp() = println("Down")
    fun turn(d: Double) = println("turn $d")
    fun forward(p: Double) = println("forward $p")
}

fun arrayOfMinusOnes(size: Int): IntArray {
    return IntArray(size).apply { fill(-1) }
}

fun elvisWithFoo(param: Int): String = foo2(param) ?: "DEFAULT"

fun foo2(param: Int): String? = when {
    param > 0 -> "one"
    param > 1 -> "two"
    else -> null
}

fun transform(color: String): Int =
    when (color) {
        "Red" -> 1
        "Blue" -> 2
        "Green" -> 3
        else -> 0
    }

abstract class MyAbstractClass {
    abstract fun foo() : Int?
    abstract fun bar() : String
}

object SingletonResource {
    var name = "SINGLETON"
}

fun String.toUpper(): String {
    return this.uppercase()
}

fun foo(a: Int = 100) = a

fun getLengthIfString(obj: Any?): Int? {
    if (obj is String) return obj.length
    return null
}

fun getLengthIfString2(obj: Any?): Int? = if (obj is String) obj.length else null


fun describe(obj: Any): String =
    when (obj) {
        1 -> "One"
        "Hello" -> "Hi"
        true -> "so TRUE!"
        false -> "oh.. umkay.."
        is Long -> "Long"
        !is String -> "Not a String"
        else -> "Unknown String"
    }

fun sum(a: Int, b: Int): Int {
    return a + b
}

fun sum2(a: Int, b: Int) = a + b

fun sum3(a: Int, b: Int) {
    println("$a + $b = ${a+b}")
}

fun maxOf(a: Int, b: Int): Int {
    return if (a>b) a else b
}