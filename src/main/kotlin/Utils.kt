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

    fun checkBounds(v: IntVec2): Boolean {
        return v.x in xRange && v.y in yRange
    }

    operator fun get(x: Int, y: Int): Char {
        return data[y][x]
    }

    operator fun get(v: IntVec2): Char {
        return data[v.y][v.x]
    }

    operator fun set(x: Int, y: Int, value: Char) {
        data[y][x] = value
    }

    operator fun set(v: IntVec2, value: Char) {
        data[v.y][v.x] = value
    }

    fun withXY(): Sequence<XYValue> {
        return sequence {
            for (y in yRange) {
                for (x in xRange) {
                    yield(XYValue(IntVec2(x, y), data[y][x]))
                }
            }
        }
    }

    fun flatten(): Sequence<Char> {
        return sequence {
            for (y in yRange) {
                for (x in xRange) {
                    yield(data[y][x])
                }
            }
        }
    }

    fun copy(): CharMatrix {
        return CharMatrix(Array(rows) { y -> data[y].copyOf() })
    }

    data class XYValue(val xy: IntVec2, val value: Char)
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

    fun checkBounds(v: IntVec2): Boolean {
        return v.x in xRange && v.y in yRange
    }

    operator fun get(x: Int, y: Int): Int {
        return data[y][x]
    }

    operator fun get(v: IntVec2): Int {
        return data[v.y][v.x]
    }

    operator fun set(x: Int, y: Int, value: Int) {
        data[y][x] = value
    }

    operator fun set(v: IntVec2, value: Int) {
        data[v.y][v.x] = value
    }

    fun withXY(): Sequence<XYValue> {
        return sequence {
            for (y in yRange) {
                for (x in xRange) {
                    yield(XYValue(IntVec2(x, y), data[y][x]))
                }
            }
        }
    }

    fun flatten(): Sequence<Int> {
        return sequence {
            for (y in yRange) {
                for (x in xRange) {
                    yield(data[y][x])
                }
            }
        }
    }

    fun copy(): IntMatrix {
        return IntMatrix(Array(rows) { y -> data[y].copyOf() })
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