# 5장 람다로 프로그래밍

## 문법

### 코틀린 람다 식의 문법

```kotlin
{ x: Int, y: Int -> x + y }
 |  파라미터      |  | 본문 |
```

> 자바와 동일한 구조이지만 괄호가 아닌, 중괄호로 둘러싸여 있다.

### 자바와 다른 점

```kotlin
val sum = { x: Int, y: Int -> x + y }
println(sum(1, 2)) // 3
```

> 코틀린에서는 함수도 일급 클래스이기 때문에 람다가 저장된 변수를 일반 함수와 마찬가지로 다룰 수 있다!

```kotlin
fun main() {
    { println("람다를 직접 호출할 수 있다.") }()
		run { println("블록으로 둘러 쌀 필요가 있다면 run을 사용하자.") }
}
```

> 위와 같은 구문도 정상적으로 작동하지만, 읽기 어렵고 그다지 쓸모도 없다. 굳이 람다를 만들자마자 호출하느니 람다 본문을 직접 실행하는 편이 낫다.
> 이렇게 코드의 일부분을 블록으로 둘러싸 실행할 필요가 있다면 run을 사용한다.

```kotlin
fun printProblemCounts(responses: Collection<String>) {
    var clientErrors = 0
    var serverErrors = 0
    responses.forEach {
        if (it.startsWith("4")) {
            clientErrors++
        }
        if (it.startsWith("5")) {
            serverErrors++
        }
    }
    println("$clientErrors client errors, $serverErrors server errors")
}

printProblemCounts(listOf("200", "400", "404", "500"))
// 2 client errors, 1 server errors
```

> 코틀린 람다 안에서는 파이널 변수가 아닌 변수에 접근할 수 있다.
> 위 처럼 람다 안에서 사용하는 외부 변수를 '람다가 포획한 변수'라고 부른다.
> 코틀린에서 이러한 것이 가능한 이유는 포획을 위한 래퍼 클래스(Ref)가 존재하기 때문이다. 자세한 내용은 209p 확인

```kotlin
val getAge = Person::age
```

```java
Function<Person, Integer> getAge = Person::getAge;
```

> 자바와 동일하게 멤버 참조가 가능하다.

```kotlin
fun main() {
    fun foo() = println("foo!")
    run(::foo) // 최상위 멤버 참조
}
```

> 최상위에 선언된 함수나 프로퍼티를 참조할 수도 있다.

```kotlin
fun sendEmail() = println("Send email")
val foo = ::sendEmail

println("sendEmail 실행 전!")

run(foo)

// sendEmail() 실행 전
// Send email
```

> 멤버 참조를 foo 에 저장한 후, run() 메서드에 멤버 참조를 넘김으로써  sendEmail() 메서드가 실행된다.
> 추가적으로, 확장 함수 또한 멤버 참조가 가능하다.

```kotlin
val createPerson = ::Person // 생성자 참조
val p = createPerson("Alice", 29)
```

> 특이한 점은, 클래스의 생성자도 메서드이기 때문에 생성자 참조또한 사용할 수 있다. 따라서 클래스 생성 작업을 연기하거나 저장해둘 수 있다.


## 함수형 스타일로 컬렉션 다루기

> 코틀린에서는 함수형 언어에서 전통적으로 람다를 많이 활용해 온 컬렉션을 예로 들 수 있다.
> 사람들로 이루어진 리스트가 있고 그중에 가장 연장자를 찾고 싶을 때 람다를 사용해본 경험이 없는 개발자는 아래와 같이 루프를 통해 구현할 것이다.

```kotlin
data class Person(
    val name: String,
    val age: Int
)

fun findTheOldest(people: List<Person>): Person? {
    var maxAge = 0
    var theOldest: Person? = null
    for (person in people) {
        if (person.age > maxAge) {
            maxAge = person.age
            theOldest = person
        }
    }
    return theOldest
}

fun main() {
    val people = listOf(Person("Alice", 29), Person("Bob", 31))
    val theOldest = findTheOldest(people)
    println("the oldest person : $theOldest")
    // the oldest person : Person(name=Bob, age=31)
}
```

> 간단한 구현이기 때문에 어렵지 않지만, 상당히 많은 코드를 작성해야 하기 때문에 실수를 하기 쉽다. 특히나 비교연산자(>) 를 잘못 사용하면 최솟값을 찾게 된다.

> 코틀린에서는 더 좋은 방법으로, 컬렉션에서 여러 라이브러리 함수를 지원한다.

