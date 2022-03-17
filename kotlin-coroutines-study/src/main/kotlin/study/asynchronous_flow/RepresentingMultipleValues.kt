package study.asynchronous_flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun simpleSequence(): Sequence<Int> = sequence {
    for (i in 1..3) {
        Thread.sleep(100)
        yield(i) // yield next value
    }
}

suspend fun simpleSuspend(): List<Int> {
    delay(1000)
    return listOf(1, 2, 3)
}

fun simpleFlow(): Flow<Int> = flow {
    for (i in 1..3) {
        delay(100)
        emit(i) // emit next value
    }
}

fun main() = runBlocking {
    // simpleSequence().forEach { println(it) }
    // simpleSuspend().forEach { println(it) }

    launch {
        for (k in 1..3) {
            println("I'm not blocked $k")
            delay(100)
        }
    }
    simpleFlow().collect { println(it) }
}
