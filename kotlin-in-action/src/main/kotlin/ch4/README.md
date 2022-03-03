# 4장 클래스, 객체, 인터페이스

코틀린의 클래스와 인터페이스는 자바와는 약간 다르다. 예를 들어 인터페이스에 프로퍼티 선언이 들어갈 수 있다. 자바와 달리 코틀린 선언은 기본적으로 final이며 public이다. 게다가 중첩 클래스는 기본적으로 내부클래스가 아니다.

## 클래스와 인터페이스

```kotlin
// 간단한 인터페이스 선언
interface Clickable {
    fun click() // 추상 메서드
    fun defaultMethod() = println("default method")
}
```

```kotlin
class Button : Clickable { // extends, implements 모두 : 로 표현된다.
    override fun click() { // 반드시 override 를 사용해야 한다.
        // 추상 메서드 구현
    }
}

```

> 한 클래스에서 동일한 메서드를 가진 두 인터페이스를 함께 구현하면 어느쪽의 메서드도 선택되지 않는다. 클래스가 구현하는 두 상위 인터페이스에 정의된 메서드 구현을 대체할 오버라이딩 메서드를 직접 제공하지 않으면 다음과 같은 컴파일러 오류가 발생한다.

```
Class 'Button' must override public open fun defaultMethod(): Unit defined in ch4.Clickable because it inherits multiple interface methods of it
```

```kotlin
override fun defaultMethod() {
	super<Clickable>.defaultMethod()
	super<DuplicateMethodInterface>.defaultMethod()
}
```

> super<> 를 통해 상위 타입의 멤버 메서드를 호출할 수 있다.

> 코틀린은 자바 6와 호환되게 설계됐다. 따라서 인터페이스의 디폴트 메서드를 지원하지 않는다. 따라서 코틀린은 디폴트 메서드가 있는 인터페이스를 일반 인터페이스와 디폴트 메서드 구현이 정적 메서드로 들어있는 클래스를 조합해 구현한다.

### open, final, abstract 변경자: 기본적으로 final

> 자바에서는 취약한 기반 클래스 문제를 피하기 위해 "상속을 위한 설계와 문서를 갖추거나, 그럴 수 없다면 상속을 금자하라" 라는 조언을 한다.(이펙티브 자바)
> 코틀린도 마찬가지 철학을 따른다. 코틀린의 클래스와 메서드는 기본적으로 final 이다.

```kotlin
open class RichButton : Clickable {
    fun disable() {} // final method
    open fun animate() {} // open method 오버라이드 가능
    override fun click() {} // override method 기본적으로 open 이다.
}
```

> 어떤 클래스의 상속을 허용하려면 클래스 앞에 open 변경자를 붙인다. 오버라이드를 허용하고 싶은 메서드나 프로퍼티 앞에도 open 변경자를 붙여야 한다.

```kotlin
final override fun click() {} // 하위 클래스에서 오버라이드하지 못하게 금지할 수 있다.
```


> 클래스의 기본적인 상속 가능 상태를 final로 함으로써 얻을수 있는 큰 이익은 다양한 경우에 스마트 캐스트가 가능하다는 점이다. 스마트 캐스트는 타입 검사 뒤에 변경될 수 없는 변수에만 적용이 가능하므로 val 이면서 커스텀 접근자가 없고 프로퍼티가 final 이어야 한다는 의미이다.

```kotlin
abstract class Animated { // abstract class
    abstract fun animate() // abstract method
    open fun stopAnimating() {} // open method
    fun animateTwice() {} // final method
} 
```

> 자바처럼 코틀린에서도 abstract로 선언한다. 추상 멤버는 항상 열려있으므로 open 변경자를 명시할 필요가 없다.
> 비추상 메서드는 기본적으로 final 이지만 원한다면 open으로 오버라이드를 허용할 수 있다.

| 변경자      | 이 변경자가 붙은 멤버는 | 설명                         |
|:---------|:--------------|:---------------------------|
| final    | 오버라이드할 수 없음   | 클래스 멤버의 기본 변경자다            |
| open     | 오버라이드할 수 있음   | 반드시 open을 명시해야 오버라이드할 수 있다 |
| abstract | 오버라이드 필수      | 추상클래스의 멤버에만 이 변경자를 붙일 수 있음 |
| override | 오버라이드 하는 중    | 기본적으로 open이다.              |

### 가시성 변경자: 기본적으로 public

