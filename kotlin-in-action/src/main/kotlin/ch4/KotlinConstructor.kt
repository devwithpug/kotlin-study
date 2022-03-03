package ch4

// 주 생성자
// class User(val nickname: String)

// 구조는 아래와 같다.
class User constructor(_nickname: String) { // constructor 는 주 생성자 또는 부 생성자 정의를 시작할 때 사용한다.
    private val nickname: String
    init { // 초기화 블록
        nickname = _nickname
    }
}

open class Car(val name: String)

class Bmw(name: String) : Car(name) // 기반 클래스의 생성자 호출

class Secretive private constructor()

open class View {
    constructor() {
        // 부 생성자
    }

    constructor(name: String) {
        // 여러 부 생성자를 선언할 수 있다.
    }
}

// 이 인터페이스를 구현하는 클래스가 nickname 의 값을 얻을 수 있는 방법을 제공해야 한다는 뜻
interface UserInterface {
    val nickname: String
}

class PrivateUser(override val nickname: String) : UserInterface

class SubscribingUser(val email: String) : UserInterface {
    override val nickname: String
        get() = email.substringBefore('@') // 커스텀 getter
}

class FacebookUser(val accountId: Int) : UserInterface {
    override val nickname = getFacebookName(accountId)

    private fun getFacebookName(accountId: Int): String {
        return ""
    }
}
