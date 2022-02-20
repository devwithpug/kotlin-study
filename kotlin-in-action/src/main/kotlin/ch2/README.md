# 2장 코틀린 기초

## 함수

```kotlin
// kotlin 의 if 는 자바와 다르게 식(expression)이지 문(statement)이 아니다.
// 또한, 타입 추론 기능때문에 리턴 타입을 생략해도 된다.
fun max(a: Int, b: Int) = if (a > b) a else b

// 하지만 블록이 본문인 함수는 리턴 값을 생략할 수 없다.
// 이렇게 설계한 이유는 아주 긴 함수에 return 문이 여럿 들어있는 경우
// 반환 타입을 꼭 명시함으로써 함수가 어떤 타입의 값을 반환하고 어디서 그런 값을 반환하는지 더 쉽게 알아볼 수 있다.
fun maxWithBlockStatement(a: Int, b: Int): Int {
    return if (a > b) a else b
}

fun main() {
    println("max = ${max(3, 5)}")
}
```

## 변수

```kotlin
val a = 1 // 타입 추론
val b: Int // 초기화 없이 선언하는 경우 반드시 타입을 지정해주어야 한다.
b = 2

var c = 3 // variable
c = 4
c = "Error: type mismatch" // 컴파일 에러 발생

// 오직 한 초기화 문장만 실행됨을 컴파일러가 확인할 수 있다면
// 조건에 따라 val 값을 다른 여러 값으로 초기화할 수 있다.
val message: String
if (canPerformOperation()) {
	message = "Success"
} else {
	message = "Failed"
}
```

> 기본적으로는 모든 변수를 val 키워드를 사용해 불변 변수로 선언하고, 나중에 꼭 필요할 때에만 var로 변경하라. 변경 불가능한 참조와 변경 불가능한 객체를 부수 효과가 없는 함수와 조합해 사용하면 코드가 함수형 코드에 가까워 진다.

## 문자열 템플릿

```kotlin
fun main() {
    val name = "pug.gg"
    println("hello $name")
    // 결과: hello pug.gg

    // 예약어를 문자열에 넣으려면 '\' 이스케이프를 사용
    println("50000\$(bucks)")
    // 결과: 5000$(bucks)
	
    // 중괄호로 둘러싼 식 안에서 큰 따옴표를 사용할 수도 있다.
    println("Hello, ${if (name.length > 1) name else "Player"}")
}
```

> 컴파일된 코드는 StringBuilder를 사용하고 문자열 상수와 변수의 값을 append로 문자열 빌더 뒤에 추가한다.

