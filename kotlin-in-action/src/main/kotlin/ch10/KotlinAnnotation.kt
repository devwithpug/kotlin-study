package ch10

import kotlin.reflect.KClass

fun main() {
    val mutableListOf = mutableListOf("foo")
    // deprecatedLevelErrorFun() // compile error
    deprecatedAndReplaceWithFun() // has quick fix
}

@Deprecated("deprecated!", level = DeprecationLevel.ERROR)
fun deprecatedLevelErrorFun() {}

@Deprecated("deprecated!", replaceWith = ReplaceWith("notDeprecatedFun()"))
fun deprecatedAndReplaceWithFun() {}

fun notDeprecatedFun() {}

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class MyAnnotation(
    val clazz: KClass<out Any>
)

class MyClass

@MyAnnotation(MyClass::class)
fun myFun() {}
