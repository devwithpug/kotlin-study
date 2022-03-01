package ch4

// before
interface Expr {
    class Num(val value: Int): Expr
    class Sum(val left: Expr, val right: Expr): Expr

    fun eval(e: Expr): Int =
        when (e) {
            is Num -> e.value
            is Sum -> eval(e.right) + eval(e.left)
            else -> throw IllegalArgumentException() // 항상 디폴트 분기를 추가해야 한다.
        }
}

// after
sealed class SealedClassExpr {
    class Num(val value: Int): SealedClassExpr()
    class Sum(val left: SealedClassExpr, val right: SealedClassExpr): SealedClassExpr()
    fun eval(e: SealedClassExpr): Int =
        when (e) {
            is Num -> e.value
            is Sum -> eval(e.right) + eval(e.left)
        } // when 식이 모든 하위 클래스를 검사하므로 별도의 else 분기가 없어도 된다.
}

// kotlin 1.5 버전부터는 sealed interface 가 가능하다.
sealed interface SealedInterfaceExpr {
    class Num(val value: Int): SealedInterfaceExpr
    class Sum(val left: SealedInterfaceExpr, val right: SealedInterfaceExpr): SealedInterfaceExpr
    fun eval(e: SealedInterfaceExpr): Int =
        when (e) {
            is Num -> e.value
            is Sum -> eval(e.right) + eval(e.left)
        } // when 식이 모든 하위 클래스를 검사하므로 별도의 else 분기가 없어도 된다.
}
