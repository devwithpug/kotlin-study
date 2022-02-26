# 3장 함수 정의와 호출

## 컬렉션

> 코틀린은 자신만의 컬렉션 기능을 제공하지 않는다. 자바 개발자가 기존 자바 컬렉션을 활용할 수 있다는 뜻이므로 이는 자바 개발자들에게 좋은 소식이다.
> 표준 자바 컬렉션을 활용하면 자바 코드와 상호작용하기가 훨씬 더 쉽다. 자바에서 코틀린 함수를 호출하거나 코틀린에서 자바 함수를 호출할 때 자바와 코틀린 컬렉션을 서로 변환할 필요가 없다. 코틀린 컬렉션은 자바 컬렉션과 똑같은 클래스이지만 코틀린에서는 자바보다 더 많은 기능을 쓸 수 있다.

```kotlin
// 코틀린은 자신만의 컬렉션 기능을 제공하지 않는다. (자바 컬렉션을 사용)
fun main() {
    val list = listOf("first", "second", "third")
    list.last()

    val numbers = setOf(1, 14, 2)
    numbers.maxOrNull() // max() 는 deprecated 되어서 없어졌다. - kotlin 1.4

    // joining
    val joined = list.joinToString("/", postfix = "]", prefix = "[")
    println(joined)

    val joinToString = list.joinToString() // default parameter values
    println(joinToString)
}

// 최상위 변수
var topVariable: Int = 0
const val SOME_CONST_VALUE = 100

// 최상위 함수
fun topFunction() {
    topVariable += SOME_CONST_VALUE
}
```

## 확장 함수와 확장 프로퍼티

### 확장 함수

**확장 함수는 어떤 클래스의 멤버 메소드인 것처럼 호출할 수 있지만 그 클래스의 밖에 선언된 함수다.**

```kotlin
package ch3
fun String.lastChar(): Char = this.get(this.length - 1)

fun String.lastChar(): Char = this[length - 1] // 이렇게도 표현 가능하다.
```

> 하지만 확장 함수가 캡슐화를 깨지는 않는다는 사실을 기억하라. 클래스 안에서 정의한 메서드와 달리 확장 함수 안에서는 private, protected 멤버를 사용할 수 없다.

```kotlin
package ch3.other

import ch3.lastChar as last // 함수 alias 도 설정 가능

fun main() {
    val c = "Kotlin".last()
}
```

> 확장 함수를 다른곳에서 사용하려면 임포트 해야 한다.
> 확장 함수는 오버라이드할 수 없다.

```kotlin
fun main() {
    println(Foo().bar()) // "hello"
}
class Foo {
    fun bar() = "hello"
}
fun Foo.bar() = "extension function"
```

> 어떤 클래스를 확장한 함수와 그 클래스의 멤버 함수의 이름과 시그니처가 같다면 멤버 함수가 호출된다.(우선순위가 더 높다.)

### 확장 프로퍼티

```kotlin
val String.lastChar: Char
    get() = this[length - 1]

fun main() {
    println("Kotlin".lastChar)
}
```

> 프로퍼티라는 이름으로 불리지만 상태를 저장할 적절한 방법이 없기 때문에 실제로 확장 프로퍼티는 아무 상태도 가질 수 없다.
> (기존 클래스의 인스턴스 객체에 필드를 추가할 방법은 없다)

**뒷받침하는 필드가 없어서 기본 게터 구현을 제공할 수 없으므로 get() 을 꼭 정의해야 한다. 마찬가지로 초기화 코드도 쓸 수 없다.**

```kotlin
var StringBuilder.lastChar: Char
    get() = get(length - 1)
    set(value) {
        this.setCharAt(length - 1, value)
    }

fun main() {
    val sb = StringBuilder("Kotlin?")
    sb.lastChar = '!'
}
```

> 변경 가능한 확장 프로퍼티 선언

## 컬렉션 처리

- 컬렉션을 처리할 때 유용한 라이브러리 함수 몇 가지

### 가변 길이 인자

```kotlin
fun listOf<T>(vararg values: T): List<T> { ... }

fun sumWithVararg(vararg elements: Int): Int = elements.sum()
```

```kotlin
fun main(args: Array<String>)
```

> 이미 배열에 들어있는 원소를 가변 길이 인자로 넘길 때도 코틀린과 자바 구문이 다르다. 자바에서는 배열을 그냥 넘기면 되지만 코틀린에서는 배열을 명시적으로 풀어서 배열의 각 원소가 인자로 전달되게 해야한다. 기술적으로는 스프레드 연산자(*)가 그런 작업을 해준다.

### 중위 함수

```kotlin
val map = mapOf(1 to "one", 2 to "two", 3 to "three")
val map2 = mapOf(Pair(1, "one"), Pair(2, "two"), Pair(3, "three"))

infix fun LocalDate.to(toDate: LocalDate) = this.rangeTo(toDate)
val startDate: LocalDate = LocalDate.of(2022, 2, 20)
val range: ClosedRange<LocalDate> = startDate to LocalDate.now()
```

### 구조 분해 선언

```kotlin
val (number, name) = 1 to "one"

for ((index, element) in list.withIndex()) {
	println("$index : $element")
}
```

## 문자열과 정규식

```kotlin
fun main() {
    println("12.345-6.A".split(".")) // 자바에서는 문자열을 Regex 로 인식하지만 코틀린은 여러가지 타입의 인자를 지원하고 있다.
}

// path = /Users/pug/kotlin/test.kt
fun parsePath(path: String) {
    val directory = path.substringBeforeLast("/")
    val fullName = path.substringAfterLast("/")
    val fileName = fullName.substringBeforeLast(".")
    val extension = fullName.substringAfterLast(".")
}

fun parsePathWithRegex(path: String) {
    val regex = """(.+)/(.+)\.(.+)""".toRegex()
    val matchResult = regex.matchEntire(path)
    if (matchResult != null) {
        val (directory, filename, extension) = matchResult.destructured
    }
}
```
