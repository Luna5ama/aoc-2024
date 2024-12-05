import kotlin.math.abs

fun main() {
    fun p1() {
        val inputP1 = readInput("D1P1.txt")
        val leftNums = mutableListOf<Int>()
        val rightNums = mutableListOf<Int>()
        val nums = inputP1.lineSequence()
            .filter { it.isNotBlank() }
            .map { line ->
                line.splitToSequence("   ").map { intStr -> intStr.toInt() }.toList()
            }.toList()
        nums.forEach { it ->
            leftNums.add(it[0])
            rightNums.add(it[1])
        }
        leftNums.sort()
        rightNums.sort()
        var totalDistance = 0
        (leftNums zip rightNums).forEach { (left, right) ->
            totalDistance += abs(right - left)
        }
        println(totalDistance)
    }

    println("Part 1:")
    p1()
}