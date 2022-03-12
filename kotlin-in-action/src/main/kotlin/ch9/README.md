# 9장 제네릭스

**다루는 내용**
- 제네릭 함수와 클래스를 정의하는 방법
- 타입 소거와 실체화한 타입 파라미터
- 선언 지점과 사용 지점 변성(다양성)

실체화한 타입 파라미터(reified type parameter)나 선언 지점 변성(declaration-site variance) 등은 코틀린의 새로운 내용이다.

## 9.1 제네릭 타입 파라미터

```kotlin
val authors = listOf("Dmitry", "Svetlana")
```

> 코틀린 컴파일러는 보통 타입과 마찬가지로 타입 인자도 추론할 수 있다.

> 자바와 달리 코틀린에서는 제네릭 타입의 타입 인자를 프로그래머가 명시하거나 컴파일러가 추론할 수 있어야 한다. (raw 타입으로 선언할 수 없다.)

```kotlin
fun <T> List<T>.slice(indices: IntRange): List<T> {
    TODO("함수의 타입 파라미터 T가 수신 객체와 반환 타입에 쓰인다.")
}

fun <T, R> List<T>.sliceTest(indices: IntRange): List<R> {
    TODO("두 가지 타입 인자를 받는 제네릭 메서드")
}

fun main() {
    // val rawList: List // 컴파일 에러: 코틀린에서는 raw 타입 사용을 허용하지 않는다.

    val letters = ('a'..'z').toList()
    letters.slice<Char>(0..2) // 메서드를 호출할 때 타입 인자를 명시적으로 선언할 수 있다.
    letters.slice(0..2) // 타입 인자 생략 : 코틀린 컴파일러가 알아서 추론해준다.
}
```

> 기본적으로 코틀린은 자바와 동일하게 제네릭을 이용해서 타입 파라미터를 선언할 수 있다.

```kotlin
interface MyList<T> {
    operator fun get(index: Int): T
}

class StringList: MyList<String> {
    override fun get(index: Int): String {
        TODO("Not yet implemented")
    }
}

class MyArrayList<T>: MyList<T> {
    override fun get(index: Int): T {
        TODO("Not yet implemented")
    }
}
```

> 자바 제네릭과 동일하다. MyList 의 구현체인 두 클래스의 get() 메서드 리턴 타입을 자세히 보자.

### 9.1.3 타입 파라미터 제약

> 타입 파라미터 제약(type parameter constraint)은 클래스나 함수에 사용할 수 있는 타입 인자를 제한하는 기능이다.  
> ex) List.sum() 의 경우 String과 같은 타입에는 적용할 수 없다.

```kotlin
fun <T : Number> List<T>.sum(): T

// 자바 코드
<T extends Number> T sum(List<T> list) // 자바의 경우 extends로 표현한다.
```

> 타입 파라미터 뒤에 상한을 지정함으로써 제약을 정의할 수 있다.

```kotlin
interface Gasoline
interface Electric

data class GasolineVehicle(
    val name: String
) : Gasoline

data class ElectricVehicle(
    val name: String
) : Electric

data class HybridVehicle(
    val name: String
) : Gasoline, Electric

fun <T : Gasoline> T.refuel() {}

fun <T : Electric> T.recharge() {}

fun main() {
    val gasolineVehicle = GasolineVehicle("G1")
    val electricVehicle = ElectricVehicle("E1")
    val hybridVehicle = HybridVehicle("H1")

    gasolineVehicle.refuel() // 급유만 가능
    electricVehicle.recharge() // 충전만 가능
    hybridVehicle.refuel() // 급유도 가능
    hybridVehicle.recharge() // 충전도 가능
}
```

> 타입 파라미터 제약을 위한 임의의 클래스 생성  
> 가솔린 차량은 급유만 가능하다.  
> 전기 차량은 충전만 가능하다.  
> 하이브리드 차량은 급유, 충전 모두 가능하다.

```kotlin
fun <T> T.hybridOnly() where T : Gasoline, T : Electric {}
```

> 타입 파라미터에 대해 둘 이상의 제약을 가해야 하는 경우 `where` 구문을 사용한다.

### 9.1.4 타입 파라미터를 널이 될 수 없는 타입으로 한정

```kotlin
class Processor<T> {
    fun process(value: T) {
        value?.hashCode() // value 는 nullable 하다
    }
}
```

> 아무런 상한을 정하지 않은 타입 파라미터는 결과적으로 `Any?` 를 상한으로 정한 파라미터와 같다.

```kotlin
class NotNullProcessor<T : Any> {
    fun process(value: T) {
        value.hashCode() // value 는 not null 이다
    }
}
```

> `<T : Any>` 라는 제약은 T 타입이 not null 임을 보장한다.

## 9.2 실행 시 제네릭스의 동작: 소거된 타입 파라미터와 실체화된 타입 파라미터

기본적으로 JVM의 제네릭스는 보통 타입 소거(type erasure)를 사용해 구현된다.  
코틀린에서는 메서드를 inline 으로 선언함으로써 타입 파라미터가 지워지지 않도록 할 수 있다. 코틀린에서는 이를 실체화(reify)라고 부른다.

