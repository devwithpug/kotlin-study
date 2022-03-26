package study.channels

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val channel = Channel<Int>(4) // 버퍼 생성
    val sender = launch {
        repeat(10) {
            println("Sending $it")
            channel.send(it) // 버퍼가 가득차면 suspend 된다.
        }
    }

    delay(1000)
    sender.cancel()
    repeat(4) {
        println("Received ${channel.receive()}")
    }
    delay(2000)
    channel.close()
    println("channel was closed")
}