```kotlin
fun main() {
    val people = listOf(Person("Alice", 29), Person("Bob", 31))
    val theOldestWithLambda = people.maxByOrNull { it.age }
		
		println("the oldest person : $theOldestWithLambda")
		// the oldest person : Person(name=Bob, age=31)
}
```

```java
Optional<Person> max = people.stream().max(Person::getAge);
```

> 자바에서는 컬렉션에서 max와 같은 함수를 지원하지 않았으므로 stream 을 통해서만 동일한 표현이 가능했다.
> 자바에서 사용하는 List 클래스와 동일하지만 코틀린의 확장 함수를 통해 추가적인 함수들을 지원한 것이다.

```kotlin
people.maxByOrNull({ p: Person -> p.age })
```

> 이전 예제에서 코틀린이 코드를 줄여 쓸 수 있게 제공했던 기능을 제거하고 정식으로 람다를 작성하면 다음과 같다.
> 중괄호 안에 람다 식이 있다. 람다 식은 Person 타입의 값을 인자로 받아서 인자의 age 값을 리턴한다. 이러한 람다 식을 maxByOrNull 함수로 넘긴다.
> 하지만 이 코드는 번잡하다. 구분자가 너무 많아 가독성이 떨어지며, 컴파일러가 문맥으로부터 유추할 수 있는 인자 타입을 굳이 적을 필요 또한 없다. 마지막으로 인자가 단 하나 뿐인 경우 굳이 인자에 이름을 붙이지 않아도 된다.

```kotlin
people.maxByOrNull { p: Person -> p.age } // 함수의 맨 마지막 인자가 람다 식이므로 괄호 밖으로 빼낼 수 있다.
people.maxByOrNull { p -> p.age } // 컴파일러가 문맥으로부터 유추할 수 있는 인자 타입을 생략할 수 있다.
people.maxByOrNull { it.age } // 인자가 단 하나 뿐인 경우 인자에 이름을 붙이지 않아도 된다.
```

### 컬렉션 함수형 API

> 자바에서 흔히 사용해오던 stream의 연산들을 코틀린 컬렉션에서 대부분 지원한다.

```kotlin
val people = listOf(
	Person("John", 5),
	Person("Kim", 10),
	Person("Park", 15),
	Person("Lee", 20),
	Person("James", 25)
)

val result = people
	.filter { it.age >= 10 }
	.map { it.name }
	.groupBy { it.length }

println(result)
// {3=[Kim, Lee], 4=[Park], 5=[James]}
```

> filter, map, all, any, count, find, groupBy, flatMap 등등 다양한 함수형 API를 지원하고 있다.

## 시퀀스 : 지연 컬렉션 연산

> 앞에서 map, filter 와 같은 컬렉션 함수들을 살펴봤다. 그런 함수는 결과 컬렉션을 즉시(eagerly) 생성한다. 이는 컬렉션 함수를 연쇄하면 매 단계마다 계산 중간 결과를 새로운 컬렉션에 임시로 담는다는 말이다.

```kotlin
val result = people
	.filter { it.age >= 10 } // 연산 결과를 임시 리스트로 생성
	.map { it.name } // 연산 결과를 임시 리스트로 생성
	.groupBy { it.length } // 연산 결과를 result 변수에 저장
```

> 앞서 실행한 코드에서 filter -> map -> groupBy 로 연쇄 호출이 이루어 지는데, 이는 결국 연산에 의해 컬렉션이 총 3개 생성된다는 뜻이다. 메모리적인 측면으로 봤을 때 임시로 생성된 리스트들은 gc의 대상이므로 큰 문제가 없겠지만 각각의 컬렉션 연산마다 새로운 리스트를 생성하므로 people 리스트의 원소가 수백만 개라면 효율이 떨어질 수 밖에 없다.

> 시퀀스(sequence) 를 사용하면 중간 임시 컬렉션을 사용하지 않고도 컬렉션 연산을 연쇄할 수 있다.

```kotlin
val result = people.asSequence() // 원본 컬렉션을 시퀀스로 변환
	.filter { it.age >= 10 }
	.map { it.name }
	.groupBy { it.length } // 결과 시퀀스를 Map으로 변환
	
	// 참고: 시퀀스 연산의 결과는 시퀀스이기 때문에 결과를 컬렉션으로 받으려면
	// .toSet() .toList() .groupBy {} 와 같은 메서드로 연쇄 호출을 마무리해야 한다.
```

> 8.2 절에서는 중간 컬렉션을 생성함에도 불구하고 코틀린에서 즉시 계산 컬렉션에 대한 연산이 더 효율적인 이유를 설명한다. 하지만 컬렉션에 들어있는 원소가 많으면 중간 원소를 재배열하는 비용이 커지기 때문에 지연 계산이 더 낫다.

