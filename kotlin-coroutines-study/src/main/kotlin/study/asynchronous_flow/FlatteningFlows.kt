package study.asynchronous_flow

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking

fun requestFlow(i: Int) = flow {
    emit("$i: First")
    delay(500)
    emit("$i: Second")
}

@OptIn(FlowPreview::class)
suspend fun simpleFlatMapConcat() = coroutineScope {
    val startTime = System.currentTimeMillis()
    (1..3).asFlow().onEach { delay(100) }.flatMapConcat { requestFlow(it) }
        .collect { println("$it at ${System.currentTimeMillis() - startTime} ms from start") }
}

@OptIn(FlowPreview::class)
suspend fun simpleFlatMapMerge() = coroutineScope {
    val startTime = System.currentTimeMillis()
    (1..3).asFlow().onEach { delay(100) }.flatMapMerge { requestFlow(it) }
        .collect { println("$it at ${System.currentTimeMillis() - startTime} ms from start") }
}

@OptIn(ExperimentalCoroutinesApi::class)
suspend fun simpleFlatMapLatest() = coroutineScope {
    val startTime = System.currentTimeMillis()
    (1..3).asFlow().onEach { delay(100) }.flatMapLatest { requestFlow(it) }
        .collect { println("$it at ${System.currentTimeMillis() - startTime} ms from start") }
}

fun main() = runBlocking {
    // simpleFlatMapConcat()
    // simpleFlatMapMerge()
    simpleFlatMapLatest()
}
