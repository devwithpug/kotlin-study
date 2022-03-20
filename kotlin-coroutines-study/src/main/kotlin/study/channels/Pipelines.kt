package study.channels

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import study.log

private fun CoroutineScope.produceNumbers() = produce {
    var x = 1
    while (x < 100) {
        send(x++)
    }
}

private fun CoroutineScope.square(numbers: ReceiveChannel<Int>) = produce {
    for (x in numbers) {
        send(x * x)
    }
}

suspend fun simplePipeline() = coroutineScope {
    val numbers = produceNumbers()
    val squares = square(numbers)
    repeat(5) {
        println(squares.receive())
    }
    println("Done!")
    coroutineContext.cancelChildren()
}

private fun CoroutineScope.numbersFrom(start: Int) = produce {
    var x = start
    while (true) send(x++)
}

private fun CoroutineScope.filter(numbers: ReceiveChannel<Int>, prime: Int) = produce {
    for (x in numbers) if (x % prime != 0) send(x)
}

suspend fun primeNumbersWithPipeline() = coroutineScope {
    var cur = numbersFrom(2)
    repeat(10) {
        val prime = cur.receive()
        log("$coroutineContext $prime")
        cur = filter(cur, prime)
    }
    coroutineContext.cancelChildren()
}

fun main() = runBlocking {
    // simplePipeline()
    primeNumbersWithPipeline()
}
