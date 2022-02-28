package ch4

class Button : Clickable, DuplicateMethodInterface { // extends, implements 모두 : 로 표현된다.
    override fun click() { // 반드시 override 를 사용해야 한다.
        // 추상 메서드 구현
    }

    override fun defaultMethod() {
        // super<> 를 통해 상위 타입의 멤버 메서드를 호출할 수 있다.
        super<Clickable>.defaultMethod()
        super<DuplicateMethodInterface>.defaultMethod()
    }
}
