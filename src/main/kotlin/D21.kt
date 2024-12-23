import kotlin.math.abs

typealias AdjList<N, E> = Map<N, Map<N, E>>

fun main() {
    val numpad = """
        789
        456
        123
         0A
    """.trimIndent().toCharMatrix()

    val dirpad = """
         ^A
        <v>
    """.trimIndent().toCharMatrix()

    fun CharMatrix.makeAdjList() = withXY()
        .filter { !it.value.isWhitespace() }
        .toList()
        .permutation(2)
        .groupBy(keySelector = { it.first() }, valueTransform = { it.last() })
        .mapValues { (a, bs) ->
            bs.associate { b ->
                val xDir = if (b.xy.x > a.xy.x) Direction4.RIGHT else Direction4.LEFT
                val yDir = if (b.xy.y > a.xy.y) Direction4.DOWN else Direction4.UP
                val xStr = xDir.toArrowChar().toString()
                val yStr = yDir.toArrowChar().toString()
                val xCount = abs(b.xy.x - a.xy.x)
                val yCount = abs(b.xy.y - a.xy.y)
                b.value to buildList<String> {
                    if (xCount > 0 && !this@makeAdjList[a.xy + xDir.vec * xCount].isWhitespace()) {
                        add(xStr.repeat(xCount) + yStr.repeat(yCount) + 'A')
                    }

                    if (yCount > 0 && !this@makeAdjList[a.xy + yDir.vec * yCount].isWhitespace()) {
                        add(yStr.repeat(yCount) + xStr.repeat(xCount) + 'A')
                    }

                    if (xCount == 0 && yCount == 0) {
                        add("A")
                    }
                }
            }
        }
        .mapKeys { it.key.value }

    val numpadAdjList = numpad.makeAdjList()
    val dirpadAdjList = dirpad.makeAdjList()

    class Chain(val list: List<AdjList<Char, List<String>>>) {
        private val cache = mutableMapOf<Pair<String, Int>, Long>()

        fun minSeqLenRecursive(code: String, index: Int): Long {
            if (index >= list.size) return code.length.toLong()
            return cache.getOrPut(code to index) {
                ('A' + code)
                    .windowed(2, 1)
                    .sumOf { str ->
                        val n = list[index][str[0]]!![str[1]]!!
                        n.minOf { minSeqLenRecursive(it, index + 1) }
                    }
            }
        }

        fun minSeqLen(code: String) = minSeqLenRecursive(code, 0)
    }

    val inputs = readInput("D21.txt").lines()

    run {
        val chain = Chain(listOf(numpadAdjList, dirpadAdjList, dirpadAdjList))
        val part1 = inputs
            .sumOf { code ->
                val codeNumeric = code.filter { it.isDigit() }.toInt()
                val seqLen = chain.minSeqLen(code)
                println("$seqLen, $codeNumeric")
                codeNumeric * seqLen
            }

        println("Part 1:")
        println(part1)
    }

    run {
        val chain = Chain(listOf(numpadAdjList) + List(25) { dirpadAdjList })
        val part2 = inputs
            .sumOf { code ->
                val codeNumeric = code.filter { it.isDigit() }.toInt()
                val seqLen = chain.minSeqLen(code)
                println("$seqLen, $codeNumeric")
                codeNumeric * seqLen
            }

        println("Part 2:")
        println(part2)
    }
}