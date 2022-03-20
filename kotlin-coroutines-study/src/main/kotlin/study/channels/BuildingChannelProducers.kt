package study.channels

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

private fun CoroutineScope.produceSquares(): ReceiveChannel<Int> = produce {
    for (x in 1..5) {
        delay(1000)
        send(x * x)
    }
}

fun main() = runBlocking {
    val squares = produceSquares()
    squares.consumeEach { println(it) } // (1) .consumeEach()

    for (x in squares) { // (2) for loop
        println(x)
    }
    println("Done!")
}
