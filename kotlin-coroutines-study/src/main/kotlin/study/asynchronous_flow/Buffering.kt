package study.asynchronous_flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import study.log
import kotlin.system.measureTimeMillis

fun simpleBuffering() = flow {
    for (i in 1..3) {
        log("emitting... $i")
        delay(100)
        emit(i)
    }
}

fun main() = runBlocking {
    val time = measureTimeMillis {
        simpleBuffering().buffer().collect {
            log("buffer $it")
            delay(300)
            println(it)
        }
    }
    println("Collected in $time ms")
}
