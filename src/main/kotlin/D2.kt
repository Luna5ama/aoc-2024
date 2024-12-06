import kotlin.math.absoluteValue
import kotlin.math.sign

fun main() {
    fun isSafe(row: List<Int>): Boolean {
        val firstDiff = row[1] - row[0]
        return row.asSequence()
            .windowed(2, 1)
            .map { it[1] - it[0] }
            .all { it.sign == firstDiff.sign && it.absoluteValue in 1..3 }
    }

    run {
        val input = readInput("D2.txt")
        val safeCount = input.lineSequence()
            .map { line ->
                line.splitToSequence(" ").map { it.toInt() }.toList()
            }.count { row ->
                isSafe(row)
            }

        println("Part 1:")
        println(safeCount)
    }

    run {
        val input = readInput("D2.txt")
        val safeCount = input.lineSequence()
            .map { line ->
                line.splitToSequence(" ").map { it.toInt() }.toList()
            }
            .count { inputRow ->
                isSafe(inputRow) || inputRow.indices.any { index ->
                    val newRow = mutableListOf<Int>()
                    newRow.addAll(inputRow.subList(0, index))
                    newRow.addAll(inputRow.subList(index + 1, inputRow.size))
                    isSafe(newRow)
                }
            }

        println("Part 2:")
        println(safeCount)
    }
}