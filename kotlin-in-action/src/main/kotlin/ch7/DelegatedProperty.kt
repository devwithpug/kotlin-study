package ch7

import kotlin.reflect.KProperty

class Foo {
    var p: Delegate by Delegate()
}

class Delegate {
    operator fun getValue(foo: Foo, property: KProperty<*>): Delegate {
        TODO("Not yet implemented")
    }

    operator fun setValue(foo: Foo, property: KProperty<*>, type: Delegate) {
        TODO("Not yet implemented")
    }
}

class Person(
    val name: String
) {
    val emails by lazy { /* loadEmails(this) */ }
}
