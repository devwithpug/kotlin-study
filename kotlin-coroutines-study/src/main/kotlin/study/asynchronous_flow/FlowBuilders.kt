package study.asynchronous_flow

import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val numbers = arrayOf(1, 2, 3)
    flowOf(*numbers).collect { println(it) }
    (1..3).asFlow().collect { println(it) }
}
