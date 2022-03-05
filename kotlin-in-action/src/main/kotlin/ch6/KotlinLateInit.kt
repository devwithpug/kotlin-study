package ch6

data class SomeDto(
    val name: String,
) {
    val value: String? = null // 반드시 초기화 해야한다.
    lateinit var result: String // 나중에 초기화할 수 있다.
}

fun main() {
    val dto = SomeDto("name")

    dto.result // UninitializedPropertyAccessException 예외 발생

    dto.result = "Lazy Initialized!"
}

fun verifyUserInput(input: String?) {
    if (input.isNullOrBlank()) { // ?. safe call 을 사용하지 않아도 된다.

    }
}

fun String?.isNullOrBlank() = this == null || this.isBlank()
// null check 를 먼저 수행하므로 실제 사용시에 safe call 을 하지 않아도 된다.