> 자바의 패키지 전용 가시성에 대한 대안으로 코틀린에는 internal 이라는 새로운 가시성 변경자를 도입했다. 모듈 내부에서만 볼 수 있음을 의미한다.

| 변경자        | 클래스 멤버      | 최상위 선언     |
|:-----------|:------------|:-----------|
| public(기본) | 모든 곳에서      | 모든 곳에서     |
| internal   | 같은 모듈 안에서만  | 같은 모듈 안에서만 |
| protected  | 하위 클래스 안에서만 | (적용 불가)    |
| private    | 같은 클래스 안에서만 | 같은 파일 안에서만 |

```kotlin
internal open class TalkativeButton : Clickable {
    override fun click() {}
    private fun yell() {}
    protected fun whisper() {}
}

fun TalkativeButton.giveSpeech() { // 컴파일에러 public 멤버가 자신의 internal 수신타입을 호출함. 자신의 가시성과 같거나 더 높아야만 한다.
    click() // open, public (가능)
    yell() // 컴파일에러 private 멤버 호출 불가능
    whisper() // 컴파일에러 protected 멤버 호출 불가능 
}
```

> 코틀린의 protected 는 자바와 다르다. protected 멤버는 오직 어떤 클래스나 그 클래스를 상속한 클래스 안에서만 보인다. 클래스를 확장한 함수는 그 클래스의 private 이나 protected 멤버에 접근할 수 없다.

### 내부 클래스와 중첩된 클래스: 기본적으로 중첩 클래스

> 코틀린의 중첩 클래스는 명시적으로 요청하지 않는 한 바깥쪽 클래스 인스턴스에 대한 접근 권한이 없다.

> 코틀린 중첩 클래스에 아무런 변경자가 붙지 않으면 자바 static 중첩 클래스와 같다. 이를 내부 클래스로 변경해서 바깥쪽 클래스에 대한 참조를 포함하게 만들고 싶다면 inner 변경자를 붙여야 한다.

```kotlin
class Outer {
    inner class Inner {
        fun getOuterReference(): Outer = this@Outer // 바깥 클래스의 참조 접근 방법
    }

    // class Inner {
    //     fun getOuterReference(): Outer = this@Outer // 컴파일 에러! 바깥쪽 클래스에 대한 참조가 없음
    // } 
}
```

| 클래스 B 안에 정의된 클래스 A          | 자바에서는          | 코틀린에서는        |
|:----------------------------|:---------------|:--------------|
| 중첩 클래스(바깥쪽 클래스에 대한 참조 저장 X) | static class A | class A       |
| 내부 클래스(바깥쪽 클래스에 대한 참조 저장 O) | class A        | inner class A |

### 봉인된 클래스: 클래스 계층 정의 시 계층 확장 제한

```kotlin
// before
interface Expr {
    class Num(val value: Int): Expr
    class Sum(val left: Expr, val right: Expr): Expr

    fun eval(e: Expr): Int =
        when (e) {
            is Num -> e.value
            is Sum -> eval(e.right) + eval(e.left)
            else -> throw IllegalArgumentException() // 항상 디폴트 분기를 추가해야 한다.
        }
}

// after
sealed class SealedClassExpr {
    class Num(val value: Int): SealedClassExpr()
    class Sum(val left: SealedClassExpr, val right: SealedClassExpr): SealedClassExpr()
    fun eval(e: SealedClassExpr): Int =
        when (e) {
            is Num -> e.value
            is Sum -> eval(e.right) + eval(e.left)
        } // when 식이 모든 하위 클래스를 검사하므로 별도의 else 분기가 없어도 된다.
}

// kotlin 1.5 버전부터는 sealed interface 가 가능하다.
sealed interface SealedInterfaceExpr {
    class Num(val value: Int): SealedInterfaceExpr
    class Sum(val left: SealedInterfaceExpr, val right: SealedInterfaceExpr): SealedInterfaceExpr
    fun eval(e: SealedInterfaceExpr): Int =
        when (e) {
            is Num -> e.value
            is Sum -> eval(e.right) + eval(e.left)
        } // when 식이 모든 하위 클래스를 검사하므로 별도의 else 분기가 없어도 된다.
}
```

> when 식에서 sealed 클래스의 모든 하위 클래스를 처리한다면 디폴트 분기가 필요 없다. 나중에 sealed 클래스의 상속 계층에 새로운 하위 클래스를 추가해도 when 식이 컴파일되지 않는다. 따라서 when 식을 고쳐야 한다는 사실을 쉽게 알 수 있다.

