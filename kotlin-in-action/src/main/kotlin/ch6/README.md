# 6장 코틀린 타입 시스템

## 널 가능성

코틀린을 비롯한 최신 언어에서 null 에 대한 접근 방법은 가능한 한 이 문제를 실행 시점에서 컴파일 시점으로 옮기는 것이다.

```kotlin
fun strLenSafe(s: String?) {
    // nullable parameter
}
```

> 물음표가 없는 타입은 그 변수가 null 참조를 지정할 수 없다는 뜻이다. 따라서 모든 타입은 기본적으로 널이 될 수 없는 타입이다. 뒤에 ?가 붙어야 널이 될 수 있다.

> 실행 시점에 nullable 타입이나 not null 타입의 객체는 같다. nullable 타입은 not null 타입을 감싼 래퍼 타입이 아니다. 모든 검사는 컴파일 시점에 수행되기 때문에 코틀린 런타임에서 nullable 타입을 처리하는 데 별도의 실행시점 부가 비용이 들지 않는다.


```kotlin
fun strLenSafe(s: String?): String? = s?.uppercase()
// (if (s != null) s.uppercase() else null 과 같다.
```

> `?.` 연산자는 null 검사와 메서드 호출을 한 번의 연산으로 수행한다.

```kotlin
class Address(
    val streetAddress: String,
    val zipCode: Int,
    val city: String,
    val country: String
)

class Company(
    val name: String,
    val address: Address?
)

class Person(
    val name: String,
    val company: Company?
)

fun Person.countryName(): String {
    val country = this.company?.address?.country
    return country ?: "Unknown" // 엘비스 연산자
}

fun main() {
    val person = Person("Dmitry", null)
    println(person.countryName())
}
```

> 안전한 호출을 연쇄시킬 수 있다.

```kotlin
class Foo(
    val name: String
) {
    override fun equals(other: Any?): Boolean {
        val otherFoo = other as? Foo ?: return false
        return otherFoo.name == this.name
    }
}
```

> `as?`  를 통해 안전한 캐스트가 가능하다.

```kotlin
fun ignoreNull(s: String?) {
    val sNotNull = s!!
    println(sNotNull.length)
}
```

> not-null assertion `!!` 연산자를 통해 강제로 not null 타입으로 변경할 수 있다.
> 실제 null에 !! 를 적용하면 NPE가 발생한다.

> 아마도 !!가 약간 무례해 보인다는 사실을 눈치 챘을 것이다. !! 기호는 마치 컴파일러에게 소리를 지르는 것 같은 느낌이 든다. 사실 이는 의도한 것이다. 코틀린 설계자들은 컴파일러가 검증할 수 없는 단언을 사용하기보다는 더 나은 방법을 찾아보라는 의도를 넌지시 표현하려고 !!라는 못생긴 기호를 택했다.

```kotlin
fun sendEmailTo(email: String) {}

fun main() {
    val email: String? = "foo"
    email?.let { sendEmailTo(it) }
}
```

> let을 통해 nullable 타입의 변수를 not null 타입의 인자로 호출할 수 있다.

```kotlin
data class SomeDto(
    val name: String,
) {
    val value: String? = null // 반드시 초기화 해야한다.
    lateinit var result: String // 나중에 초기화할 수 있다.
}

fun main() {
    val dto = SomeDto("name")

    dto.result // UninitializedPropertyAccessException 예외 발생
    
    dto.result = "Lazy Initialized!"
}
```

> lateinit 을 통해 not null 타입의 값을 생성되는 시점에 바로 초기화하지 않고 초기화를 지연시킬 수 있다.

```kotlin
fun verifyUserInput(input: String?) {
    if (input.isNullOrBlank()) { // ?. safe call 을 사용하지 않아도 된다.

    }
}

fun String?.isNullOrBlank() = this == null || this.isBlank()
// null check 를 먼저 수행하므로 실제 사용시에 safe call 을 하지 않아도 된다.
```

> 확장함수를 통해 nullable 타입을 확장할 수 있다.

```kotlin
fun <T> printHashCode(t: T) {
    println(t?.hashCode()) // t 가 nullable 하므로 safe call 을 사용해야 한다.
}
```

> 코틀린에서는 함수나 클래스의 모든 타입 파라미터는 기본적으로 널이 될 수 있다.
> 타입 파라미터 T를 클래스나 함수 안에서 타입 이름으로 사용하면 이름 끝에 물음표가 없더라도 null 이 될 수 있는 타입이다.

```kotlin
fun <T : Any> printHashCodeOnlyNotNull(t: T) { // 이제 T 는 not null 타입이다.
    println(t.hashCode())
}

fun main() {
    printHashCode(null)
    // printHashCodeOnlyNotNull(null) // 컴파일되지 않는다.
}
```

