package my.demo

lateinit var someValue: String

@OptIn(ExperimentalStdlibApi::class)
fun main() {
    val myRange = 100 downTo 0 step 5   // 100, 95, 90 ... 0
    val myList = buildList { for (x in myRange) add(x) } // [100, 95, 90, ..., 0]

    for (i in myList) {
        getScore(i)
    }

}

fun getScore(x: Int) = when {
    x > 90 -> "A"
    x > 80 -> "B"
    x > 70 -> "C"
    else -> "F"
}

fun initValue() {
    someValue = "initialized"
}