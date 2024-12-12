import java.io.File
import kotlin.math.abs

fun readInput(fileName: String): String {
    return File("inputs/$fileName").readText().trimEnd('\n')
}

enum class Direction4(val dx: Int, val dy: Int, val oppositeIndex: Int, val bitMask: Int) {
    UP(0, -1, 2, 0b0001),
    RIGHT(1, 0, 3, 0b0010),
    DOWN(0, 1, 0, 0b0100),
    LEFT(-1, 0, 1, 0b1000);

    val vec = IntVec2(dx, dy)
    val opposite get() = entries[oppositeIndex]
    val right get() = entries[(ordinal + 1) % 4]
    val left get() = entries[(ordinal + 3) % 4]
}

class CharMatrix {
    val rows: Int
    val cols: Int
    val data: CharArray
    val xRange: IntRange
    val yRange: IntRange

    private constructor(rows: Int, cols: Int, data: CharArray) {
        this.rows = rows
        this.cols = cols
        this.data = data
        xRange = 0..<cols
        yRange = 0..<rows
    }

    constructor(rows: Int, cols: Int, init: (Int, Int) -> Char) {
        this.rows = rows
        this.cols = cols
        data = CharArray(rows * cols) { index -> init(index % cols, index / cols) }
        xRange = 0..<cols
        yRange = 0..<rows
    }

    constructor(data: Array<CharArray>) {
        rows = data.size
        cols = data[0].size
        this.data = CharArray(rows * cols) { index -> data[index / cols][index % cols] }
        xRange = 0..<cols
        yRange = 0..<rows
    }

    fun checkBounds(x: Int, y: Int): Boolean {
        return x in xRange && y in yRange
    }

    fun checkBounds(v: IntVec2): Boolean {
        return checkBounds(v.x, v.y)
    }

    fun index(x: Int, y: Int): Int {
        return y * cols + x
    }

    operator fun get(x: Int, y: Int): Char {
        return data[index(x, y)]
    }

    operator fun get(v: IntVec2): Char {
        return get(v.x, v.y)
    }

    operator fun set(x: Int, y: Int, value: Char) {
        data[index(x, y)] = value
    }

    operator fun set(v: IntVec2, value: Char) {
        set(v.x, v.y, value)
    }

    fun withXY(): Sequence<XYValue> {
        return sequence {
            for (y in yRange) {
                for (x in xRange) {
                    yield(XYValue(IntVec2(x, y), get(x, y)))
                }
            }
        }
    }

    fun scale(factor: Int): CharMatrix {
        require(factor > 1)
        val newRows = rows * factor
        val newCols = cols * factor
        return CharMatrix(newRows, newCols) { x, y -> get(x / factor, y / factor) }
    }

    fun copy(): CharMatrix {
        return CharMatrix(rows, cols, data.copyOf())
    }

    data class XYValue(val xy: IntVec2, val value: Char)
}

class IntMatrix {
    val rows: Int
    val cols: Int
    val data: IntArray
    val xRange: IntRange
    val yRange: IntRange

    private constructor(rows: Int, cols: Int, data: IntArray) {
        this.rows = rows
        this.cols = cols
        this.data = data
        xRange = 0..<cols
        yRange = 0..<rows
    }

    constructor(rows: Int, cols: Int, init: (Int, Int) -> Int) {
        this.rows = rows
        this.cols = cols
        data = IntArray(rows * cols) { index -> init(index % cols, index / cols) }
        xRange = 0..<cols
        yRange = 0..<rows
    }

    constructor(rows: Int, cols: Int) {
        this.rows = rows
        this.cols = cols
        data = IntArray(rows * cols)
        xRange = 0..<cols
        yRange = 0..<rows
    }

    fun checkBounds(x: Int, y: Int): Boolean {
        return x in xRange && y in yRange
    }

    fun checkBounds(v: IntVec2): Boolean {
        return checkBounds(v.x, v.y)
    }

    fun index(x: Int, y: Int): Int {
        return y * cols + x
    }

    operator fun get(x: Int, y: Int): Int {
        return data[index(x, y)]
    }

    operator fun get(v: IntVec2): Int {
        return get(v.x, v.y)
    }

    operator fun set(x: Int, y: Int, value: Int) {
        data[index(x, y)] = value
    }

    operator fun set(v: IntVec2, value: Int) {
        set(v.x, v.y, value)
    }

    fun xy(): Sequence<IntVec2> {
        return sequence {
            for (y in yRange) {
                for (x in xRange) {
                    yield(IntVec2(x, y))
                }
            }
        }
    }

    fun withXY(): Sequence<XYValue> {
        return sequence {
            for (y in yRange) {
                for (x in xRange) {
                    yield(XYValue(IntVec2(x, y), get(x, y)))
                }
            }
        }
    }

    fun scale(factor: Int): IntMatrix {
        require(factor > 1)
        val newRows = rows * factor
        val newCols = cols * factor
        return IntMatrix(newRows, newCols) { x, y -> get(x / factor, y / factor) }
    }

    fun copy(): IntMatrix {
        return IntMatrix(rows, cols, data.copyOf())
    }

    data class XYValue(val xy: IntVec2, val value: Int)
}

fun String.toCharMatrix(): CharMatrix {
    val lines = lines()
    val rows = lines.size
    val cols = lines[0].length
    return CharMatrix(Array(rows) { y -> CharArray(cols) { x -> lines[y][x] } })
}

fun <T> List<T>.cartesianProduct(n: Int): Sequence<List<T>> {
    return if (n == 0) {
        sequenceOf(emptyList())
    } else {
        val indices = IntArray(n)
        sequence {
            while (true) {
                yield(indices.map { this@cartesianProduct[it] })
                var i = 0
                while (i < n && indices[i] == size - 1) {
                    indices[i] = 0
                    i++
                }
                if (i == n) break
                indices[i]++
            }
        }
    }
}

data class IntVec2(val x: Int, val y: Int) {
    operator fun plus(other: IntVec2) = IntVec2(x + other.x, y + other.y)
    operator fun plus(other: Int) = IntVec2(x + other, y + other)
    operator fun minus(other: IntVec2) = IntVec2(x - other.x, y - other.y)
    operator fun minus(other: Int) = IntVec2(x - other, y - other)
    operator fun times(scalar: Int) = IntVec2(x * scalar, y * scalar)
    operator fun times(other: IntVec2) = IntVec2(x * other.x, y * other.y)
    operator fun div(scalar: Int) = IntVec2(x / scalar, y / scalar)
    operator fun div(other: IntVec2) = IntVec2(x / other.x, y / other.y)
    operator fun unaryMinus() = IntVec2(-x, -y)
    fun manhattan() = abs(x) + abs(y)
}

fun Int.plus(other: IntVec2) = IntVec2(this + other.x, this + other.y)
fun Int.minus(other: IntVec2) = IntVec2(this - other.x, this - other.y)
fun Int.times(other: IntVec2) = IntVec2(this * other.x, this * other.y)
fun Int.div(other: IntVec2) = IntVec2(this / other.x, this / other.y)