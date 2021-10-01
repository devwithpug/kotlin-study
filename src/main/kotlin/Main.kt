import java.util.*
import kotlin.IllegalStateException
import kotlin.NoSuchElementException
import kotlin.random.Random

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

@OptIn(ExperimentalStdlibApi::class)
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

    val stringConcat = "before"
    val newStringConcat = "$stringConcat after"
    println(newStringConcat)

    val buildStr = buildString {
        for (i in 1..25 step 2) {
            append(i)
        }
    }
    println(buildStr)

    val buildLis = buildList {
        for (i in 1..10) {
            add(i)
        }
    }
    println(buildLis)

    val numbers = listOf(1, 2, 3, 4, 5, 6)
    val invertedOddNumbers = numbers
        .filter { it % 2 != 0 }
        .map { -it }
    println(invertedOddNumbers)

    for (i in 1..100) {
        print("${Random.nextBoolean()} ")
    }
    println()

    val name = (if (Random.nextBoolean()) "" else "TRUE").ifBlank { "OH" }
    println(name)

    val input = "##plcae##holder##"
    println(input.removeSurrounding("##"))

    val regex = Regex("""\w*\d+\w*""")
    val input2 = "login: Pokemon5, password: 1q2w3e4r"
    val replaceResult = regex.replace(input2, replacement = "xxx")
    println(replaceResult)

    println("La.La.Means.I.Love.You".split("."))


    val input3 = "What is Love? Baby Don't hurt me? No more."
    println(input3.substringAfter("?"))
    println(input3.substringAfterLast("?"))

    println(
        """
            자동으로 줄바꿈을
            해준다고 합니다
                인덴트도 되는건가요?
                    와우!!!
            오집니다잉
        """.trimIndent()
    )

    println(
        """
            #in-line
            # with-space
            #   with-indent
        """.trimMargin("#")
    )

    val trimIndent = """
        TEST
            TEST
    """.trimIndent()
    println()

    val myLis = listOf("A", "B", "C")

    println(myLis.joinToString { "[$it]" })
    println(myLis.joinToString(prefix = "[", postfix = "]"))

//    throwExample(2)

    val d1 = MyDate(2021, 10, 26)
    val d2 = MyDate(2021, 11, 5)

    for (d in d1..d2) {
        println("${d.year} | ${d.month} | ${d.dayOfMonth}")
    }

    println(listOf("1", "1", "2", "3", "5").toSet())

    val listOf =
        listOf(listOf("1", "1", "2", "3", "5"), listOf("A", "A", "B", "C", "E"), listOf("a", "a", "b", "c", "e"))

    val flatMap = listOf.flatMap { it }
    println(flatMap)

    val members = listOf(
        Member("memberA", Address("오산", "코리아", 11), 1, listOf("ABC", "CBA")),
        Member("memberB", Address("광주", "코리아", 22), 2, listOf("ABC", "CBA")),
        Member("memberC", Address("수원", "한국", 33), 3, listOf("ABC", "CBA")),
        Member("memberD", Address("서울", "코리아", 44), 4, listOf("ABC", "CBA")),
        Member("memberE", Address("제주", "한국", 55), 5, listOf("ABC", "CBA")),
        Member("memberF", Address("세종", "대한민국", 66), 6, listOf("ABC", "CBA")),
    )

    println(members.associateBy { it.name })
    println(members.associateWith { it.addr })
    println(members.associate { it.name to it.addr })

    println(members.groupBy { it.addr.country })

    val (big, small) = members.partition { it.addr.code > 30 }
    println("""
        big     : $big
        small   : $small
    """.trimIndent())

    val joinToString = members
        .flatMap { it.types }
        .joinToString()
    println(joinToString)

    println(members
        .fold(1) { t, e -> t + e.num })

    var nullableInteger: Int?

    nullableInteger = null
    nullableInteger = 10

    nullableInteger?.let { println("$it") }
    nullableInteger?.run { println("$this") }
}

data class Member(
    val name: String,
    val addr: Address,
    val num: Int,
    val types: List<String>
)

data class Address(
    val city: String,
    val country: String,
    val code: Int
)

enum class TimeInterval { DAY, WEEK, YEAR }

data class MyDate(val year: Int, val month: Int, val dayOfMonth: Int) : Comparable<MyDate> {

    override fun compareTo(other: MyDate): Int {
        if (year != other.year) return year - other.year
        if (month != other.month) return month - other.month
        return dayOfMonth - other.dayOfMonth
    }
}

operator fun MyDate.plus(timeInterval: TimeInterval): MyDate =
    addTimeIntervals(timeInterval, 1)

operator fun MyDate.rangeTo(other: MyDate) = DateRange(this, other)

fun MyDate.addTimeIntervals(timeInterval: TimeInterval, amount: Int): MyDate {
    val c = Calendar.getInstance()
    c.set(year + if (timeInterval == TimeInterval.YEAR) amount else 0, month, dayOfMonth)
    var timeInMillis = c.timeInMillis
    val millisecondsInADay = 24 * 64 * 64 * 1000L
    timeInMillis += amount * when (timeInterval) {
        TimeInterval.DAY -> millisecondsInADay
        TimeInterval.WEEK -> 7 * millisecondsInADay
        TimeInterval.YEAR -> 0L
    }
    val result = Calendar.getInstance()
    result.timeInMillis = timeInMillis
    return MyDate(result.get(Calendar.YEAR), result.get(Calendar.MONTH), result.get(Calendar.DATE))
}

fun MyDate.followingDate(): MyDate {
    val c = Calendar.getInstance()
    c.set(year, month, dayOfMonth)
    val m = 24 * 60 * 60 * 1000L
    val timeInM = c.timeInMillis + m
    val result = Calendar.getInstance()
    result.timeInMillis = timeInM
    return MyDate(result.get(Calendar.YEAR), result.get(Calendar.MONTH), result.get(Calendar.DATE))
}

class DateRange(val start: MyDate, val end: MyDate) : Iterable<MyDate> {
    override fun iterator(): Iterator<MyDate> {
        return object : Iterator<MyDate> {
            var current = start

            override fun hasNext(): Boolean = current <= end

            override fun next(): MyDate {
                if (!hasNext()) throw NoSuchElementException()
                val result = current
                current = current.followingDate()
                return result
            }
        }
    }
}

fun containsEven(collection: Collection<Int>): Boolean =
    collection.any { it % 2 == 0 }

fun throwExample(num: Int?): Nothing {
    throw IllegalArgumentException("exception Example $num")
}

fun checkAge(age: Int?) {
    if (age == null || age !in 0..150) throwExample(age)
    println("Congrats! Next year you'll be ${age + 1}.")
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