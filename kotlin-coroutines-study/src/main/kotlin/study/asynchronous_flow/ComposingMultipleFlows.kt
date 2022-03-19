package study.asynchronous_flow

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.runBlocking

suspend fun simpleZip() = coroutineScope {
    val nums = (1..3).asFlow()
    val strings = flowOf("one", "two", "three")
    nums.zip(strings) { a, b -> "$a -> $b" }
        .collect { println(it) }
}

suspend fun simpleZipDelay() = coroutineScope {
    val nums = (1..4).asFlow().onEach { delay(300) }
    val strings = flowOf("one", "two", "three").onEach { delay(400) }
    val startTime = System.currentTimeMillis()
    nums.zip(strings) { a, b -> "$a -> $b" }
        .collect { println("$it at ${System.currentTimeMillis() - startTime} ms from start") }
}

suspend fun simpleCombineDelay() = coroutineScope {
    val nums = (1..4).asFlow().onEach { delay(300) }
    val strings = flowOf("one", "two", "three").onEach { delay(400) }
    val startTime = System.currentTimeMillis()
    nums.combine(strings) { a, b -> "$a -> $b" }
        .collect { println("$it at ${System.currentTimeMillis() - startTime} ms from start") }
}

fun main() = runBlocking {
    // simpleZip()
    // simpleZipDelay()
    simpleCombineDelay()
}
