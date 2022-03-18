package study.asynchronous_flow

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.runBlocking

suspend fun performRequest(request: Int): String {
    delay(1000)
    return "response $request"
}

suspend fun simpleTransformWithMap() = coroutineScope {
    (1..3).asFlow()
        .map { performRequest(it) }
        .collect { println(it) }
}

suspend fun transformOperator() = coroutineScope {
    (1..3).asFlow()
        .transform {
            emit("Making request $it")
            emit(performRequest(it))
        }
        .collect { println(it) }
}

suspend fun someFlowNumbers() = flow {
    try {
        emit(1)
        emit(2)
        println("This line will not execute")
        emit(3)
    } catch (e: Exception) {
        println(e)
    } finally {
        println("Finally in numbers")
    }
}

suspend fun sizeLimitingOperators() = coroutineScope {
    someFlowNumbers()
        .take(2)
        .collect { println(it) }
}

fun main() = runBlocking {
    // simpleTransformWithMap()
    // transformOperator()
    sizeLimitingOperators()
}
