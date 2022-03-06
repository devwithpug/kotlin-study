package ch8

val value: Int = 5
val sum: (Int, Int) -> Int = { x, y -> x + y }
val action: (Any) -> Unit = { println(it) }
val noParam: () -> Unit = { println("no parameter") }

val paramCanBeNull: (Any?) -> Unit = { println(it) } // nullable 파라미터 타입
val canReturnNull: () -> Int? = { null } // nullable 리턴 타입
var funOrNull: ((Int, Int) -> Int)? = null // nullable 함수 타입
