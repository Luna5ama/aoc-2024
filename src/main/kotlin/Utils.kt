import java.io.File

fun readInput(fileName: String): String {
    return File("inputs/$fileName").readText().trimEnd('\n')
}

enum class Direction4(val dx: Int, val dy: Int, val oppositeIndex: Int, val bitMask: Int) {
    UP(0, -1, 2, 0b0001),
    RIGHT(1, 0, 3, 0b0010),
    DOWN(0, 1, 0, 0b0100),
    LEFT(-1, 0, 1, 0b1000);

    val opposite get() = entries[oppositeIndex]
    val right get() = entries[(ordinal + 1) % 4]
    val left get() = entries[(ordinal + 3) % 4]
}

class CharMatrix(val data: Array<CharArray>) {
    val rows = data.size
    val cols = data[0].size
    val xRange = 0 until cols
    val yRange = 0 until rows

    fun checkBounds(x: Int, y: Int): Boolean {
        return x in xRange && y in yRange
    }

    operator fun get(x: Int, y: Int): Char {
        return data[y][x]
    }

    operator fun set(x: Int, y: Int, value: Char) {
        data[y][x] = value
    }

    fun copy(): CharMatrix {
        return CharMatrix(Array(rows) { y -> data[y].copyOf() })
    }
}

class IntMatrix(val data: Array<IntArray>) {
    val rows = data.size
    val cols = data[0].size
    val xRange = 0 until cols
    val yRange = 0 until rows

    constructor(rows: Int, cols: Int) : this(Array(rows) { IntArray(cols) })
    constructor(rows: Int, cols: Int, init: (Int, Int) -> Int) : this(Array(rows) { y -> IntArray(cols) { x -> init(x, y) } })

    fun checkBounds(x: Int, y: Int): Boolean {
        return x in xRange && y in yRange
    }

    operator fun get(x: Int, y: Int): Int {
        return data[y][x]
    }

    operator fun set(x: Int, y: Int, value: Int) {
        data[y][x] = value
    }

    fun copy(): IntMatrix {
        return IntMatrix(Array(rows) { y -> data[y].copyOf() })
    }
}

fun String.toCharMatrix(): CharMatrix {
    val lines = lines()
    val rows = lines.size
    val cols = lines[0].length
    return CharMatrix(Array(rows) { y -> CharArray(cols) { x -> lines[y][x] } })
}