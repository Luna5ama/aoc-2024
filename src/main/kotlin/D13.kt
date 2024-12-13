import it.unimi.dsi.fastutil.longs.LongOpenHashSet
import it.unimi.dsi.fastutil.objects.ObjectHeapPriorityQueue
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

fun main() {
    run {
        val input = readInput("D13.txt")

        data class Machine1(val ad: IntVec2, val bd: IntVec2, val prize: IntVec2) {
            inner class Node(val a: Int, val b: Int) : Comparable<Node> {
                val pos = ad * a + bd * b
                val tokens get() = a * 3 + b
                override fun compareTo(other: Node): Int {
                    return tokens.compareTo(other.tokens)
                }
            }
        }

        val buttonRegex = """Button [AB]: X\+([0-9]+), Y\+([0-9]+)""".toRegex()
        val prizeRegex = """Prize: X=([0-9]+), Y=([0-9]+)""".toRegex()

        fun Regex.parseIntVec2(line: String): IntVec2 {
            val (x, y) = find(line)!!.destructured
            return IntVec2(x.toInt(), y.toInt())
        }

        val part1 = input.lines()
            .filter { it.isNotBlank() }
            .windowed(3, 3) {
                val ad = buttonRegex.parseIntVec2(it[0])
                val bd = buttonRegex.parseIntVec2(it[1])
                val prize = prizeRegex.parseIntVec2(it[2])
                Machine1(ad, bd, prize)
            }
            .sumOf {
                val queue = ObjectHeapPriorityQueue<Machine1.Node>()
                val explored = LongOpenHashSet()
                queue.enqueue(it.Node(0, 0))

                var tokens = 0

                while (!queue.isEmpty) {
                    val node = queue.dequeue()
                    if (node.pos.x > it.prize.x || node.pos.y > it.prize.y) {
                        continue
                    }
                    if (!explored.add((node.a.toLong() shl 32) or (node.b.toLong()))) {
                        continue
                    }
                    if (node.pos == it.prize) {
                        tokens = node.tokens
                        break
                    }

                    queue.enqueue(it.Node(node.a + 1, node.b))
                    queue.enqueue(it.Node(node.a, node.b + 1))
                }

                tokens
            }

        println("Part 1:")
        println(part1)
    }

    run {
        val input = readInput("D13.txt")

        data class Machine2(val ad: LongVec2, val bd: LongVec2, val prize: LongVec2) {
            inner class Node(val a: Long, val b: Long) : Comparable<Node> {
                val pos = ad * a + bd * b
                val tokens get() = a * 3 + b
                override fun compareTo(other: Node): Int {
                    return tokens.compareTo(other.tokens)
                }
            }
        }

        val buttonRegex = """Button [AB]: X\+([0-9]+), Y\+([0-9]+)""".toRegex()
        val prizeRegex = """Prize: X=([0-9]+), Y=([0-9]+)""".toRegex()

        val add = 10000000000000L

        fun Regex.parseIntVec2(line: String): LongVec2 {
            val (x, y) = find(line)!!.destructured
            return LongVec2(x.toLong(), y.toLong())
        }

        val part2 = input.lines()
            .filter { it.isNotBlank() }
            .windowed(3, 3) {
                val ad = buttonRegex.parseIntVec2(it[0])
                val bd = buttonRegex.parseIntVec2(it[1])
                val prize = prizeRegex.parseIntVec2(it[2]) + add
                Machine2(ad, bd, prize)
            }
            .sumOf { machine ->
                val a = machine.ad.x.toBigDecimal()
                val b = machine.bd.x.toBigDecimal()
                val c = machine.ad.y.toBigDecimal()
                val d = machine.bd.y.toBigDecimal()
                val v = 1.0.toBigDecimal().divide((a * d - b * c), MathContext(128, RoundingMode.HALF_UP))
                val invMat = arrayOf(d * v, -b * v, -c * v, a * v)

                val px = machine.prize.x.toBigDecimal()
                val py = machine.prize.y.toBigDecimal()
                val va = (invMat[0] * px + invMat[1] * py).round(MathContext(32, RoundingMode.HALF_UP)).stripTrailingZeros()
                val vb = (invMat[2] * px + invMat[3] * py).round(MathContext(32, RoundingMode.HALF_UP)).stripTrailingZeros()

                if (va.scale() <= 0 && vb.scale() <= 0) {
                    va.toLong() * 3L + vb.toLong()
                } else {
                    0
                }
            }

        println("Part 2:")
        println(part2)
    }

}
