package ch4

class Outer {
    inner class Inner {
        fun getOuterReference(): Outer = this@Outer // 바깥 클래스의 참조 접근 방법
    }

    // class Inner {
    //     fun getOuterReference(): Outer = this@Outer // 컴파일 에러! 바깥쪽 클래스에 대한 참조가 없음
    // }
}
