package study.asynchronous_flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking

private fun simpleColdFlow(): Flow<Int> = flow {
    println("Flow started")
    for (i in 1..3) {
        delay(100)
        emit(i)
    }
}

fun main() = runBlocking {
    println("Calling simple function...")
    val flow = simpleColdFlow()
    println("Calling collect...")
    flow.collect { println(it) }
    println("Calling collect again...")
    flow.collect { println(it) }
}
