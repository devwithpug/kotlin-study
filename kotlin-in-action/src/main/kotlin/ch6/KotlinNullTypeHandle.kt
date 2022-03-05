package ch6

fun ignoreNull(s: String?) {
    val sNotNull = s!!
    println(sNotNull.length)
}

fun sendEmailTo(email: String) {}

fun main() {
    val email: String? = "foo"
    email?.let { sendEmailTo(it) }
}