## 뻔하지 않은 생성자와 프로퍼티

자바와 비슷하지만, 코틀린은 주 생성자와 부 생성자를 구분한다. 또한 초기화 블록을 통해 초기화 로직을 추가할 수 있다.

```kotlin
// 주 생성자
class User(val nickname: String)

// 구조는 아래와 같다.
class User constructor(_nickname: String) { // constructor 는 주 생성자 또는 부 생성자 정의를 시작할 때 사용한다.
    private val nickname: String
    init { // 초기화 블록
        nickname = _nickname
    }
}
```

> 생성자 파라미터에도 디폴트 값을 정의할 수 있다.
> 클래스의 인스턴스는 new 키워드 없이 생성자를 직접 호출한다.

```kotlin
open class Car(val name: String)

class Bmw(name: String) : Car(name) // 기반 클래스의 생성자 호출
```

> 클래스를 상속한 하위 클래스는 반드시 상위 클래스의 생성자를 호출해야 한다. 이 규칙으로 인해 기반 클래스의 이름 뒤에는 괄호가 들어간다. 반면 인터페이스는 생성자가 없으므로 괄호가 없다. 따라서 클래스와 인터페이스를 쉽게 구분할 수 있다.

```kotlin
class Secretive private constructor()
```

> 어떤 클래스를 클래스 외부에서 인스턴스화하지 못하게 막고 싶다면 모든 생성자를 private으로 만들면 된다.

```kotlin
open class View {
    constructor() {
        // 부 생성자
    }
    
    constructor(name: String) {
        // 여러 부 생성자를 선언할 수 있다.
    }
}
```

## 데이터 클래스

```kotlin
data class DataClass(
    val foo: String,
    val bar: String
)

fun main() {
    val dataClass = DataClass("foo", "bar")
    dataClass.foo
    dataClass.bar
    dataClass.toString()
    dataClass.hashCode()
}
```

* 코틀린은 동등성 연산에 ==를 사용함
> 자바에서는 ==를 원시 타입과 참조 타입을 비교할 때 사용한다. 코틀린에서는 == 연산자가 두 객체를 비교하는 기본적인 방법이다. ==는 내부적으로 equals를 호출해서 객체를 비교한다. 따라서 클래스가 equals를 오버라이드하면 ==를 통해 안전하게 그 클래스의 인스턴스를 비교할 수 있다.
> 참조 비교를 위해서는 === 연산자를 사용할 수 있다. === 연산자는 자바에서 객체의 참조를 비교할 때 사용하는 == 연산자와 같다.

## 클래스 위임

```kotlin
class DecoratingCollection<T> : Collection<T> {
    private val innerList = arrayListOf<T>()
    override val size: Int
        get() = TODO("Not yet implemented")

    override fun contains(element: T): Boolean {
        TODO("Not yet implemented")
    }

    override fun containsAll(elements: Collection<T>): Boolean {
        TODO("Not yet implemented")
    }

    override fun isEmpty(): Boolean {
        TODO("Not yet implemented")
    }

    override fun iterator(): Iterator<T> {
        TODO("Not yet implemented")
    }
}

class DelegatingCollection<T>(
    innerList: Collection<T> = arrayListOf()
) : Collection<T> by innerList {
    // 위에서 필수로 구현해야 했던 메서드들을 자동으로 생성해준다.
    // 구현도 알아서 해준다.
}

fun main() {
    val strings = DelegatingCollection(listOf("A", "B", "C"))
    println(strings.contains("B")) // 구현이 되어 있다.
    // 정확히는 arrayListOf() 즉, ArrayList<T> 에게 위임한 것이다.
}
```

## object 키워드 사용

```kotlin
object Payroll {
    val foo = mutableListOf<String>()
}

fun main() {
    Payroll.foo // 싱글턴 객체
    // Payroll() 불가능
}

class CompanionClass {
    companion object { // 동반 객체(정적)
        const val CONST_VALUE: String = "static val"
    }
}
```

> 객체(object) 선언을 사용하면 코틀린답게 싱글턴 클래스를 정의할 수 있다.
> 동반 객체(companion object)는 자바의 정적 메서드와 필드 정의를 대신한다.
> 동반 객체도 다른 객체와 마찬가지로 인터페이스를 구현할 수 있다. 외부에서 동반 객체에 대한 확장 함수와 프로퍼티를 정의할 수 있다.
