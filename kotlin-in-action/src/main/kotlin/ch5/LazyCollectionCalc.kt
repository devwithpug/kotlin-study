package ch5

fun main() {
    val people = listOf(
        Person("John", 5),
        Person("Kim", 10),
        Person("Park", 15),
        Person("Lee", 20),
        Person("James", 25)
    )

    val sequence = people.asSequence()
        .filter { println("filter $it"); it.age >= 10 }
        .map { println("map $it"); it.name }

    for (person in sequence.iterator()) {
        println(person)
    }

    val naturalNumbers = generateSequence(0) { it + 1 }
    val numbersTo100 = naturalNumbers.takeWhile { it <= 100 }
    println(numbersTo100.sum()) // 최종 연산 sum 을 수행하기 전까지 시퀀스의 각 숫자는 계산되지 않는다.
}
