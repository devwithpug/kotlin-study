package ch8

enum class Delivery { STANDARD, ROCKET }

class Order(val itemCount: Int)

fun getShippingCostCalculator(delivery: Delivery): (Order) -> Int { // 함수를 리턴하는 함수 선언
    if (delivery == Delivery.ROCKET) {
        return { order -> 5000 * order.itemCount }
    }
    return { order -> 3000 * order.itemCount }
}

fun main() {
    val rocketCalculator = getShippingCostCalculator(Delivery.ROCKET)
    val standardCalculator = getShippingCostCalculator(Delivery.STANDARD)
    val myOrder = Order(2)

    println("일반 배송비는 ${standardCalculator(myOrder)} 입니다.")
    println("로켓 배송비는 ${rocketCalculator(myOrder)} 입니다.")
    // 일반 배송비는 10000 입니다.
    // 로켓 배송비는 6000 입니다.
}
