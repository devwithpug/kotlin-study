package ch5


fun postPoneComputation(delay: Long, computation: Runnable) {
    Thread.sleep(delay)
    computation.run()
}

fun main() {
    postPoneComputation(1000) { println("foo") }

    postPoneComputation(1000, object : Runnable {
        override fun run() {
            println("bar")
        }
    })

    val runnable = Runnable { println("foo") }
    postPoneComputation(1000, runnable)
}
