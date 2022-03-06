package ch8

fun twoAndThree(operation: (Int, Int) -> Int) {
    val result = operation(2, 3)
    println("The result is $result")
}

fun main() {
    twoAndThree { x, y -> x + y } // The result is 5
    twoAndThree { x, y -> x * y } // The result is 6
    // twoAndThree { _, _ -> throw IllegalStateException() }

    println("STRing".myFilter { it in 'A'..'Z' }) // STR

    val items = listOf(
        Item(1, "itemA"),
        Item(2, "itemB")
    )
    val resultString = items.joinToString(prefix = "[", postfix = "]")
    println(resultString) // [Item(id=1, name=itemA), Item(id=2, name=itemB)]

    val resultString2 = items.joinToString(prefix = "[", postfix = "]") { it.name }
    println(resultString2) // [itemA, itemB]

    println("AnonymousFunction".myFilterWithAnonymousFunction { it in 'A'..'Z' }) // AF
}

fun String.myFilter(predicate: (Char) -> Boolean) = buildString {
    with(this@myFilter) {
        for (index in indices) {
            val element = get(index)
            if (predicate(element)) {
                this@buildString.append(element)
            }
        }
    }
}

fun String.myFilterWithAnonymousFunction(predicate: (Char) -> Boolean) = buildString(fun(sb) {
    for (index in indices) {
        val element = get(index)
        if (predicate(element)) {
            sb.append(element)
        }
    }
})

fun <T> Collection<T>.joinToString(
    separator: String = ", ",
    prefix: String = "",
    postfix: String = ""
): String {
    val result = StringBuilder(prefix)
    for ((index, element) in this.withIndex()) {
        if (index > 0) {
            result.append(separator)
        }
        result.append(element)
    }
    result.append(postfix)
    return result.toString()
}

fun <T> Collection<T>.joinToString(
    separator: String = ", ",
    prefix: String = "",
    postfix: String = "",
    transform: (T) -> String = { it.toString() }
): String {
    val result = StringBuilder(prefix)
    for ((index, element) in this.withIndex()) {
        if (index > 0) {
            result.append(separator)
        }
        result.append(transform(element))
    }
    result.append(postfix)
    return result.toString()
}

data class Item(
    val id: Int,
    val name: String
)
