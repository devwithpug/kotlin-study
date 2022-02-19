package ch1

data class Person( // data class
    val name: String,
    val age: Int? = null // nullable type & default value
)

fun main() {
    val persons = listOf(
        Person("pug", age = 25), // named argument
        Person("bug")
    )

    val oldest = persons.maxByOrNull { it.age ?: 0 } // lambda & elvis operator
    println("나이가 가장 많은 사람: $oldest") // string template
}
