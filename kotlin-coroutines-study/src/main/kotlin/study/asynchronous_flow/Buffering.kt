package study.asynchronous_flow

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import study.log
import kotlin.coroutines.coroutineContext
import kotlin.system.measureTimeMillis

fun simpleDelayFlow() = flow {
    for (i in 1..10) {
        log("$coroutineContext emitting... $i")
        delay(100)
        emit(i)
    }
}

suspend fun doBuffer() = coroutineScope {
    val time = measureTimeMillis {
        simpleDelayFlow().buffer().collect {
            log("$coroutineContext buffer $it")
            delay(500)
            println(it)
        }
    }
    println("Collected in $time ms")
}

suspend fun doConflate() = coroutineScope {
    val time = measureTimeMillis {
        simpleDelayFlow().conflate().collect {
            log("$coroutineContext conflate $it")
            delay(500)
            println(it)
        }
    }
    println("Collected in $time ms")
}

suspend fun doCollectLatest() = coroutineScope {
    val time = measureTimeMillis {
        simpleDelayFlow().collectLatest {
            log("$coroutineContext collecting $it")
            delay(300)
            println(it)
        }
    }
    println("Collected in $time ms")
}

fun main() = runBlocking {
    // doBuffer()
    // doConflate()
    doCollectLatest()
}
