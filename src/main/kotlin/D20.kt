import it.unimi.dsi.fastutil.objects.ObjectArrayList
import kotlin.math.abs

fun main() {
    val input = readInput("D20.txt").toCharMatrix()

    val origGrid = IntMatrix(input.rows, input.cols)
    origGrid.data.fill(-1)
    input.withXY()
        .filter { it.value == '#' }
        .forEach { origGrid[it.xy] = -2 }

    val inputStart = input.withXY().find { it.value == 'S' }!!.xy
    val inputEnd = input.withXY().find { it.value == 'E' }!!.xy

    fun bfs(grid: IntMatrix, startPos: IntVec2, maxStep: Int = Int.MAX_VALUE) {
        grid[startPos] = 0
        var posList = ObjectArrayList<IntVec2>()
        var nextPosList = ObjectArrayList<IntVec2>()
        posList.add(startPos)
        var step = 1
        while (posList.isNotEmpty() && step <= maxStep) {
            for (i in 0..<posList.size) {
                val it = posList[i]
                for (dir in Direction4.entries) {
                    val newPos = it + dir
                    if (!grid.checkBounds(newPos)) continue
                    if (grid[newPos] != -1) continue
                    grid[newPos] = step
                    nextPosList.add(newPos)
                }
            }
            posList.clear()
            val tmp = posList
            posList = nextPosList
            nextPosList = tmp
            step++
        }
    }

    val fromStart = origGrid.copy()
    bfs(fromStart, inputStart)

    val fromEnd = origGrid.copy()
    bfs(fromEnd, inputEnd)

    fun solution(range: Int): Sequence<Int> {
        return input.xy()
            .filter { input[it] != '#' }
            .flatMap { startPos ->
                sequence {
                    for (dx in -range..range) {
                        for (dy in -range..range) {
                            if (abs(dx) + abs(dy) != range) continue
                            val endPos = startPos + IntVec2(dx, dy)
                            if (!input.checkBounds(endPos)) continue
                            if (input[endPos] == '#') continue
                            yield(startPos to endPos)
                        }
                    }
                }
            }
            .map {
                fromStart[it.first] + fromEnd[it.second] + range
            }
            .map {
                fromStart[inputEnd] - it
            }
            .filter { it > 0 }
    }

    val part1 = solution(2).count { it >= 100 }

    println("Part 1:")
    println(part1)

    val part2 = (2..20).sumOf {
        solution(it).count { it >= 100 }.toLong()
    }

    println("Part 2:")
    println(part2)
}