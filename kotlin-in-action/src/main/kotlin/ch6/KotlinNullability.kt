package ch6

fun strLenSafe(s: String?): String? = s?.uppercase()
// (if (s != null) s.uppercase() else null 과 같다.

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
