# 10장  어노테이션과 리플렉션

**다루는 내용**
- 어노테이션 적용과 정의
- 리플렉션을 사용해 실행 시점에 객체 내부 관찰
- 코틀린 실전 프로젝트 예제

## 10.1 어노테이션 선언과 적용

```kotlin
fun main() {
    val mutableListOf = mutableListOf("foo")
    // deprecatedLevelErrorFun() // compile error
    deprecatedAndReplaceWithFun() // has quick fix
}

@Deprecated("deprecated!", level = DeprecationLevel.ERROR)
fun deprecatedLevelErrorFun() {}

@Deprecated("deprecated!", replaceWith = ReplaceWith("notDeprecatedFun()"))
fun deprecatedAndReplaceWithFun() {}

fun notDeprecatedFun() {}
```

> 클래스를 어노테이션 파라미터로 지정할 때는 `@MyAnnotation(MyClass::class)` 와 같이 `::class` 를 붙여야 한다.  
> 다른 어노테이션을 파라미터로 지정할 때는 이름 앞에 `@` 를 생략해야 한다. 위 예제에서의 ReplaceWith 은 어노테이션이다.

- 간단한 어노테이션 선언 & 사용 예제

```kotlin
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class MyAnnotation(
    val clazz: KClass<out Any>
)

class MyClass

@MyAnnotation(MyClass::class)
fun myFun() {}
```

> `@Retention`, `@Target` 과 같이 어노테이션 클래스에 사용되는 어노테이션을 메타어노테이션 이라고 부른다.

> `@Retention` 어노테이션  
> 자바 `@Retention` 어노테이션을 본 적이 있을 것이다. `@Retention` 은 정의 중인 어노테이션 클래스를 소스 수준에서만 유지할지, `.class` 파일에 저장할지, 런타임 시점에 리플렉션을 사용해 접근할 수 있게 할지를 지정하는 메타어노테이션이다. 자바 컴파일러는 기본적으로 어노테이션을 `.class` 파일에는 저장하지만 런타임에는 사용할 수 없게 한다.  
> 하지만 대부분의 어노테이션은 런타임에도 사용할 수 있어야 하므로 코틀린에서는 기본적으로 `@Retention` 을 런타임으로 지정한다.

## 10.2 코틀린 리플렉션

안드로이드와 같이 런타임 라이브러리 크기가 문제가 되는 플랫폼을 위해 코틀린 리플렉션 API는 kotlin-reflect.jar 라는 별도의 .jar 파일에 담겨 제공되며, 새 프로젝트를 생성할 때 리플렉션 패키지 .jar 파일에 대한 의존관계가 자동으로 추가되는 일은 없다.

```kotlin
// 의존성 추가
dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")
}
```

```kotlin
import kotlin.reflect.full.*

class Person(
    val name: String,
    val age: Int
)

fun main() {
    val person = Person("Alice", 29)
    val kClass = person.javaClass.kotlin

    kClass.members.forEach { println(it.name) } // age, name, equals, hashCode, toString
    kClass.memberProperties.forEach { println(it.name) } // age, name
}
```

> 클래스의 모든 멤버의 목록이 `KCallable` 인스턴스의 컬렉션인 것을 확인할 수 있다.  
> `KCallable` 은 함수와 프로퍼티를 아우르는 공통 상위 인터페이스다. 그 안에 `call()` 메서드가 들어있으며, 이를 통해 게터를 호출할 수 있다.

```kotlin
fun foo(x: Int) = println(x)

val kFunction = ::foo
kFunction.call(50) // call(vararg args: Any?)
kFunction.invoke(50) // invoke(x: Int)
```

> 메서드의 참조는 `KFunctionN` 클래스로 구성된다.  
> 파라미터, 리턴 값에 따라 N의 값이 다른데, N과 파라미터 개수가 같은 invoke를 추가로 포함한다. 예를 들어 `KFunction2<P1, P2, R>` 은 두가지 파라미터와 리턴 타입으로 구성되는 것을 알 수 있다.  
> 이러한 `KFunctionN` 타입은 N과 파라미터 개수가 같은 `invoke()` 메서드를 추가로 포함한다. 따라서 `call()`  에서 파라미터 개수를 제대로 입력하지 않아서 생기는 문제를 컴파일 단계에서 해소할 수 있다.  
> 특별한 점은, 이런 함수 타입들은 컴파일러가 생성한 합성 타입(synthetic compiler-generated type)이다. 따라서 kotlin.reflect 패키지에서 이런 타입의 정의는 찾을 수 없다. 자바와 다르게 코틀린에서는 컴파일러가 생성한 합성 타입을 사용하기 때문에 원하는 수만큼 많은 파라미터를 갖는 함수에 대한 인터페이스를 사용할 수 있다. 합성 타입을 사용하기 때문에 코틀린은 kotlin.runtime.jar 의 크기를 줄일 수 있고, 함수 파라미터 개수에 대한 인위적인 제약을 피할 수 있다.

```kotlin
val kMutableProperty = ::counter
kMutableProperty.setter.call(21)
println(kMutableProperty.get()) // 21
```

> 프로퍼티에 대한 참조또한 동일하게 리플렉션을 사용할 수 있다. `KProperty<V>, KMutableProperty<V>`

```kotlin
fun test() {
    var rr = 1
    // ::rr // Unsupported [References to variables aren't supported yet]
}
```

> 최상위 수준이나 클래스 안에 정의된 프로퍼티만 리플렉션으로 접근할 수 있고 메서드의 로컬 변수에는 접근할 수 없다. (아직 지원하지 않음)

## 요약

1. 코틀린에서 어노테이션을 적용할 때 사용하는 문법은 자바와 거의 같다.
2. 코틀린에서는 자바보다 더 넓은 대상에 어노테이션을 적용할 수 있다. ex) File, Expression
3. 클래스를 컴파일 시점에 알고 있다면 KClass 인스턴스를 얻기 위해 `ClassName::class` 를 사용한다. 하지만 실행 시점에 obj 변수에 담긴 객체로부터 KClass 인스턴스를 얻기 위해서는 `obj.javaClass.kotlin` 을 사용해야 한다.
4. `KCallable.callBy` 메서드를 사용하면 메서드를 호출하면서 디폴트 파라미터 값을 사용할 수 있다
