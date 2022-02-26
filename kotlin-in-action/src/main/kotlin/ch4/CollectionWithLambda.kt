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
}