```kotlin
//         Person("John", 5),
//         Person("Kim", 10),
//         Person("Park", 15),
//         Person("Lee", 20),
//         Person("James", 25)

val sequence = people.asSequence()
	.filter { println("filter $it"); it.age >= 10 }
	.map { println("map $it"); it.name }

for (person in sequence.iterator()) {
	println(person)
}
```

실행결과 사진 참조

> 값을 실제 참조하기 전까지는 컬렉션 연산이 수행되지 않는 것을 확인할 수 있다.

> 자바 8 스트림을 아는 독자라면 시퀀스라는 개념이 스트림과 같다는 사실을 알았을 것이다. 코틀린에서 같은 개념을 따로 구현해 제공하는 이유는 안드로이드 등에서 예전 버전 자바를 사용하는 경우 자바 8에 있는 스트림이 없기 때문이다.

```kotlin
val naturalNumbers = generateSequence(0) { it + 1 }
val numbersTo100 = naturalNumbers.takeWhile { it <= 100 }
println(numbersTo100.sum()) // 최종 연산 sum 을 수행하기 전까지 시퀀스의 각 숫자는 계산되지 않는다.
```

> 코틀린에서는 시퀀스를 직접 만드는 generateSequence() 메서드를 제공한다.

## 자바 함수형 인터페이스를 코틀린에서 사용

**자바의 경우**

```java

// 기술온보딩 - 스프링 카페 미션 [모든 회원을 조회하는 findAll() 메서드]
public List<User> findAll() {
	String sql = "select u.id, u.username, u.password from users u";
	return jdbcTemplate.query(sql, mapper);
}

private static class UserMapper implements RowMapper<User> {
	@Override
	public User mapRow(ResultSet rs, int rowNum) throws SQLException {
		return new User(
				rs.getLong("id"),
				rs.getString("username"),
				rs.getString("password")
		);
	}
}

// 익명 클래스로 리팩터링
public List<User> findAll() {
	String sql = "select u.id, u.username, u.password from users u";
	return jdbcTemplate.query(sql, new RowMapper<User>() {
		@Override
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new User(
					rs.getLong("id"),
					rs.getString("username"),
					rs.getString("password")
			);
		}
	});
}

// 람다로 리팩터링
public List<User> findAll() {
	String sql = "select u.id, u.username, u.password from users u";
	return jdbcTemplate.query(sql, (rs, rowNum) -> new User(
			rs.getLong("id"),
			rs.getString("username"),
			rs.getString("password")
	));
}
```

**코틀린의 경우**

```kotlin
fun findAll(): List<User> {
    val sql = "select u.id, u.username, u.password from users u"
    return jdbcTemplate.query(sql) {rs, rowNum -> User(
            rs.getLong("id"),
            rs.getString("username"),
            rs.getString("password")
        )
    }
}
```

> 코드가 문제없이 작동하는 이유는 RowMapper 인터페이스에 추상 메서드가 단 하나만 있기 때문이다. (함수형 인터페이스 또는 SAM 인터페이스 라고 함)
> 자바에서 함수형 프로그래밍을 흉내내기 위해 함수형 인터페이스를 구현했고, 코틀린은 함수형 인터페이스를 인자로 취하는 자바 메 서드를 호출할 때 람다를 넘길 수 있으므로 자바의 익명 클래스와 동일한 효과를 낼 수 있다.

> 자바와 달리 코틀린에는 제대로 된 함수 타입이 존재한다. 따라서 코틀린에서 함수를 인자로 받을 필요가 있는 함수는 함수형 인터페이스가 아니라 함수 타입을 인자 타입으로 사용해야 한다. 코틀린 함수를 사용할 때는 코틀린 컴파일러가 코틀린 람다를 함수형 인터페이스로 변환해주지 않는다. 함수 선언에서 함수 타입을 사용하는 방법은 8.1절에서 설명한다.

```kotlin
fun postPoneComputation(delay: Long, computation: Runnable) {
    Thread.sleep(delay)
    computation.run()
}

postPoneComputation(1000) { println("foo") } // 코틀린 컴파일러가 람다를 Runnable 인스턴스로 변환
```

```kotlin
postPoneComputation(1000, object : Runnable {
	override fun run() {
		println("bar")
	}
})
```

> Runnable을 구현하는 익명 객체를 명시적으로 만들어서 사용할 수도 있다.
> 하지만 람다와 익명 객체 사이에는 차이가 있다. 객체를 명시적으로 선언하는 경우 메서드를 호출할 때마다 새로운 객체가 생성된다.
> 람다의 경우 컴파일러는 익명 객체를 메서드 밖에 만들어 놓고 아래와 같이 메서드 호출마다 반복 사용한다.

