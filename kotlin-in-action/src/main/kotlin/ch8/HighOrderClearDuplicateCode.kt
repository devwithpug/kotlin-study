package ch8

data class SiteVisit(
    val path: String,
    val duration: Double,
    val os: OS
)

enum class OS { WINDOWS, LINUX, MAC, IOS, ANDROID }

val log = listOf(
    SiteVisit("/", 34.0, OS.WINDOWS),
    SiteVisit("/", 22.0, OS.MAC),
    SiteVisit("/login", 12.0, OS.WINDOWS),
    SiteVisit("/signup", 8.0, OS.IOS),
    SiteVisit("/", 16.3, OS.ANDROID),
)

fun List<SiteVisit>.averageDurationFor(os: OS) =
    filter { it.os == os }.map { it.duration }.average()

fun List<SiteVisit>.averageDurationFor(predicate: (SiteVisit) -> Boolean) =
    filter(predicate).map { it.duration }.average()

fun main() {
    val averageWindowsDuration = log
        .filter { it.os == OS.WINDOWS }
        .map { it.duration }
        .average()

    val averageMacDuration = log
        .filter { it.os == OS.MAC }
        .map { it.duration }
        .average()

    println(averageWindowsDuration) // 23.0
    println(averageMacDuration) // 22.0

    println(log.averageDurationFor(OS.WINDOWS))
    println(log.averageDurationFor(OS.MAC))

    println(log.averageDurationFor { it.os == OS.IOS && it.path == "/signup" }) // 8.0
}
