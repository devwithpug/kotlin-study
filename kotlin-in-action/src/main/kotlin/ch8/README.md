# 8장 고차 함수: 파라미터와 반환 값으로 람다 사용

**목차**
- 함수 타입
- 고차 함수와 코드를 구조화할 때 고차 함수를 사용하는 방법
- 인라인 함수
- 비로컬 return과 레이블
- 무명 함수

고차함수(high order function)란?   
-> 람다(함수)를 인자로 받거나 반환하는 함수!

> 8장에서는...  
> 고차 함수로 코드를 더 간결하게 다듬고 코드 중복을 없애고 더 나은 추상화를 구축하는 방법을 알아본다.  
> 람다를 사용함에 따라 발생할 수 있는 성능상 부가 비용을 없애고 람다 안에서 더 유연하게 흐름을 제어할 수 있는 코틀린의 특성인 인라인 함수에 대해 설명한다.

## 고차 함수 정의

고차 함수는 다른 함수를 인자로 받거나 함수를 반환하는 함수

```kotlin
list.filter { x > 0 }
```

> ex) 표준 라이브러리 함수인 .filter() 는 술어(predicate) 함수를 인자로 받으므로 고차 함수이다.

### 함수 타입

고차 함수를 정의하려면 **함수 타입**에 대해 먼저 알아야 한다.

```kotlin
val value = 5 // Int 타입 변수
val sum = { x: Int, y: Int -> x + y } // 함수 타입 변수
val action = { it: Any -> println(it) } // 리턴타입이 없는 함수 타입 변수
val noParam = { println("no parameter") } // 파라미터와 리턴타입이 없는 함수 타입 변수
```

> 코틀린의 타입 추론으로 인해 변수 타입을 지정하지 않아도 컴파일러는 sum과 action이 함수 타입임을 추론할 수 있다.

```kotlin
val value: Int = 5
val sum: (Int, Int) -> Int = { x: Int, y: Int -> x + y }
val action: (Any) -> Unit = { it: Any -> println(it) }
val noParam: () -> Unit = { println("no parameter") }
```

> 변수의 타입을 위와 같이 표현할 수 있다.

```kotlin
val sum: (Int, Int) -> Int = { x, y -> x + y } // 람다 식에서 x, y 의 타입을 생략
val action: (Any) -> Unit = { println(it) } // 람다 식에서 하나의 파라미터(it) 선언 자체를 생략
```

> 변수 타입을 함수 타입으로 지정하면 함수 타입에 있는 파라미터로부터 람다의 파라미터 타입을 유추할 수 있다.  
> 따라서 람다 식 안에서 파라미터 타입을 생략할 수 있다.

```kotlin
val paramCanBeNull: (Any?) -> Unit = { println(it) } // nullable 파라미터 타입
val canReturnNull: () -> Int? = { null } // nullable 리턴 타입
var funOrNull: ((Int, Int) -> Int)? = null // nullable 함수 타입
```

> 다른 함수와 마찬가지로 함수 타입에서도 파라미터와 반환 타입 모두 nullable 타입으로 지정할 수 있다.  
> 함수 타입 자체를 nullable 하게 설정할 수도 있다.

### 고차 함수 정의 (파라미터)

```kotlin
fun twoAndThree(operation: (Int, Int) -> Int) {
    val result = operation(2, 3)
    println("The result is $result")
}

fun main() {
    twoAndThree { x, y -> x + y } // The result is 5
    twoAndThree { x, y -> x * y } // The result is 6
    twoAndThree { _, _ -> throw IllegalStateException() }
}
```

* String.filter() 똑같이 구현하기

```kotlin
fun String.myFilter(predicate: (Char) -> Boolean): String {
    val sb = StringBuilder()
    for (index in indices) {
        val element = get(index)
        if (predicate(element)) {
            sb.append(element)
        }
    }
    return sb.toString()
}

"STRing".myFilter { it in 'A'..'Z' } // STR
```


