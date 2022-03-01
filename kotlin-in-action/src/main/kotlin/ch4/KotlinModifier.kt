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

// 가시성 변경자
// public, internal, protected, private

internal open class TalkativeButton : Clickable {
    override fun click() {}
    private fun yell() {}
    protected fun whisper() {}
}

// fun TalkativeButton.giveSpeech() { // 컴파일에러 public 멤버가 자신의 internal 수신타입을 호출함. 자신의 가시성과 같거나 더 높아야만 한다.
//     click() // open, public (가능)
//     yell() // 컴파일에러 private 멤버 호출 불가능
//     whisper() // 컴파일에러 protected 멤버 호출 불가능
// }
