# 7장 연산자 오버로딩과 기타 관례

## 산술 연산자 오버로딩

```kotlin
data class Point(
    val x: Int,
    val y: Int
) {
    operator fun plus(other: Point): Point {
        return Point(x + other.x, y + other.y)
    }
}

fun main() {
    val p1 = Point(10, 20)
    val p2 = Point(30, 40)
    println(p1 + p2) // Point(x=40, y=60)
    var p3 = Point(0, 0)
    p3 += p1 // 복합 대입 연산자도 함께 지원해준다
    p3 += p2
    println(p3) // Point(x=40, y=60)
}
```

> 연산자를 오버로딩하는 함수 앞에는 꼭 operator가 있어야 한다.

```kotlin
operator fun Point.plus(other: Point): Point {
    return Point(x + other.x, y + other.y)
}
```

> 확장 함수로 정의할 수도 있다.

```kotlin
operator fun Point.times(scale: Double): Point {
    return Point((x * scale).toInt(), (y * scale).toInt())
}
```

> 코틀린에서는 프로그래머가 직접 연산자를 만들어 사용할 수 없고 언어에서 미리 지정해둔 연산자만 오버로딩할 수 있으며 관례에 따르기 위해 클래스에서 정의해야 하는 이름이 연산자별로 정해져 있다. (times, div, mod, plus, minus)
> 직접 정의한 함수를 통해 구현하더라도 연산자 우선순위는 언제나 표준 숫자 타입에 대한 연산자 우선순위와 같다.

> 코틀린 연산자가 자동으로 교환 법칙을 지원하지는 않음에 유의하라. `1.5 * p` 라고 쓸 수 있어야 한다면 Double.times 연산자 오버로딩을 추가적으로 정의해야 한다.

> 단항 연산자도 오버로딩 가능하다. (unaryPlus, unaryMinus, not, inc, dec)

> 비교 연산자 == 는 컴파일 타임에 equals 메서드 호출로 컴파일된다. != 또한 마찬가지다

> 순서 연산자의 경우 자바에서는 짧게 호출할 수 있는 방법이 없다. < 나 > 등의 연산자로는 원시 타입의 값만 비교할 수 있다. foo.compareTo(bar) 와 같이 명시적으로 사용해야 한다.
> 하지만 코틀린에서는 <, >, <=, >= 모두 compareTo 호출로 컴파일 된다. compareTo 가 반환하는 값은 Int 이다.
> 따라서 p1 < p2 는 p1.compareTo(p2) < 0 과 같다. 다른 비교 연산자도 똑같은 방식으로 작동한다.

## 컬렉션의 관례

```kotlin
operator fun Point.get(index: Int): Int {
    return when (index) {
        0 -> x
        1 -> y
        else -> throw IndexOutOfBoundsException()
    }
}

fun main() {
    val p = Point(10, 20)
    println(p[1]) // 20
}
```

> 위의 관례는 다음과 같이 표현된다. `x[a, b] -> x.get(a, b)`

```kotlin
data class Rectangle(
    val upperLeft: Point,
    val lowerRight: Point
)

operator fun Rectangle.contains(p: Point): Boolean {
    return p.x in upperLeft.x until lowerRight.x &&
        p.y in upperLeft.y until lowerRight.y
}

fun main() {
    val rect = Rectangle(Point(10, 20), Point(50, 50))
    println(Point(20, 30) in rect) // true
    println(Point(5, 5) in rect) // false
}
```

> in 관례의 경우 contains 메서드와 대응된다. `a in c -> c.contains(a)`

## 범위의 관례

### rangeTo 관례

```kotlin
fun main() {
    val now = LocalDate.now()
    val closedRange = now..now.plusDays(10)
    // now.rangeTo(now.plusDays(10)) 으로 변환된다.

    val n = 9
    0..(n + 1) // 0..n+1 이라고 써도 되지만 괄호를 치면 뜻이 명확해진다

    (0..n).forEach { print(it) } // 0..n.forEach {} 와 같이는 컴파일 할 수 없다. 우선순위 때문!
}
```

### for 루프를 위한 iterator 관례

