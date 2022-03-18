package study.asynchronous_flow

import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.fold
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val sum = (1..5).asFlow()
        .map { it * it }
        .fold(1) { a, b -> a + b }
    println(sum)
}
