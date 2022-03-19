package study.asynchronous_flow

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

suspend fun simpleFlowException() = coroutineScope {
    try {
        simpleDelayFlow().collect {
            println(it)
            check(it < 5) { "Collected $it" }
        }
    } catch (e: Throwable) {
        println("Caught $e")
    }
}

suspend fun everythingIsCaughtFlowException() = coroutineScope {
    try {
        simpleDelayFlow()
            .map {
                check(it < 5) { "Crashed on $it" }
                "string $it"
            }
            .collect { println(it) }
    } catch (e: Throwable) {
        println("Caught $e")
    }
}

suspend fun transparentCatch() = coroutineScope {
    simpleDelayFlow()
        .catch { emit(-1) } // 아래 다운스트림의 예외는 catch 하지 않는다.
        .map {
            check(it < 5) { "Crashed on $it" }
            "string $it"
        }
        .catch { emit("emitting exception $it") }
        .collect { println(it) }
}

fun main() = runBlocking {
    // simpleFlowException()
    // everythingIsCaughtFlowException()
    transparentCatch()
}