```kotlin
fun test(value: Any) {
		// 컴파일 에러 Cannot check for instance of erased type: List<String>
    if (value is List<String>) { ... } 
}

fun main() {
		// (예외) 코틀린 컴파일러는 컴파일 시점에 타입 정보가 주어진 경우에는 is 검사를 수행하게 허용할 수 있을 정도로 똑똑하다.
    val value = listOf("A", "B", "C")
    if (value is List<String>) { // is 검사 수행이 가능하다.
        println(value)
    }
}
```

> 타입 파라미터를 따로 저장하지 않기 때문에 런타임에서 타입 인자를 검사할 수 없다.

```kotlin
fun testWithStarProjection(value: Any) {
    if (value is List<*>) { ... } // 알 수 없는 제네릭 타입을 표현할 때 스타 프로젝션(*) 을 사용한다
}
```

> 앞에서 말한대로 코틀린에서는 타입 파라미터를 명시하지 않고 제네릭 타입을 사용할 수 없다. 따라서 어떤 값이 Map 이 아니라 List 라는 사실을 확인하려면 스타 프로젝션을 사용해야 한다.  
> 타입 파라미터가 2개 이상이라면 모든 타입 파라미터에 대해 *를 포함해야 한다.

```kotlin
fun printSum(c: Collection<*>) {
    val intList = c as? List<Int>
        ?: throw IllegalArgumentException()
    println(intList.sum())
}

fun main() {
    printSum(listOf(1, 2, 3)) // 정상적으로 동작한다.
    // printSum(setOf(1, 2, 3)) // 의도한대로 예외가 발생한다.
    // printSum(listOf("1", "2", "3")) // 의도하지 않은 ClassCastException 이 발생한다.
}
```

> `as` 나 `as?` 캐스팅에도 여전히 제네릭 타입을 사용할 수 있다. 하지만 문제는 베이스 클래스(List)는 같지만 타입 파라미터가 다른 타입으로 캐스팅해도 여전히 캐스팅에 성공한다는 점을 조심해야 한다. 실행 시점에는 제네릭 타입의 타입 파라미터를 알 수 없으므로 캐스팅은 항상 성공하지만 컴파일러는 `unchecked cast` 라는 경고를 한다. (경고만 할 뿐 정상적으로 컴파일된다)  
> 위 예제의 마지막 메서드 호출의 경우 문자열 리스트를 파라미터로 넘기게 된다. 해당 파라미터가 `List<Int>` 인지 검사할 수는 없으므로 `as?` 캐스트가 성공하며, 문자열 리스트에 대해 `sum()` 메서드가 호출된다. 따라서 `sum()` 내부에서 예외가 발생하게 된다.

### 9.2.2 실체화한 타입 파라미터를 사용한 함수 선언

위에서 코틀린 컴파일러가 타입 파라미터에 대해 is 검사를 허용하는 예외 케이스를 잠깐 소개했었다.

```kotlin
fun main() {
		// (예외) 코틀린 컴파일러는 컴파일 시점에 타입 정보가 주어진 경우에는 is 검사를 수행하게 허용할 수 있을 정도로 똑똑하다.
    val value = listOf("A", "B", "C")
    if (value is List<String>) { // is 검사 수행이 가능하다.
        println(value)
    }
}
```

> 기본적으로 제네릭은 타입 소거 때문에 사용되는 타입 인자를 알 수 없는게 정상이지만 코틀린 컴파일러 덕분에 다음과 같이 블록 안에서 (컴파일 시점에) 블록 내부에 타입 정보가 주어지는 경우에는 is 검사가 가능하다!  
> 이러한 점을 이용해 코틀린에서는 타입 파라미터에 대한 제약을 피할 수 있다. 바로 inline 메서드를 사용하는 것이다.  
> inline 메서드의 타입 파라미터는 실체화(reify) 되므로 실행 시점에 inline 메서드의 타입 파라미터를 알 수 있다.

```kotlin
inline fun <reified T> testReify(value: Any) = value is T
```

> inline 메서드로 선언한 후 타입 파라미터를 reified로 지정해주면 된다.

## 9.3 변성: 제네릭과 하위 타입

```kotlin
fun printContents(list: List<Any>) {
    println(list.joinToString())
}

fun main() {
    printContents(listOf("foo", "bar")) // foo, bar
}
```

> `String` 클래스는 `Any` 를 확장하므로 위 코드는 문제가 없다(절대로 안전하다).

```kotlin
fun addAnswer(list: MutableList<Any>) {
    list.add(12)
}

fun main() {
    val strings = mutableListOf("foo", "bar")
    // addAnswer(strings) // 컴파일 에러 Type mismatch
}
```

> 메서드가 리스트의 원소를 추가하거나 변경한다면 타입 불일치가 생길 수 있어서 `MutableList<String>` 타입의 객체를 파라미터로 넘길 수 없다.

### 9.3.2 클래스, 타입, 하위 타입

```kotlin
var x: String // String 클래스의 인스턴스를 저장하는 변수
var y: String? // 같은 String 클래스 이름을 nullable 타입에도 쓸 수 있다.
```

