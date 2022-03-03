package ch4

object Payroll {
    val foo = mutableListOf<String>()
}

fun main() {
    Payroll.foo // 싱글턴 객체
    // Payroll() 불가능
}

class CompanionClass {
    companion object { // 동반 객체(정적)
        const val CONST_VALUE: String = "static val"
    }
}
