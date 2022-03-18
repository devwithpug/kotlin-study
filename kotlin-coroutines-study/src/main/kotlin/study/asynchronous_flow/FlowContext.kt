package study.asynchronous_flow

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import study.log

private fun simpleFlowContext() = flow {
    log("Started simple flow")
    for (i in 1..3) {
        emit(i)
    }
}

private fun wrongEmissionWithContext() = flow {
    withContext(Dispatchers.Default) {
        for (i in 1..3) {
            Thread.sleep(100)
            emit(i)
        }
    }
}

private fun emissionWithFlowOn() = flow {
    for (i in 1..3) {
        Thread.sleep(100)
        log("Emitting $i")
        emit(i)
    }
}.flowOn(Dispatchers.Default)

fun main() = runBlocking {
    // simpleFlowContext().collect { log("Collected $it") }
    // wrongEmissionWithContext().collect { println(it) }
    emissionWithFlowOn().collect { log("Collected $it") }
}