> 클래스와 타입은 같은 의미가 아니다. 위 예제를 보면 하나의 클래스가 적어도 둘 이상의 타입을 구성할 수 있음을 보여준다.

> 제네릭 클래스는 더 복잡하다. 올바른 타입을 얻으려면 구체적인 타입 파라미터를 설정해야 한다.  
> 예를 들어 `List`는 타입이 아니라 클래스이고 `List<Int>`, `List<String>`, `List<String?>` 등은 모두 타입이다.  
> 각각의 제네릭 클래스는 무수히 많은 타입을 만들어낼 수 있다.

```kotlin
fun test(i: Int) {
    val n: Number = i // Int 가 Number 의 하위 타입이므로 컴파일된다.
    fun f(s: String) {}
    // f(i) // Int 가 String 의 하위 타입이 아니므로 컴파일되지 않는다.
}
```

> 간단한 경우 하위 타입은 하위 클래스와 근본적으로 같다. `Int` 는 `Number` 의 하위 클래스이므로 하위 타입이다.

> 하지만, nullable 타입은 하위 타입과 하위 클래스가 같지 않은 경우를 보여주는 예다.

A 는 A? 의 하위 타입이다.
Int 는 Int? 의 하위 타입이다.
Int? 는 Int 의 하위 타입이 아니다.

> `Int` 는 `Int?` 의 하위 타입이지만 `Int` 와 `Int?` 모두 같은 클래스이다.

> 앞에서 다룬 예시 `List<String>` 과 `List<Any>` 를 빗대어 설명해보면.. `List<String>` 은 `List<Any>` 의 하위 타입인가? 라는 질문이 생긴다. 결론부터 말하면 코틀린에서 두 타입은 공변적(covariant)이다. 즉, A가 B의 하위 타입이면 `List<A>` 는 `List<B>`의 하위 타입이다.  
> 자바에서는 모든 클래스가 무공변이다.

### 공변성: 하위 타입 관계를 유지

```kotlin
interface Producer<out T> { // 클래스가 T에 대해 공변적이라고 선언한다.
    fun produce(): T
}
```

> 타입 파라미터 앞에 out 키워드를 넣음으로써 코틀린에서 제네릭 클래스가 타입 파라미터에 대해 공변적임을 표현할 수 있다.

```kotlin
fun transform(t: T): T
//            {in}  {out}
```

> 클래스 타입 파라미터 T 앞에 out 키워드를 붙이면 클래스 안에서 T를 사용하는 메서드가 out 위치 에서만 T를 사용하게 허용하고 in 위치에서는 T를 사용하지 못하게 막는다.  
> 따라서 out 키워드를 통해 T의 사용법을 out 으로만 제한해서 T로 인해 생기는 타입 안전성을 보장한다.

```kotlin
// 코틀린의 List는 읽기 전용이므로 out 제한이 걸려있다.
public interface List<out E> : Collection<E> {}

// MutableList의 경우 타입 파라미터가 in, out 위치에서 모두 사용되기 때문에 공변적일 수 없다.
public interface MutableList<E> : List<E>, MutableCollection<E> {}
```

> 생성자 파라미터는 in, out 어느 쪽도 아니라는 사실에 유의해야 한다. 타입 파라미터가 out이라 해도 그타입을 여전히 생성자 파라미터 선언에 사용할 수 있다.

### 반공변성: 뒤집힌 하위 타입 관계

반공변성(contravariance)은 공변성의 반대이다.

```kotlin
interface Consumer<in T> {
    fun typeParam(element: T) {}
    // fun returnTypeParam(): T // 컴파일 에러: out 위치에서 타입 파라미터를 사용할 수 없다.
}
```

### 사용 지점 변성

```kotlin
fun <T> copyData(
    source: MutableList<out T>,
    destination: MutableList<T>
) {
    for (item in source) {
        destination.add(item)
    }
}
```

> 클래스 레벨에서 지정하는 선언 지점 변성과는 다르게 사용하려는 메서드에서 변성을 지정할 수 있다. 이것을 사용 지점 변성 이라고 부른다.

## 요약

1. 코틀린 제네릭은 자바와 아주 비슷하다.
2. 자바와 마찬가지로 제네릭 타입의 타입 인자는 컴파일 시점에만 존재한다.
3. inline 메서드의 타입 매개변수를 reified로 표시해서 실체화하면 is로 검사하거나 java 클래스를 얻을 수 있다.
4. 변성을 통해 베이스 클래스가 같고 타입 파라미터가 다른 두 제네릭 타입 사이에서 서로 어떤 영향을 받는지를 명시할 수 있다.
5. out 을 통해 공변적, in 을 통해 반공변적으로 만들 수 있다.
6. 코틀린에서는 제네릭 클래스의 공변성을 클래스 레벨(선언 지점 변성) 또는 메서드 레벨(사용 지점 변성)에서 지정할 수 있다.
7. 제네릭 클래스의 타입 인자가 어떤 타입인지 정보가 없거나 타입 인자가 어떤 타입인지 중요하지 않다면 스타 프로젝션을 사용할 수 있다.
