package ch9

// 9.1.3 타입 파라미터 제약

private fun <T : Number> List<T>.sum(): T {
    TODO()
}

interface Gasoline
interface Electric

data class GasolineVehicle(
    val name: String
) : Gasoline

data class ElectricVehicle(
    val name: String
) : Electric

data class HybridVehicle(
    val name: String
) : Gasoline, Electric

fun <T : Gasoline> T.refuel() {}

fun <T : Electric> T.recharge() {}

// 타입 파라미터에 둘 이상의 제약을 가해야 하는 경우 where 구문을 사용한다
fun <T> T.hybridOnly() where T : Gasoline, T : Electric {}

class Processor<T> {
    fun process(value: T) {
        value?.hashCode() // value 는 nullable 하다
    }
}

class NotNullProcessor<T : Any> {
    fun process(value: T) {
        value.hashCode() // value 는 not null 이다
    }
}

fun main() {
    listOf(1, 2, 3).sum()
    // listOf("A", "B", "C").sum() // 컴파일 에러

    val gasolineVehicle = GasolineVehicle("G1")
    val electricVehicle = ElectricVehicle("E1")
    val hybridVehicle = HybridVehicle("H1")

    gasolineVehicle.refuel() // 급유만 가능
    electricVehicle.recharge() // 충전만 가능
    hybridVehicle.refuel() // 급유도 가능
    hybridVehicle.recharge() // 충전도 가능
}
