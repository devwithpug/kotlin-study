package ch4
// 간단한 인터페이스 선언
interface Clickable {
    fun click() // 추상 메서드
    fun defaultMethod() = println("default method")
}