* apply 적용 (buildString)

```kotlin
fun String.myFilter(predicate: (Char) -> Boolean) = buildString {
    for (index in indices) {
        val element = get(index)
        if (predicate(element)) {
            append(element)
        }
    }
}

"STRing".myFilter { it in 'A'..'Z' } // 아무것도 출력하지 않음
```


![image](https://user-images.githubusercontent.com/69145799/156915887-534aa0f8-d5a7-4077-8a16-a5b8608519ea.png)

> String.myFilter 의 String 글씨 색이 회색으로 되어있다.

![image](https://user-images.githubusercontent.com/69145799/156915925-aa41702c-f242-4aeb-8ef7-8ba06e87bbe4.png)

> Receiver parameter is never used (파라미터로 넘어온 수신 객체를 사용하지 않고 있다는 경고.. 대체 왜?)

![image](https://user-images.githubusercontent.com/69145799/156916028-6a2a3f9e-e805-440a-8242-e0f760f70701.png)

> "STRing" 이라는 문자열을 수신 객체로 받았을 텐데 indices 의 범위는 0..-1 이다???

![image](https://user-images.githubusercontent.com/69145799/156916063-f1a43654-b301-4cfa-bf14-7c5ab44b3b18.png)

> 수신 객체를 직접 명시(this@myFilter)해주면 indices 범위가 0..5 로 제대로 나온다.  
> 여기서 @myFilter 는 레이블이라고 부르는데, 자세한건 뒤에서 설명한다.

![image](https://user-images.githubusercontent.com/69145799/156916111-6917f4a3-8981-46c8-9311-da1a5479febc.png)

> this 의 값을 확인해보면.. StringBuilder 객체이다.

**문제는 String.myFilter 에서 두가지 수신 객체가 넘어온 다는 점이다.**

![image](https://user-images.githubusercontent.com/69145799/156916185-d4b934a7-504f-43c3-b44d-86a271b9a040.png)

> `this@myFilter`, `this@stringBuilder`

따라서 myFilter 메서드가 정상적으로 작동하려면 myFilter 의 수신 객체를 명시해야 한다.

```kotlin
fun String.myFilter(predicate: (Char) -> Boolean) = buildString {
    for (index in this@myFilter.indices) {
        val element = this@myFilter[index]
        if (predicate(element)) {
            append(element)
        }
    }
}

// 또는 with 을 사용해서 첫 번째 수신 객체를 myFilter로 변경할 수 있다.
fun String.myFilter(predicate: (Char) -> Boolean) = buildString {
    with(this@myFilter) {
        for (index in indices) {
            val element = get(index)
            if (predicate(element)) {
                this@buildString.append(element)
            }
        }
    }
}
```

> 코드를 간소화 하려다보니, 내부 로직의 가독성이 조금 나빠졌다..

[참고: A Look Into the Future by Roman Elizarov - Multiple receivers](https://www.youtube.com/watch?v=0FF19HJDqMo&t=795s)


- 자바에서 사용하던 joinToString 메서드
```kotlin
fun <T> Collection<T>.joinToString(
    separator: String = ", ",
    prefix: String = "",
    postfix: String = ""
): String {
    val result = StringBuilder(prefix)
    for ((index, element) in this.withIndex()) {
        if (index > 0) {
            result.append(separator)
        }
        result.append(element)
    }
    result.append(postfix)
    return result.toString()
}

fun main() {
    val items = listOf(
        Item(1, "itemA"),
        Item(2, "itemB")
    )
    val resultString = items.joinToString(prefix = "[", postfix = "]")
    println(resultString) // [Item(id=1, name=itemA), Item(id=2, name=itemB)]
}
```

> 이 구현은 유연하지만 핵심 요소 하나를 제어할 수 없다는 단점이 있다. 그 핵심 요소는 바로 컬렉션의 각 원소를 문자열로 변환하는 방법이다.  
> 이 함수는 항상 객체를 toString 메서드를 통해 문자열로 바꾼다. 물론 toString도 충분한 경우도 많지만 그렇지 않을 때도 있다.

```kotlin
fun <T> Collection<T>.joinToString(
    separator: String = ", ",
    prefix: String = "",
    postfix: String = "",
    transform: (T) -> String = { it.toString() }
): String {
    val result = StringBuilder(prefix)
    for ((index, element) in this.withIndex()) {
        if (index > 0) {
            result.append(separator)
        }
        result.append(transform(element)) // transform 함수 적용
    }
    result.append(postfix)
    return result.toString()
}

fun main() {
    val items = listOf(
        Item(1, "itemA"),
        Item(2, "itemB")
    )
    val resultString2 = items.joinToString(prefix = "[", postfix = "]") { it.name }
    println(resultString2) // [itemA, itemB]
}
```

> 다음과 같이 람다를 통해 원소를 문자열로 변경할 방법을 전달할 수 있기 때문에 코드를 간소화할 수 있다.

### 고차 함수 정의 (리턴)

함수가 함수를 반환할 필요가 있는 경우보다는 함수가 함수를 파라미터로 받아야 할 필요가 있는 경우가 훨씬 더 많다.(위의 예 처럼)  
하지만, 함수를 반환하는 함수도 여전히 유용하다. 예를 들어 사용자가 선택한 배송 수단에 따라 배송비를 계산하는 방법이 달라질 수 있다.

```kotlin
enum class Delivery { STANDARD, ROCKET }

class Order(val itemCount: Int)

fun getShippingCostCalculator(delivery: Delivery): (Order) -> Int { // 함수를 리턴하는 함수 선언
    if (delivery == Delivery.ROCKET) {
        return { order -> 5000 * order.itemCount }
    }
    return { order -> 3000 * order.itemCount }
}

fun main() {
    val rocketCalculator = getShippingCostCalculator(Delivery.ROCKET)
    val standardCalculator = getShippingCostCalculator(Delivery.STANDARD)
    val myOrder = Order(2)

    println("일반 배송비는 ${standardCalculator(myOrder)} 입니다.")
    println("로켓 배송비는 ${rocketCalculator(myOrder)} 입니다.")
		// 일반 배송비는 6000 입니다.
    // 로켓 배송비는 10000 입니다.
}
```

### 고차 함수(람다)를 활용한 중복 제거


- 운영체제 별 평균 방문 시간

```kotlin
data class SiteVisit(
    val path: String,
    val duration: Double,
    val os: OS
)

enum class OS { WINDOWS, LINUX, MAC, IOS, ANDROID }

val log = listOf(
    SiteVisit("/", 34.0, OS.WINDOWS),
    SiteVisit("/", 22.0, OS.MAC),
    SiteVisit("/login", 12.0, OS.WINDOWS),
    SiteVisit("/signup", 8.0, OS.IOS),
    SiteVisit("/", 16.3, OS.ANDROID),
)

fun main() {
    val averageWindowsDuration = log
        .filter { it.os == OS.WINDOWS }
        .map { it.duration }
        .average()

    val averageMacDuration = log // 중복 코드!
        .filter { it.os == OS.MAC }
        .map { it.duration }
        .average()

    println(averageWindowsDuration) // 23.0
    println(averageMacDuration) // 22.0
}
```

> average 메서드를 통해 평균 방문 시간을 쉽게 구할 수 있었다.  
> 하지만, 운영체제 마다 같은 코드를 반복해야 하기 때문에 중복 코드가 발생했다.  
> 이러한 중복을 피하기 위해 OS를 파라미터로 뽑아 낼 수 있다.

```kotlin
fun List<SiteVisit>.averageDurationFor(os: OS) =
    filter { it.os == os }.map { it.duration }.average()
	
fun main() {
    println(log.averageDurationFor(OS.WINDOWS)) // 23.0
    println(log.averageDurationFor(OS.MAC)) // 22.0
} 
```

> OS 필터를 파라미터로 뽑아내서 코드 중복을 제거할 수 있었다.  
> 그렇다면, 더 복잡한 질의를 사용해 방문 기록을 분석하고 싶은 경우는 어떻게 하면 좋을까??

- filter의 predicate 변수를 메서드의 파라미터로 받기

```kotlin
fun List<SiteVisit>.averageDurationFor(predicate: (SiteVisit) -> Boolean) =
    filter(predicate).map { it.duration }.average()

fun main() {
    println(log.averageDurationFor { it.os == OS.IOS && it.path == "/signup" }) // 8.0
} 
```

## 인라인 함수: 람다의 부가 비용 없애기

지금까지 고차 함수를 만드는 방법을 설명했다. 이제 고차 함수의 성능에 대해 이야기 해보자.  
고차함수를 여기저기 활용하면 전통적인 루프와 조건문을 사용할 때보다 더 느려지지 않을까?  
다음 절에서는 람다를 활용한다고 코드가 항상 더 느려지지는 않는다는 사실과 inline 키워드를 통해 어떻게 람다의 성능을 개선하는지 보여준다.

- 코틀린 람다 코드의 컴파일 예

> 1. 코틀린이 보통 람다를 무명클래스로 컴파일 하지만 그렇다고 람다 식을 사용할 때마다 새로운 클래스가 만들어지지는 않는다.  
> 2. 람다가 변수를 포획하면 람다가 생성되는 시점마다 새로운 무명 클래스 객체가 생성된다.  

> 따라서 람다를 사용하는 구현은 똑같은 작업을 수행하는 일반 함수를 사용한 구현보다 덜 효율적이다.  

> 람다로 구현된 코드는 컴파일 단계에서 반복되는 코드로 변환되기 떄문인데.. 그렇다면 반복되는 코드를 별도의 라이브러리 함수로 빼내되 컴파일러가 자바의 일반 명령문만큼 효율적인 코드를 생성하게 만들 수는 없을까?  
> 사실 코틀린 컴파일러에서는 그런 일이 가능하다.

### inline 변경자

inline 변경자를 어떤 함수에 붙이면 컴파일러는 그 함수를 호출하는 모든 문장을 함수 본문에 해당하는 바이트코드로 바꿔치기 해준다.

> 다른 말로 하면 함수를 호출하는 코드를 함수를 호출하는 바이트코드 대신에 함수 본문을 번역한 바이트 코드로 컴파일한다는 뜻이다.


- inline 메서드와 일반 메서드에 대해 간단한 비교

```kotlin
fun oneAndTwo(operation: (Int, Int) -> Int) {
    val result = operation(1, 2)
    println("The result is $result")
}

inline fun inlineOneAndTwo(operation: (Int, Int) -> Int) {
    val result = operation(1, 2)
    println("The result is $result")
}

fun main() {
    oneAndTwo { x, y -> x + y }
    oneAndTwo { x, y -> x - y }
    oneAndTwo { x, y -> x * y }
    oneAndTwo { x, y -> x % y }
    inlineOneAndTwo { x, y -> x + y }
    inlineOneAndTwo { x, y -> x - y }
    inlineOneAndTwo { x, y -> x * y }
    inlineOneAndTwo { x, y -> x % y }
}
```

## 변환된 자바 코드 비교

- 1. inline 이 아닌 일반 메서드 `oneAndTwo()`
```kotlin
fun oneAndTwo(operation: (Int, Int) -> Int) {
    val result = operation(1, 2)
    println("The result is $result")
}

fun main() {
    oneAndTwo { x, y -> x + y }
    oneAndTwo { x, y -> x - y }
    oneAndTwo { x, y -> x * y }
    oneAndTwo { x, y -> x % y }
}
```

- 자바 디컴파일 코드
  ![image](https://user-images.githubusercontent.com/69145799/156922264-151450b9-9b2d-4f30-9587-5484d7014330.png)

- 바이트 코드
  ![image](https://user-images.githubusercontent.com/69145799/156922498-e0bcb351-f25c-44f7-8b90-5d5bb3796247.png)

> 람다 내부 식에 대한 정보가 사라져있다.  
> 개인적인 생각으로는 컴파일단계에서 각각의 메서드에 +, -, *, % 연산에 대한 클래스? 객체? 메서드? 를 생성해주는 것 같다.(정확한 이유를 모르겠다..)

> 아무튼, 각각의 코틀린 코드에서의 메서드 호출이 자바 코드에서도 동일하게 표현되어 있다.

- 2. inline 메서드 `inlineOneAndTwo()`
```kotlin
inline fun inlineOneAndTwo(operation: (Int, Int) -> Int) {
    val result = operation(1, 2)
    println("The result is $result")
}

fun main() {
    inlineOneAndTwo { x, y -> x + y }
    inlineOneAndTwo { x, y -> x - y }
    inlineOneAndTwo { x, y -> x * y }
    inlineOneAndTwo { x, y -> x % y }
}
```

![image](https://user-images.githubusercontent.com/69145799/156922362-7a4e88c2-810a-4396-87d2-fb1a252065ac.png)

> 정말 말그대로 inline 되었다. (메인 함수 내에 모든 로직이 표현되어 있다)

## 바이트 코드 비교

- inline이 아닌 일반 메서드 : 392 line  
- inline 메서드 : 292 line

> 람다 식으로 이루어진 메서드를 단 4개 호출했는데 바이트 코드는 100 line 이나 차이가 생겼다!

## 인라인 함수의 한계

인라이닝을 하는 방식으로 인해 람다를 사용하는 모든 함수를 인라이닝할 수는 없다.

```kotlin
inline fun inlineOneAndTwo(operation: (Int, Int) -> Int) {
    val result = operation(1, 2)
    topLevelValue = operation
    println("The result is $result")
}

var topLevelValue: (Int, Int) -> Int = { x, y -> x + y }
```

![image](https://user-images.githubusercontent.com/69145799/156923220-d77ff7ce-5b94-4583-af38-3307e2b76f65.png)

> 파라미터로 받은 람다를 다른 변수에 저장하고 나중에 그 변수를 사용한다면 람다를 표현하는 객체가 어딘가는 존재해야 하기 때문에 람다를 인라이닝할 수 없다.


- `noinline` 을 통해 인라이닝을 금지
```kotlin
inline fun foo(
    inlined: () -> Unit,
    noinline notInlined: () -> Unit // 인라이닝을 금지
) { }
```

> noinline 을 사용하면 둘 이상의 람다를 인자로 받는 함수에서 어떤 람다에 너무 많은 코드가 들어가거나 어떤 람다에 인라이닝을 하면 안되는 코드가 들어가는 경우 그런 람다를 인라이닝 하지 않도록 할 수 있다.

### 컬렉션 연산 인라이닝

코틀린 표준 라이브러리의 컬렉션 함수는 대부분 람다를 인자로 받는다. (filter, map, groupBy 등등..)  
이런 연산에도 inline이 적용된다면 성능을 개선할 수 있지 않을까?

> 결론부터 말하면 람다를 파라미터로 받는 코틀린 표준 라이브러리의 컬렉션 함수는 모두 inline으로 구현되어 있다.

![image](https://user-images.githubusercontent.com/69145799/156923589-9b86e6f7-158d-4a6f-b094-b97373651130.png)

```kotlin
someList
	.filter {...}
	.map {...}
```

> 컬렉션 함수를 적절히 체이닝하여 사용한다면 모두 인라인되어 처리된다.  
> 하지만, 이 코드는 각각의 컬렉션 연산마다 리스트를 걸러낸 결과를 저장하는 중간 리스트를 만든다.  

> 처리할 원소가 많아지면 중간 리스트를 생성하는 비용이 걱정할만큼 커지기 때문에 앞 장에서는 asSequence() 메서드를 통해 시퀀스로 변환하는 방법을 소개했다.  
> 하지만 시퀀스는 (람다를 저장해야 하므로) 람다를 인라인 하지 않는다.(못한다) **위 인라인 함수의 한계 참고** 따라서 지연 계산을 통해 성능을 향상시키려는 이유로 모든 컬렉션 연산에 asSequence를 붙여서는 안 된다. 시퀀스 연산에서는 람다가 인라이닝되지 않기 때문에 크기가 작은 컬렉션은 오히려 일반 컬렉션 연산이 더 성능이 나을 수도 있다. 시퀀스를 통해 성능을 향상시킬 수 있는 경우는 컬렉션 크기가 큰 경우뿐이다.  

### 함수를 인라인으로 선언해야 하는 경우

위 내용만 본다면 코드를 더 빠르게 만들기 위해 코드 여기저기에서 `inline`을 사용해야 할 것만 같다. 하지만 이는 좋은 생각이 아니다!  
아래 경우들을 모두 따져보고, 주의 깊게 성능을 측정하고 조사해본뒤에 `inline`을 적용하는 것이 적절하다.  

1. 일반 함수 호출의 경우 JVM은 이미 강력하게 인라이닝을 지원한다 (JIT)
2. 람다를 인자로 받는 함수를 인라이닝하면 이익이 더 많다. (위에서 본 바이트코드 길이 차이)
3. 인라이닝을 사용하면 일반 람다에서는 사용할 수 없는 몇 가지 기능을 사용할 수 있다. (뒤에서 다루는 내용)
4. `inline` 변경자를 붙일 때는 코드 크기에 주의를 기울여야 한다. (코틀린 컬렉션의 `inline` 함수들은 모두 크기가 아주 작다!)

## 고차 함수 안에서 흐름 제어

- 자바 try-with-resource

```java
static String readFirstLineFromFile(String path) throws IOException {
	try (BufferedReader br = new BufferedReader(new FileReader(path))) {
		return br.readLine();
	}
}
```

코틀린에서는 함수 타입의 값을 파라미터로 받는 함수(고차함수)를 통해 아주 매끄럽게 이를 처리할 수 있으므로 이런 기능을 자바의 try-with-resource 처럼 언어 구성 요소로 제공하지 않는다.  
대신 같은 기능을 제공하는 `use` 라는 함수가 코틀린 표준 라이브러리 안에 들어있다.

- 위의 자바 예제 코드를 코틀린으로 표현한 코드

```kotlin
fun readFirstLineFromFile(path: String): String {
	BufferedReader(FileReader(path)).use { br -> 
		return br.readLine()
	}
}
```

> use 함수는 closeable 자원에 대한 확장 함수며, 람다를 인자로 받는다.  
> 자바의 try-with-resource와 동일하게 동작하며, 인라인 함수이기 때문에 성능에 영향이 없다.  

람다의 본문 안에서 사용한 return은 **넌로컬 return** 이다. 이 return 문은 람다가 아니라 readFirstLineFromFile 함수를 끝내면서 값을 반환한다.

- 넌로컬 return 예제

```kotlin
fun lookForAlice(people: List<Person>) {
    for (person in people) {
        if (person.name == "Alice") {
            println("Found!")
            return // lookForAlice() 에 대해 return
        }
    }
    println("Alice is not found")
}

fun lookForEachAlice(people: List<Person>) {
    people.forEach {
        if (it.name == "Alice") {
            println("Found!")
            return // lookForEachAlice() 에 대해 return
        }
    }
    println("Alice is not found")
}

fun main() {
    lookForAlice(people) // Found!
    lookForEachAlice(people) // Found!
}
```

> 자신을 둘러싸고 있는 블록보다 더 바깥에 있는 다른 블록을 반환하게 만드는 return문을 넌로컬 return 이라 부른다.  
> (당연한거 아닌가??)  
> 코틀린에서는 언어가 제공하는 기본 구성 요소가 아니라 람다를 받는 함수로 for 이나 synchronized 와 같은 기능을 구현하게 되는데 그런 함수 안에서 쓰이는 return이 자바의 return과 같은 의미를 갖도록 허용한 것이 넌로컬 return  이라고 하는 것 같다.

- 람다 식에서 로컬 return 사용

```kotlin
fun lookForAliceWithLocalReturn(people: List<Person>) {
    people.forEach {
        if (it.name == "Alice") {
            println("Found!")
            return@forEach // 함수 이름을 레이블로 명시함
        }
    }
    println("Alice is not found")
}

// Found!
// Alice is not found
```

- 임의의 레이블을 선언하기

```kotlin
fun lookForAliceWithLocalReturn(people: List<Person>) {
    people.forEach foo@{ // foo 레이블 선언
        if (it.name == "Alice") {
            println("Found!")
            return@foo // foo 레이블으로부터 리턴
        }
    }
    println("Alice is not found")
}
```

> 앞에서 this 식에 레이블을 사용하는 것을 본 것처럼 수신 객체 지정 람다의 본문에서는 this 참조 & 레이블을 사용해 묵시적인 컨텍스트 객체를 가리킬 수 있다.

- this@ 에 대한 추가적인 예제
```kotlin
println(StringBuilder().apply sb@{
	listOf(1, 2, 3).apply {
		this@sb.append(this.toString())
		// this@sb -> StringBuilder
		// this -> List<Int>
	}
})
```

하지만 넌로컬 return 문은 장황하고, 람다 안에 여러 위치에 return 식이 들어가야 하는 경우 사용하기 불편하다.  
코틀린은 코드 블록을 여기저기 전달하기 위한 다른 해법을 제공하며, 그 해법을 사용하면 넌로컬 return 문을 여럿 사용해야 하는 코드 블록을 쉽게 작성할 수 있다. 바로 익명(무명) 함수가 그 해법이다.

### 익명 함수

```kotlin
fun lookForAliceWithAnonymousFunction(people: List<Person>) {
    people.forEach(fun (person) { // 익명 함수 선언
        if (person.name == "Alice") {
            println("Found!")
            return // 익명 함수 내부에서의 return
        }
        println("${person.name} is not Alice")
    })
}

// Found!
// Bob is not Alice
```

> 익명 함수는 일반 함수와 비슷해 보이지만 함수 이름이나 파라미터 타입을 생략할 수 있다는 점이 특징이다.

- 코틀린 컬렉션의 filter 를 익명 함수로 사용하는 예제
```kotlin
people.filter(fun(person): Boolean {
	return person.age < 30
})

people.filter(fun(person) = person.age < 30)
```

- 위에서 다루었던 `String.filter()` 메서드를 익명 함수로 개선하기
```kotlin
fun String.myFilterWithAnonymousFunction(predicate: (Char) -> Boolean) = buildString(fun(sb) {
    for (index in indices) {
        val element = get(index)
        if (predicate(element)) {
            sb.append(element)
        }
    }
})

println("AnonymousFunction".myFilterWithAnonymousFunction { it in 'A'..'Z' }) // AF
```

## 요약

- 함수 타입 `ex: () -> Unit` 을 사용해 함수에 대한 참조를 담는 변수나 파라미터나 리턴 값을 만들 수 있다.
- 고차 함수는 다른 함수를 인자로 받거나 함수를 반환한다.
- 인라인 함수를 사용하면 성능적 부가 비용이 들지 않는다.
- 인라인 함수에서는 람다 안에 있는 return 문이 바깥쪽 함수를 반환 시키는 넌로컬 return을 사용할 수 있다.
- 익명 함수는 람다 식을 대신할 수 있으며 로컬 return 이기때문에 본문 여러곳에서 return 해야 하는 코드 블록을 만들어야 한다면 람다 대신 익명함수로 간단하게 표현이 가능하다.
