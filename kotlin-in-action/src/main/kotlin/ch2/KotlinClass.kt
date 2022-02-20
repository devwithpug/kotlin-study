package ch2

// public class JavaPerson {
//     private final String name;
//
//     public Person(String name) {
//         this.name = name;
//     }
//
//     public String getName() {
//         return name;
//     }
// }

// value object 라고 부른다.
// 코틀린의 기본 가시성은 public 이다.
class Person(
    val name: String, // 읽기 전용 프로퍼티: private & getter
    var isMarried: Boolean? = null // 쓸 수 있는 프로퍼티: private & getter, setter
)

// data: toString 자동생성을 위해 일부로 선언
data class Rectangle(
    private val height: Int,
    private val width: Int
) {
    // 커스텀 접근자 getter
    val isSquare: Boolean
        get() {
            return height == width
        }
    // 블록 & 리턴 타입 생략 가능
    val isSquareWithNoBlockStatement get() = height == width

    // 커스텀 접근자 setter
    var name: String = ""
    set(value) {
        println("$this setter called")
        field = value
    }
}

fun main() {
    val person = Person("pug.gg")
    person.isMarried = false
    // getter 를 사용하는 대신 프로퍼티로 직접 명시한다.
    println("name: ${person.name} / ${person.isMarried}")

    val rectangle = Rectangle(5, 5)
    println("$rectangle is square? ${rectangle.isSquare}")
    rectangle.name = "Square Rectangle"
}
