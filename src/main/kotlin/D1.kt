import kotlin.math.abs

fun main() {
    run {
        val input = readInput("D1.txt")
        val leftNums = mutableListOf<Int>()
        val rightNums = mutableListOf<Int>()
        input.lineSequence()
            .map { line ->
                line.splitToSequence("   ").map { it.toInt() }.toList()
            }.forEach {
                leftNums.add(it[0])
                rightNums.add(it[1])
            }
        leftNums.sort()
        rightNums.sort()
        val totalDistance = (leftNums zip rightNums).sumOf { (left, right) ->
            abs(right - left)
        }
        println("Part 1:")
        println(totalDistance)
    }

    run {
        val inputP1 = readInput("D1.txt")
        val leftNums = mutableListOf<Int>()
        val rightNumCounts = mutableMapOf<Int, Int>()
        inputP1.lineSequence()
            .map { line ->
                line.splitToSequence("   ").map { it.toInt() }.toList()
            }.forEach {
                leftNums.add(it[0])
                rightNumCounts[it[1]] = rightNumCounts.getOrDefault(it[1], 0) + 1
            }

        val totalSimilarityScore = leftNums.sumOf { left ->
            left * rightNumCounts.getOrDefault(left, 0)
        }
        println("Part 2:")
        println(totalSimilarityScore)
    }
}