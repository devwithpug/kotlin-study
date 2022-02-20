package ch2

import java.io.BufferedReader
import java.io.StringReader
import kotlin.NumberFormatException

// 예외 throw, try, catch, finally 모두 java 와 동일하게 사용 가능하다.
// kotlin 에서는 checked, unchecked 예외를 나누지 않는다.
// 불필요한 IOException 에 대한 처리를 하지 않아도 된다!
fun readNumber(reader: BufferedReader) {
    val number = try {
        Integer.parseInt(reader.readLine())
    } catch (e: NumberFormatException) {
        -1
    }
    println(number)
}

fun main() {
    val reader = BufferedReader(StringReader("not a number"))
    readNumber(reader) // -1 (NumberFormatException)
}