```kotlin
val runnable = Runnable { println("foo") }
postPoneComputation(1000, runnable)
```

> 하지만 람다가 주변 영역의 변수를 '포획' 한다면 매 호출하다 같은 인스턴스를 사용할 수 없기 때문에 컴파일러는 매번 주변 영역의 변수를 포획한 새로운 인스턴스를 생성해준다.

## 수신 객체 지정 람다 : with & apply

> 자바의 람다에는 없는 코틀린 람다의 독특한 기능들이다.

### with

> 어떤 객체의 이름을 반복하지 않고도 그 객체에 대해 다양한 연산을 수행할 수 있는 함수이다.

```kotlin
fun alphabet(): String {
    val result = StringBuilder()
    for (letter in 'A'..'Z') {
        result.append(letter)
    }
    result.append("\nDone!")
    return result.toString()
}
```

> 위 예제에서 result 에 대해 다른 여러 메서드를 호출하면서 매번 result 를 반복 사용 했다. 이정도는 괜찮지만 코드가 훨씬 길거나, result 를 더 자주 반복해야 했다면 어땠을까?

```kotlin
fun alphabetUsingWith(): String {
    val stringBuilder = StringBuilder()
    return with(stringBuilder) {
        for (letter in 'A'..'Z') {
            this.append(letter)
        }
        append("\nDone!") // this 를 생략하고 메서드를 호출할 수 있다.
        toString() // 람다에서 값을 반환한다.
    }
}
```

> with 은 코틀린이 지원하는 특별한 키워드처럼 보이지만 실제로는 파라미터가 2개 있는 함수다.
> 예제에서 첫 번째 파라미터는 StringBuilder 이고, 두 번째 파라미터는 람다다.

```kotlin
fun alphabetUsingWith() = with(StringBuilder()) {
    for (letter in 'A'..'Z') {
        append(letter)
    }
    append("\nDone!") // this 를 생략하고 메서드를 호출할 수 있다.
    toString() // 람다에서 값을 반환한다.
}
```

> 위와 같이 불필요한 stringBuilder 변수를 없애도록 리팩터링 할 수 있다.

### apply

```kotlin
fun alphabetWithApply() = StringBuilder().apply {
    for (letter in 'A'..'Z') {
        append(letter)
    }
    append("\nDone!")
}.toString()
```

> apply 함수는 거의 with 과 같다. 유일한 차이란 apply는 항상 자신에게 전달된 객체를 반환한다는 점이다.
> 위 예제에서는 apply 메서드가 실행된 후 StringBuilder 객체를 리턴한다.

> 자바에서는 보통 별도의 Builder 객체가 이런 역할을 담당한다. 코틀린에서는 어떤 클래스가 정의돼 있는 라이브러리의 특별한 지원 없이도 그 클래스 인스턴스에 대해 apply 를 활용할 수 있다.

```kotlin
fun alphabetWithBuildString() = buildString {
    for (letter in 'A'..'Z') {
        append(letter)
    }
    append("\nDone!")
}
```

> with 과 apply 는 수신 객체 지정 람다를 사용하는 일반적인 예제 중 하나이며, 위와 같이 더 구체적인 함수를 비슷한 패턴으로 활용할 수 있다.
> 위 예제에서는 표준 라이브러리의 buildString 함수를 사용해서 alphabet 함수를 더 단순화시켰다.

## 요약

1. 자바의 람다를 코틀린에서 동일하게 사용할 수 있다.
2. 코틀린에서는 람다가 함수의 맨 뒤 인자인 경우 괄호 밖으로 람다를 빼낼 수 있다.
3. 람다의 인자가 단 하나뿐인 경우 인자 이름을 지정하지 않고 it 이라는 디폴트 이름으로 참조할 수 있다.
4. 람다 안에서 람다 밖의 코드를 '포획' 해서 사용할 수 있다.
5. 메서드, 생성자, 프로퍼티 앞에 :: 를 붙이는 각각에 대한 참조를 만들 수 있고 이러한 참조를 다른 함수에 넘길 수 있다.
6. 자바의 stream 연산들을 코틀린 컬렉션에서 모두 지원하며 동일하게 람다로 표현 가능하다.
7. 시퀀스를 사용하면 컬렉션 연쇄 연산이 지연 실행되도록 할 수 있다.
8. with, apply 와 같이 다양한 수신 객체 지정 람다를 지원한다.
