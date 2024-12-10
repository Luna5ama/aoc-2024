import it.unimi.dsi.fastutil.objects.ObjectArrayFIFOQueue

fun main() {
    run {
        val input = readInput("D10.txt").toCharMatrix()
        val intMat = IntMatrix(input.rows, input.cols) { x, y -> input[x, y].digitToInt() }
        val sum = intMat.withXY()
            .filter { it.value == 0 }
            .sumOf {
                val mat = intMat.copy()
                val queue = ObjectArrayFIFOQueue<IntVec2>()
                queue.enqueue(it.xy)
                var score = 0
                while (!queue.isEmpty) {
                    val curPos = queue.dequeue()
                    val curVal = mat[curPos]
                    mat[curPos] = -999

                    if (curVal == 9) {
                        score++
                        continue
                    }
                    for (dir in Direction4.entries) {
                        val next = curPos + dir.vec
                        if (mat.checkBounds(next) && mat[next] == curVal + 1) {
                            queue.enqueue(next)
                        }
                    }
                }
                score
            }

        println("Part 1:")
        println(sum)
    }

    run {
        val input = readInput("D10.txt").toCharMatrix()
        val intMat = IntMatrix(input.rows, input.cols) { x, y -> input[x, y].digitToInt() }
        val sum = intMat.withXY()
            .filter { it.value == 0 }
            .sumOf {
                val mat = intMat.copy()
                val queue = ObjectArrayFIFOQueue<IntVec2>()
                queue.enqueue(it.xy)
                var score = 0
                while (!queue.isEmpty) {
                    val curPos = queue.dequeue()
                    val curVal = mat[curPos]

                    if (curVal == 9) {
                        score++
                        continue
                    }
                    for (dir in Direction4.entries) {
                        val next = curPos + dir.vec
                        if (mat.checkBounds(next) && mat[next] == curVal + 1) {
                            queue.enqueue(next)
                        }
                    }
                }
                score
            }

        println("Part 1:")
        println(sum)
    }
}