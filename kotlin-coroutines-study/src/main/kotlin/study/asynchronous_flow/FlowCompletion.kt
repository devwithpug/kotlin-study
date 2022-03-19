package study.asynchronous_flow

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.runBlocking

suspend fun imperativeFinallyBlock() = coroutineScope {
    try {
        simpleDelayFlow().collect { println(it) }
    } finally {
        println("Done")
    }
}

suspend fun declarativeHandling() = coroutineScope {
    simpleDelayFlow()
        .onCompletion { println("Done") }
        .collect { println(it) }
}

fun throwExceptionFlow() = flow {
    emit(1)
    throw RuntimeException()
}

fun main() = runBlocking {
    // imperativeFinallyBlock()
    // declarativeHandling()
    throwExceptionFlow()
        .onCompletion { if (it != null) println("Flow completed exceptionally") }
        .catch { println("Caught $it") }
        .collect { println(it) }
}
