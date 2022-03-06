package ch8

// fun oneAndTwo(operation: (Int, Int) -> Int) {
//     val result = operation(1, 2)
//     println("The result is $result")
// }

inline fun inlineOneAndTwo(operation: (Int, Int) -> Int) {
    val result = operation(1, 2)
    // topLevelValue = operation // compile error
    println("The result is $result")
}

var topLevelValue: (Int, Int) -> Int = { x, y -> x + y }

fun main() {
    // oneAndTwo { x, y -> x + y }
    // oneAndTwo { x, y -> x - y }
    // oneAndTwo { x, y -> x * y }
    // oneAndTwo { x, y -> x % y }
    inlineOneAndTwo { x, y -> x + y }
    inlineOneAndTwo { x, y -> x - y }
    inlineOneAndTwo { x, y -> x * y }
    inlineOneAndTwo { x, y -> x % y }
}

inline fun foo(
    inlined: () -> Unit,
    noinline notInlined: () -> Unit // 인라이닝을 금지
) { }
