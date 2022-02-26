package ch5

fun main() {
    fun foo() = println("foo!")
    run(::foo) // 최상위 멤버 참조

    fun sendEmail() = println("Send email")
    val nextAction = ::sendEmail

    println("sendEmail() 실행 전")

    run(nextAction) // sendEmail() 실행

    val createPerson = ::Person
    val p = createPerson("Alice", 29)

}
