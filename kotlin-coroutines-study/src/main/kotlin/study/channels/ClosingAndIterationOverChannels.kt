package study.channels

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import study.log

fun main(): Unit = runBlocking {
    val channel = Channel<Int>()
    launch {
        for (x in 1..5) {
            delay(1000)
            log("$coroutineContext send... ${x * x}")
            channel.send(x * x)
        }
    }
    launch(Dispatchers.Default) {
        for (y in channel) {
            log("$coroutineContext $y")
        }
        println("Done!")
    }
}
