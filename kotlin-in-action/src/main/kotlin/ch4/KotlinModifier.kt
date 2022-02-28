package ch4

open class RichButton : Clickable {
    fun disable() {} // final method
    open fun animate() {} // open method 오버라이드 가능
    override fun click() {} // override method 기본적으로 open 이다.
}

abstract class Animated { // abstract class
    abstract fun animate() // abstract method
    open fun stopAnimating() {}
    fun animateTwice() {}
}