> 타입 파라미터가 not null 임을 확실히 하려면 not null 타입 상한(upper bound)를 지정해야 한다.

### 플랫폼 타입

> 플랫폼 타입은 코틀린이 null 관련 정보를 알 수 없는 타입을 말한다. 그 타입을 nullable 타입으로 처리해도 되고 not null 타입으로 처리해도 된다. 이는 자바와 마찬가지로 플랫폼 타입에 대해 수행하는 모든 연산에 대한 책임은 온전히 여러분에게 있다는 뜻이다.
> 자바 타입은 코틀린에서 플랫폼 타입으로 표현된다.

- 코틀린이 왜 플랫폼 타입을 도입했는가?

> 모든 자바 타입을 nullable 타입으로 다루면 더 안전하지 않을까? 물론 그래도 되지만 모든 타입을 nullable 타입으로 다루면 not null 값에 대해서도 불필요한 null check 을 해야 한다.
> 특히 제네릭을 다룰 때 상황이 더 나빠지는데, `ArrayList<String>`를 코틀린에서 `ArrayList<String?>?` 처럼 다루면 배열의 원소에 접근할 때 마다 null check 를 수행하거나 safe call 을 사용해야 한다. 이런 식으로 처리하면 널 안전성으로 얻는 이익보다 검사에 드는 비용이 훨씬 더 커진다.

## 원시타입

> 코틀린은 실행 시점에 숫자 타입은 가능한 한 효율적인 방식으로 표현된다. 대부분의 경우 코틀린의 Int 타입은 자바 int 타입으로 컴파일 된다. 이런 컴파일이 불가능한 경우 (ex 컬렉션, 제네릭)

> null 이 될 수 있는 원시 타입: Int?, Boolean? 등은 자바 원시 타입으로 표현할 수 없다. 따라서 코틀린에서 null 이 될 수 있는 원시 타입을 사용하면 그 타입은 자바의 래퍼 타입으로 컴파일된다.

> 코틀린과 자바의 가장 큰 차이점 중 하나는 숫자를 변환하는 방식이다. 코틀린은 한 타입의 숫자를 다른 타입의 숫자로 자동 변환하지 않는다.

```kotlin
// Any: 자바의 Object

fun main() {
    val answer: Any = 42 // 42 는 참조 타입에 박싱된다.
}
```

```kotlin
// Unit: 자바의 void

fun f(): Unit {} // Unit 은 생략 가능하다.

interface Processor<T> {
    fun process(): T
}

class NoReturnProcessor : Processor<Unit> {
    override fun process() {
        // return 을 명시할 필요가 없다.
    }
}
```

> 자바의 java.lang.Void 타입을 사용하는 방법도 있지만 여전히 Void 타입에 대응할 수 있는 유일한 값인 null 을 반환하기 위해 return null 을 명시해야 한다.
> Unit의 경우 컴파일러가 묵시적으로 return Unit 을 넣어준다.


```kotlin
// Nothing: 이 함수는 결코 정상적으로 끝나지 않는다.

fun fail(message: String): Nothing = throw IllegalStateException(message)

fun foo() {
    val company = Company("company", null)
    val address = company.address ?: fail("No address")
}
```

> 코틀린에서는 반환 값이라는 개념 자체가 없는 함수가 일부 존재한다. 특별한 메시지가 들어있는 예외를 던지는 경우를 예로 들 수 있다.

## 코틀린 컬렉션

```kotlin
fun <T> copyElements(
    source: Collection<T>,
    target: MutableCollection<T>
) {
    for (item in source) {
        target.add(item)
    }
}

fun main() {
    // val source = listOf(3, 5, 7)
    // val target = mutableListOf(1)

    val source: Collection<Int> = arrayListOf(3, 5, 7)
    val target: MutableCollection<Int> = arrayListOf(1)
    copyElements(source, target)
    // 책에서는 컴파일 에러가 난다고 하지만 현재 최신 kotlin 1.5 버전에서는 정상 동작한다.
    // 참조: https://github.com/Kotlin/kotlinx.collections.immutable/blob/master/proposal.md
    println(target)
}
```

## 요약

- 코틀린은 nullable 타입을 지원해 NPE를 컴파일 시점에 감지한다.
- `?.`, `?:`, `!!`, `let` 등을 사용해 nullable 타입을 간결한 코드로 다룰 수 있다.
- 자바에서 가져온 타입은 플랫폼 타입으로 취급된다. 플랫폼 타입은 nullable, not null 으로 모두 사용할 수 있다.
- 코틀린은 원시타입에 대한 구분이 없으며 컴파일러가 알아서 적절히 변환해준다.