> 코틀린의 for 루프는 범위 검사와 똑같이 in 연산자를 사용한다. 하지만 이 경우 in의 의미는 다르다.
> iterator() 를 호출해서 이터레이터를 얻은 다음, 자바와 마찬가지로 그 이터레이터에 대해 hasNext 와 next 호출을 반복하는 식으로 변환된다.

```kotlin
fun main() {
    val now = LocalDate.now()
    val closedRange = now..now.plusDays(10)
    
    for (date in closedRange) { // 아래 구현을 통해 사용할 수 있다.
        println(date)
    }
}

operator fun ClosedRange<LocalDate>.iterator(): Iterator<LocalDate> =
    object : Iterator<LocalDate> {
        var current = start
        override fun hasNext() = current <= endInclusive
        override fun next() = current.apply { current = plusDays(1) }
    }
```

### 구조 분해 선언과 component 함수

```kotlin
fun main() {
    val p = MyPoint(10, 20)
    val (x, y) = p
    println("$x, $y")
}

class MyPoint(
    val x: Int,
    val y: Int
) {
    operator fun component1() = "x[$x]"
    operator fun component2() = "y[$y]"
}
```

> 구조 분해를 사용하면 복합적인 값을 분해해서 여러 다른 변수를 한꺼번에 초기화할 수 있다.
> data 클래스는 맨 앞의 다섯 원소에 대한 componentN 을 제공한다.

### 위임 프로퍼티

> 위임 프로퍼티는 코틀린이 제공하는 관례에 의존하는 특성 중에 독특하면서 강력한 기능이다.
> 위임 프로퍼티를 사용하면 값을 뒷받침하는 필드에 단순히 저장하는 것보다 더 복잡한 방식으로 작동하는 프로퍼티를 쉽게 구현할 수 있다. 예를 들어 프로퍼티는 위임을 사용해 자신의 값을 필드가 아니라 데이터베이스 테이블이나 브라우저 세션, 맵 등에 저장할 수 있다.

> 위임은 객체가 직접 작업을 수행하지 않고 다른 도우미 객체가 그 작업을 처리하게 맡기는 디자인 패턴을 말한다. 이 때 작업을 처리하는 도우미 객체를 위임 객체라고 부른다.

```kotlin
class Foo {
    var p: Delegate by Delegate()
}

class Delegate {
    operator fun getValue(foo: Foo, property: KProperty<*>): Delegate {
        TODO("Not yet implemented")
    }

    operator fun setValue(foo: Foo, property: KProperty<*>, type: Delegate) {
        TODO("Not yet implemented")
    }
}
```

> 프로퍼티 위임 관례를 따르는 Delegate 클래스는 getValue() 와 setValue()를 제공해야 한다.

```kotlin
class Person(
    val name: String
) {
    val emails by lazy { /* loadEmails(this) */ }
}
```

> 위임 프로퍼티 키워드 by lazy 를 사용해 프로퍼티의 초기화를 지연시킬 수 있다.
> lazy 함수는 기본적으로 스레드 안전하다. 하지만 필요에 따라 동기화에 사용할 락을 lazy 함수에 전달할 수도 있고, 다중 스레드 환경에서 사용하지 않을 프로퍼티를 위해 lazy 함수가 동기화를 하지 못하게 막을 수도 있다.

# 요약

- 코틀린에서는 정해진 이름의 함수를 오버로딩함으로써 표준 수학 연산자를 오버로딩할 수 있다. 하지만 직접 새로운 연산자를 만들 수는 없다.
- 비교 연산자는 equals와 compareTo 메서드로 변환된다.
- 클래스에 get, set, contains 라는 함수를 정의하면 그 클래스의 인스턴스에 대해 []와 in 연산을 사용할 수 있고, 그 객체를 코틀린 컬렉션 객체와 비슷하게 다룰 수 있다.
- 미리 정해진 관례를 따라 rangeTo, iterator 함수를 정의하면 범위를 만들거나 컬렉션과 배열의 원소를 이터레이션할 수 있다.
- 구조 분해 선언을 통해 한 객체의 상태를 여러 변수에 대입할 수 있다.
- 위임 프로퍼티를 통해 프로퍼티 값을 저장하거나 초기화하거나 읽거나 변경할 때 사용하는 로직을 재활용할 수 있다.
