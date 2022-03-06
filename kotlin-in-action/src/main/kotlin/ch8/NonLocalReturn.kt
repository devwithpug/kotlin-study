package ch8

data class Person(
    val name: String,
    val age: Int
)

val people = listOf(Person("Alice", 29), Person("Bob", 31))

fun lookForAlice(people: List<Person>) {
    for (person in people) {
        if (person.name == "Alice") {
            println("Found!")
            return
        }
    }
    println("Alice is not found")
}

fun lookForEachAlice(people: List<Person>) {
    people.forEach {
        if (it.name == "Alice") {
            println("Found!")
            return
        }
    }
    println("Alice is not found")
}

fun lookForAliceWithLocalReturn(people: List<Person>) {
    people.forEach foo@{
        if (it.name == "Alice") {
            println("Found!")
            return@foo
        }
    }
    println("Alice is not found")
}

fun lookForAliceWithAnonymousFunction(people: List<Person>) {
    people.forEach(fun(person) {
        if (person.name == "Alice") {
            println("Found!")
            return
        }
        println("${person.name} is not Alice")
    })
}

fun main() {
    lookForAlice(people) // Found!
    lookForEachAlice(people) // Found!
    lookForAliceWithLocalReturn(people)
    lookForAliceWithAnonymousFunction(people)

    people.filter(fun(person): Boolean {
        return person.age < 30
    })

    people.filter(fun(person) = person.age < 30)
}