> 자바 9에서 최적화 되었던 invoke dynamic 을 코틀린에서는 코틀린 1.4.20 부터 옵션을 주어 invoke dynamic으로 컴파일이 가능하며, 코틀린 1.5.20 부터는 기본으로 indy(invoke dynamic) 이 적용된다.
[참고1](https://www.baeldung.com/kotlin/string-interpolation)
[참고2](https://kotlinlang.org/docs/whatsnew1520.html#string-concatenation-via-invokedynamic)

## 클래스

```java
// java class
public class JavaPerson {
    private final String name;

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
```

```kotlin
// kotlin class
class Person(val name: String)
```

### 프로퍼티

```kotlin
// value object 라고 부른다.
// 코틀린의 기본 가시성은 public 이다.
class Person(
    val name: String, // 읽기 전용 프로퍼티: private & getter
    var isMarried: Boolean? = null // 쓸 수 있는 프로퍼티: private & getter, setter
)

fun main() {
    val person = Person("pug.gg")
    person.isMarried = false
    // getter 를 사용하는 대신 프로퍼티로 직접 명시한다.
    println("name: ${person.name} / ${person.isMarried}")
}
```

### 커스텀 접근자

```kotlin
// data: toString 자동생성을 위해 일부로 선언
data class Rectangle(
    private val height: Int,
    private val width: Int
) {
    // 커스텀 접근자 getter
    val isSquare: Boolean
        get() {
            return height == width
        }
    // 블록 & 리턴 타입 생략 가능
    val isSquareWithNoBlockStatement get() = height == width

    // 커스텀 접근자 setter
    var name: String = ""
    set(value) {
        println("$this setter called")
        field = value
    }
}

fun main() {
    val rectangle = Rectangle(5, 5)
    println("$rectangle is square? ${rectangle.isSquare}")
    rectangle.name = "Square Rectangle"
}
```

## enum & when

코틀린에서의 enum 은 소프트 키워드(soft keyword)라 부르는 존재다. enum 은 class 앞에 있을 때는 특별한 의미를 지니지만 다른 곳에서는 이름에 사용할 수 있다. 반면 class 는 키워드이기 때문에 이름으로 사용할 수 없다. (ex clazz)

```kotlin
enum class Color(
    private val r: Int,
    private val g: Int,
    private val b: Int
) {
    RED(255, 0, 0),
    BLUE(0, 0, 255),
    GREEN(0, 255, 0),
    YELLOW(255, 255, 0),
    ORANGE(255, 165, 0); // 반드시 세미콜론을 사용해야 함.

    fun rgb() = (r * 256 + g) * 256 + b
}

fun main() {
    println(Color.BLUE.rgb())
}
```

> enum 클래스 안에 메서드를 정의하는 경우 반드시 enum 상수 목록과 메서드 정의 사이에 세미콜론을 넣어야 한다.

### when으로 enum 클래스 다루기

```kotlin
fun getMnemonic(color: Color) =
    when (color) {
        Color.RED -> "Richard"
        Color.BLUE -> "Battle"
        Color.GREEN -> "Gave"
        Color.YELLOW -> "York"
        Color.ORANGE -> "Of"
    }
	
fun getWarmth(color: Color) =
    when (color) {
        Color.RED, Color.ORANGE, Color.YELLOW -> "warm"
        Color.GREEN -> "neutral"
        Color.BLUE -> "cold"
    }
	
fun mix(c1: Color, c2: Color) =
    when (setOf(c1, c2)){
        setOf(Color.RED, Color.YELLOW) -> Color.YELLOW
        setOf(Color.YELLOW, Color.BLUE) -> Color.GREEN
        else -> throw Exception("Dirty Color")
    }
	
// when 분기 조건에서 여러 Set 인스턴스를 생성하면 가비지 객체가 증가하므로
// 메모리 효율상 좋지 않다. 따라서 아래와 같이 성능 개선이 가능하다.
fun mixOptimized(c1: Color, c2: Color) =
    when {
        (c1 == Color.RED && c2 == Color.YELLOW) ||
        (c1 == Color.YELLOW && c2 == Color.RED) ->
            Color.ORANGE
        
        // 생략

        else -> throw Exception("Dirty Color")
    }
```

> when에 아무 인자도 없으려면 각 분기의 조건이 불리언 결과를 계산하는 식이어야 한다.

### 스마트 캐스트: 타입 검사와 타입 캐스트를 조합

```kotlin
interface Expr
class Num(val value: Int): Expr
class Sum(val left: Expr, val right: Expr): Expr

// (1 + 2) + 4 연산은 아래와 같이 표현된다.
// Sum(Sum(Num(1), Num(2)), Num(4))

fun eval(e: Expr): Int {
    if (e is Num) { // java 의 instanceof 와 같다.
        return e.value // smart cast! 변수의 배경색으로 표현된다. (Smart cast to ch2.Num)
    }
    if (e is Sum) {
        return eval(e.right) + eval(e.left)
    }
    throw IllegalArgumentException("Unknown expression")
}

fun main() {
    val expr = Sum(Sum(Num(1), Num(2)), Num(4))
    println("result = ${eval(expr)}")
}
```

> 스마트 캐스트는 is로 변수에 든 값의 타입을 검사한 당므에 그 값이 바뀔 수 없는 경우에만 작동한다. 반드시 val 이어야 하며 커스텀 접근자를 사용한것이어도 안 된다. val 이지만 커스텀 접근자를 사용하는 경우에는 해당 프로퍼티에 대한 접근 get() 이 항상 같은 값을 내놓는다고 확신할 수 없기 때문이다.
> 명시적 타입 캐스팅은 as 키워드를 사용한다. `val n = e as Num`

```kotlin
// when을 사용하여 아래와 같이 코드를 다듬을 수 있다.
fun eval(e: Expr): Int =
    when (e) {
        is Num -> e.value
        is Sum -> eval(e.right) + eval(e.left)
        else -> throw IllegalArgumentException("Unknown expression") 
    }
```

## 이터레이션

```kotlin
// while 루프는 자바와 동일하다.

fun main() {
    /**
     * for loop with range
     */
    // 자바의 for 루프 요소가 없다.
    // 이를 range 로 대신하여 표현한다.
    val oneToTen = 1..10 // range

    for (i in oneToTen) {
        println(i)
    }

    val tenDownToOneByStepTwo = 10 downTo 1 step 2
    for (i in tenDownToOneByStepTwo) {
        println(i)
    }

    val oneUntilTen = 1 until 10
    for (i in oneUntilTen) {
        println(i)
    }

    /**
     * Map Iteration
     */
    val binaryReps = TreeMap<Char, String>()

    for (c in 'A'..'F') {
        val binary = Integer.toBinaryString(c.code)
        binaryReps[c] = binary
    }

    for ((letter, binary) in binaryReps) {
        println("$letter = $binary")
    }

    /**
     * List Iteration
     */
    val list = listOf("A", "B", "C")

    for ((idx, element) in list.withIndex()) { // 인덱스와 함께 이터레이션 가능
        println("$idx: $element")
    }

    /**
     * in (contains)
     */
    val range = 1..100
    println(5 in range)
}
```

## 예외

```kotlin
// 예외 throw, try, catch, finally 모두 java 와 동일하게 사용 가능하다.
// kotlin 에서는 checked, unchecked 예외를 나누지 않는다.
// 불필요한 IOException 에 대한 처리를 하지 않아도 된다!
fun readNumber(reader: BufferedReader) {
    val number = try {
        Integer.parseInt(reader.readLine())
    } catch (e: NumberFormatException) {
        -1
    }
    println(number)
}

fun main() {
    val reader = BufferedReader(StringReader("not a number"))
    readNumber(reader) // -1 (NumberFormatException)
}
```

> 자바 코드와 가장 큰 차이는 throws 절이 코드에 없다는 점이다. 코틀린은 checked exception 과 unchecked exception 을 구별하지 않는다. 발생한 예외를 잡아내도 되고 잡아내지 않아도 된다. 자바는 checked exception 처리를 강제한다. 하지만 프로그래머들이 의미 없이 예외를 다시 던지거나, 예외를 잡되 처리하지는 않고 그냥 무시하는 코드를 작성하는 경우가 흔하다.
