# Kotlin 듀토리얼 :D

## Basic syntax

1. 엔트리 포인트

```kotlin
fun main() {
    println("?:o")
}

fun main(args: Array<String>) {
    println(args.contentToString())
}
```

2. standard output `print`

```kotlin
print("HELLO\n")

println("HELLO")

println("${'HELLO'}")

val message = "HELLO"

println(message)                 // HELLO

val message2 = "?:o"

println(message + message2)    // HELLO?:o

// String templates

var intA: Int = 100
val stringA = "intA is $a"

intA = 200

val stringB = "${stringA.replace("is", "was")}, now is $intA"
// intA was 100, now is 200
```

3. functions

```kotlin
fun add(a: Int, b: Int): Int {
    return a + b
}

fun add2(a: Int, b: Int) = a + b
```

4. Variables

```kotlin
// 1. value

val a: Int = 10
val b = 20
val c: Int
c = 30

// 2. variable

var d: Int = 40
d += 10

// 4. nullable

data class Person(
    val name: String,
    var info: String? = null
)

val person = Person("userA")

person.info?.let { println(it) } // null check

val info = person.info ?: "none" // elvis operator

println(info) // none

// 3. lateinit var

lateinit var someValue: String

fun main() {
    initValue()
    println("value is now $someValue") // value is now initialized
}

fun initValue() {
    someValue = "initialized"
}
```

5. Ranges

```kotlin
for (x in 1..5) println(x)          // 1 ~ 5

val myRange = 100 downTo 0 step 5   // 100, 95, 90 ... 0
val myRange2 = 0 until 10           // 0 ~ 9

for (x in myRange) print(x)
for (x in myRange2) print(x)

```

6. Collections

```kotlin
val myList = buildList { for (x in myRange) add(x) } // [100, 95, 90, ..., 0]

for (i in myList) {
    getScore(i)
}

fun getScore(x: Int) = when {
    x > 90 -> "A"
    x > 80 -> "B"
    x > 70 -> "C"
    else -> "F"
}
```

## Idioms

1. DTOs

```kotlin
data class Member(
    val name: String,
    var email: String?
)
// 메소드 제공
// getter & setter(var-only)
// equals, hashCode
// toString
// copy

val member = Member("memberA", null)            // memberA, null

val copied = member.copy(email = "newEmail")    // memberA, newEmail
```

2. Defaults values for function parameters

```kotlin
fun foo(x: Int = 0, y: Int = 0, name: String) = "$name : ${x + y}"

foo(name = "f1")            // f1 : 0
foo(y = 10, name = "f2")    // f2 : 10
foo(x = 1, name = "f3")     // f3 : 1
foo(1, 2, "f4")             // f4 : 3
```

3. Filter a list

```kotlin
val list = listOf(-3, -2, -1, 0, 1, 2, 3)
val pos = list.filter { it > 0 }
val neg = list.filter { it < 0 }
```

4. Mutable, Immutable Collections

```kotlin
val mutableListOf = mutableListOf(1, 2, 3)
val immutableListOf = listOf(1, 2, 3)

mutableListOf.add(4)
//immutableListOf.add(4) : X
```

5. Access a map entry

```kotlin
val map = mutableMapOf("a" to "A", "b" to "B", "c" to "C")
println(map["a"])
map["d"] = "D"

for ((lower, upper) in map) {
    println("$lower -> $upper")
}
```

6. Lazy property

```kotlin
lateinit var inputValue: String

fun main() {
    val p: String by lazy {
        "Will" + "be" + "initialized" + "lazily : " + inputValue.length
    }
    inputValue = "something"

    println("$p\nthen initialized!")
}
```

7. Extension functions

```kotlin
fun String.upperOnlyLetter(): String {
    for (c in this) if (!c.isLetter()) throw IllegalArgumentException()
    return this.uppercase()
}

fun main() {
    "string".upperOnlyLetter()  // STRING
    "123a".upperOnlyLetter()    // IllegalArgumentException 
}
```

8. Singleton

```kotlin
object Resource {
    const val name = "name"
}
```

9. Instantiate an abstract class

```kotlin
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
```