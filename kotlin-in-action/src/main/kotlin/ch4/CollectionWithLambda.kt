package ch4

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
    // the oldest person : Person(name=Bob, age=31)

    val theOldestWithLambda = people.maxByOrNull { it.age }
    println("the oldest person : $theOldestWithLambda")

    people.maxByOrNull(Person::age)
    // Optional<Person> max = people.stream().max(Person::getAge);

    people.maxByOrNull { p: Person -> p.age } // 함수의 맨 마지막 인자가 람다 식이므로 괄호 밖으로 빼낼 수 있다.
    people.maxByOrNull { p -> p.age } // 컴파일러가 문맥으로부터 유추할 수 있는 인자 타입을 생략할 수 있다.
    people.maxByOrNull { it.age } // 인자가 단 하나 뿐인 경우 인자에 이름을 붙이지 않아도 된다.
}
