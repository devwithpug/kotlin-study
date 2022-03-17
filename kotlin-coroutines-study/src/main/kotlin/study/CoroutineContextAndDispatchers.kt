package study

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.asContextElement
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import kotlin.coroutines.CoroutineContext

@OptIn(DelicateCoroutinesApi::class)
suspend fun setDispatchersWithLaunchParam() = coroutineScope {
    launch {
        println("main runBlocking\t\t: I'm working in thread ${Thread.currentThread().name}")
    }
    launch(Dispatchers.Unconfined) {
        println("Unconfined\t\t\t\t: I'm working in thread ${Thread.currentThread().name}")
    }
    launch(Dispatchers.Default) {
        println("Default\t\t\t\t\t: I'm working in thread ${Thread.currentThread().name}")
    }
    launch(newSingleThreadContext("MyOwnThread")) {
        println("newSingleThreadContext\t: I'm working in thread ${Thread.currentThread().name}")
    }
}

suspend fun unconfinedAndConfinedDispatcher() = coroutineScope {
    launch(Dispatchers.Unconfined) {
        println("Unconfined: I'm working in thread ${Thread.currentThread().name}")
        delay(500)
        println("Unconfined: After delay in thread ${Thread.currentThread().name}")
    }
    launch {
        println("main runBlocking: I'm working in thread ${Thread.currentThread().name}")
        delay(500)
        println("main runBlocking: After delay in thread ${Thread.currentThread().name}")
    }
}

@OptIn(DelicateCoroutinesApi::class)
suspend fun jumpingBetweenThreads() = coroutineScope {
    newSingleThreadContext("Ctx1").use { ctx1 ->
        newSingleThreadContext("Ctx2").use { ctx2 ->
            runBlocking(ctx1) {
                log("Started in ctx1")
                withContext(ctx2) {
                    log("Working in ctx2")
                }
                log("Back to ctx1")
            }
        }
    }
}

suspend fun printJobInTheContext() = coroutineScope {
    println("My job is ${coroutineContext[Job]}")
}

suspend fun childrenOfACoroutine() = coroutineScope {
    var childrenJob: Job? = null
    val request = launch {
        childrenJob = launch(Job()) {
            println("job1: I run in my own Job and execute independently!")
            delay(1000)
            println("job1: I am not affected by cancellation of the request")
        }
        launch {
            delay(100)
            println("job2: I am a child of the request coroutine")
            delay(1000)
            println("job2: I will not execute this line if my parent request is cancelled")
        }
    }
    delay(500)
    request.cancel()
    childrenJob?.join()
}

suspend fun parentalResponsibilities() = coroutineScope {
    val request = launch {
        repeat(3) { i ->
            launch {
                delay((i + 1) * 200L)
                println("Coroutine $i is done")
            }
        }
        println("request: I'm done and I don't explicitly join my children that are still alive")
    }
    request.join()
    println("Now processing of the request is complete")
}

suspend fun namingCoroutines() = coroutineScope {
    val v1 = async(CoroutineName("v1")) {
        delay(500)
        coroutineContext[CoroutineName]?.name
    }
    val v2 = async(CoroutineName("v2")) {
        delay(1000)
        coroutineContext[CoroutineName]?.name
    }
    println("${v1.await()} and ${v2.await()}")
}

suspend fun combiningContextElements() = coroutineScope {
    launch(Dispatchers.Default + CoroutineName("test")) {
        val threadName = Thread.currentThread().name
        val coroutineName = coroutineContext[CoroutineName]?.name
        println("I'm working in thread $threadName and my name is $coroutineName")
    }
}

suspend fun coroutineScopeWithActivity() = coroutineScope {
    val activity = Activity(coroutineContext)
    activity.doSomething()
    println("Launched coroutines")
    delay(500L)
    println("Destroying activity!")
    activity.destroy()
    delay(1000)
}

suspend fun threadLocalData() = coroutineScope {
    val threadLocal = ThreadLocal<String?>()

    threadLocal.set("main")
    println("Pre-main, current thread: ${Thread.currentThread()}, thread local value: `${threadLocal.get()}`")
    val job = launch(Dispatchers.Default + threadLocal.asContextElement(value = "launch")) {
        println("Launch start, current thread: ${Thread.currentThread()}, thread local value: `${threadLocal.get()}`")
        yield()
        println("After yield, current thread: ${Thread.currentThread()}, thread local value: `${threadLocal.get()}`")
    }
    job.join()
    println("Post-main, current thread: ${Thread.currentThread()}, thread local value: `${threadLocal.get()}`")
}

fun main() {
    runBlocking {
        // setDispatchersWithLaunchParam()
        // unconfinedAndConfinedDispatcher()
        // jumpingBetweenThreads()
        // printJobInTheContext()
        // childrenOfACoroutine()
        // parentalResponsibilities()
        // namingCoroutines()
        // combiningContextElements()
        // coroutineScopeWithActivity()
        threadLocalData()
    }
}

private fun log(msg: String) = println("[${Thread.currentThread().name}] $msg")

class Activity(
    context: CoroutineContext
) {
    private val coroutineScope = CoroutineScope(context)

    fun destroy() {
        coroutineScope.cancel()
    }

    fun doSomething() {
        repeat(10) { i ->
            coroutineScope.launch {
                delay((i + 1) * 200L)
                println("Coroutine $i is done")
            }
        }
    }
}
