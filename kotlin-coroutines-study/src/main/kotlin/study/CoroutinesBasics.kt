package study

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking {
        doWorld()
        println("End")
    }
    println("Done!") // 위 코루틴 스코프가 종료될 때까지 메인 스레드는 차단(block)된다.

    runBlocking { coroutinesAreLightWeight() }

    // threadTest()
}

suspend fun doWorld() = coroutineScope {
    val job = launch {
        delay(2000L)
        println("World 2")
    }
    println("Hello")
    job.join()
    launch {
        delay(1000L)
        println("World 1")
    }
}

suspend fun coroutinesAreLightWeight() = coroutineScope {
    repeat(100_000) {
        launch {
            delay(5000L)
            print(".")
            if (it % 1000 == 0) println()
        }
    }
}

fun threadTest() {
    repeat(100_000) {
        Thread {
            Thread.sleep(5000L)
            print(".")
            if (it % 1000 == 0) println()
        }.start()
    }
}
