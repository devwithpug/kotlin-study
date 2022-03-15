package study

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull
import kotlinx.coroutines.yield

fun main() {
    runBlocking {
        // cancellationCoroutines()
        // didNotCancel()
        // isActiveToCheckCancellation()
        // yieldToCheckCancellation()
        // closingResourcesWithFinally()
        // runNonCancellableBlock()
        // coroutineWithTimeout()
        // coroutineWithTimeoutOrNull()
        acquireResource()
    }
    println(acquired)
}

suspend fun cancellationCoroutines() = coroutineScope {
    println("Cancellation coroutines")
    val job = launch {
        repeat(1000) { i ->
            println("job: I'm sleeping $i ...")
            delay(500L)
        }
    }
    delay(1300L)
    println("main: I'm tired of waiting!")
    job.cancelAndJoin()
    println("main: Now I can quit.")
}

suspend fun didNotCancel() = coroutineScope {
    println("Did not cancel")
    val startTime = System.currentTimeMillis()
    val job = launch(Dispatchers.Default) {
        var nextPrintTime = startTime
        var i = 0
        while (i < 5) {
            if (System.currentTimeMillis() >= nextPrintTime) {
                println("job: I'm sleeping ${i++} ...")
                nextPrintTime += 500L
            }
        }
    }
    delay(1300L)
    println("main: I'm tired of waiting!")
    job.cancelAndJoin()
    println("main: Now I can quit.")
}

suspend fun isActiveToCheckCancellation() = coroutineScope {
    println("isActive to check cancellation")
    val startTime = System.currentTimeMillis()
    val job = launch(Dispatchers.Default) {
        var nextPrintTime = startTime
        var i = 0
        while (isActive) {
            if (System.currentTimeMillis() >= nextPrintTime) {
                println("job: I'm sleeping ${i++} ...")
                nextPrintTime += 500L
            }
        }
    }
    delay(1300L)
    println("main: I'm tired of waiting!")
    job.cancelAndJoin()
    println("main: Now I can quit.")
}

suspend fun yieldToCheckCancellation() = coroutineScope {
    println("yield to check cancellation")
    val startTime = System.currentTimeMillis()
    val job = launch(Dispatchers.Default) {
        var nextPrintTime = startTime
        var i = 0
        while (i < 5) {
            yield()
            if (System.currentTimeMillis() >= nextPrintTime) {
                println("job: I'm sleeping ${i++} ...")
                nextPrintTime += 500L
            }
        }
    }
    delay(1300L)
    println("main: I'm tired of waiting!")
    job.cancelAndJoin()
    println("main: Now I can quit.")
}

suspend fun closingResourcesWithFinally() = coroutineScope {
    println("Closing resources with finally")
    val job = launch(Dispatchers.Default) {
        try {
            repeat(1000) { i ->
                println("job: I'm sleeping $i ...")
                delay(500L)
            }
        } catch (e: CancellationException) {
            println(e)
        } finally {
            println("job: I'm running finally")
            delay(1000L)
            println("코루틴이 취소됐으므로 출력되지 않음")
        }
    }
    delay(1300L)
    println("main: I'm tired of waiting!")
    job.cancelAndJoin()
    println("main: Now I can quit.")
}

suspend fun runNonCancellableBlock() = coroutineScope {
    println("Run non-cancellable block")
    val job = launch {
        try {
            repeat(1000) { i ->
                println("job: I'm sleeping $i ...")
                delay(500L)
            }
        } finally {
            withContext(NonCancellable) {
                println("job: I'm running finally")
                delay(1000L)
                println("NonCancellable 이기 때문에 이 블록은 취소될 수 없다.")
            }
        }
    }
    delay(1300L)
    println("main: I'm tired of waiting!")
    job.cancelAndJoin()
    println("main: Now I can quit.")
}

suspend fun coroutineWithTimeout() = coroutineScope {
    withTimeout(1300L) {
        repeat(1000) { i ->
            println("I'm sleeping $i ...")
            delay(500L)
        }
    }
}

suspend fun coroutineWithTimeoutOrNull() = coroutineScope {
    val result = withTimeoutOrNull(1300L) {
        repeat(1000) { i ->
            println("I'm sleeping $i ...")
            delay(500L)
        }
        "Done"
    }
    println("result is $result")
}

var acquired = 0

class Resource {
    init { acquired++ } // 리소스 획득

    fun close() { acquired-- } // 리소스 해제
}

suspend fun acquireResource() = coroutineScope {
    repeat(100) {
        launch {
            var resource: Resource? = null
            try {
                withTimeout(60) {
                    delay(55)
                    resource = Resource()
                }
            } finally {
                resource?.close()
            }
        }
    }
}
