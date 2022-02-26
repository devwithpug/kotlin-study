package ch5

fun main() {
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

    flatten()
    withSequence(people)
}

fun flatten() {
    val strings = listOf("abc", "def")
    println(strings.flatMap { it.toList() })
}

fun withSequence(people: List<Person>) {
    val result = people.asSequence()
        .filter { it.age >= 10 }
        .map { it.name }
        .groupBy { it.length }

    println(result)
}
