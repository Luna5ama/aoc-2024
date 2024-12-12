import it.unimi.dsi.fastutil.objects.ObjectArrayFIFOQueue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.max

fun main() {
    run {
        val input = readInput("D12.txt").toCharMatrix()
        var result = 0
        for ((startXY, startC) in input.withXY()) {
            if (startC == '.') continue

            val queue = ObjectArrayFIFOQueue<IntVec2>()
            queue.enqueue(startXY)
            var area = 0
            val auxGrid = IntMatrix(input.rows + 2, input.cols + 2)

            while (!queue.isEmpty) {
                val curPos = queue.dequeue()
                if (input.checkBounds(curPos) && input[curPos] == startC) {
                    input[curPos] = '.'
                    area++
                    auxGrid[curPos + 1] = -1
                } else {
                    if (auxGrid[curPos + 1] != -1) {
                        auxGrid[curPos + 1]++
                    }
                    continue
                }

                for (dir in Direction4.entries) {
                    val next = curPos + dir.vec
                    if (!auxGrid.checkBounds(next + 1)) continue
                    queue.enqueue(next)
                }
            }

            val perimeter = auxGrid.data.sumOf { max(it, 0) }

            result += area * perimeter
        }

        println("Part 1:")
        println(result)
    }

    runBlocking {
        val input = readInput("D12.txt").toCharMatrix()
        val result = AtomicInteger()
        coroutineScope {
            for ((startXY, startC) in input.withXY()) {
                if (startC == '.') continue

                val queue = ObjectArrayFIFOQueue<IntVec2>()
                queue.enqueue(startXY)
                var area = 0
                val auxGridA = IntMatrix(input.rows + 2, input.cols + 2)

                while (!queue.isEmpty) {
                    val curPos = queue.dequeue()
                    if (input.checkBounds(curPos) && input[curPos] == startC) {
                        input[curPos] = '.'
                        area++
                        auxGridA[curPos + 1] = 1
                    } else {
                        continue
                    }

                    for (dir in Direction4.entries) {
                        val next = curPos + dir.vec
                        if (!auxGridA.checkBounds(next + 1)) continue
                        queue.enqueue(next)
                    }
                }

                launch(Dispatchers.Default) {
                    var sides = 0
                    val auxGridB = auxGridA.scale(4)

                    for ((bxy, n) in auxGridB.withXY()) {
                        if (n == 1) continue
                        outer@ for (dy in -1..1) {
                            for (dx in -1..1) {
                                if (dy == 0 && dx == 0) continue
                                val next = bxy + IntVec2(dx, dy)
                                if (auxGridB.checkBounds(next) && auxGridB[next] == 1) {
                                    auxGridB[bxy] = 9
                                    break@outer
                                }
                            }
                        }
                    }

                    for ((bxy, n) in auxGridB.withXY()) {
                        if (n != 9) continue

                        var curr = bxy
                        val initDir = Direction4.entries.find {
                            val next = curr + it.vec
                            auxGridB.checkBounds(next) && auxGridB[next] == 9
                        }

                        if (initDir == null) {
                            continue
                        }

                        var dir = initDir!!

                        while (true) {
                            val next = curr + dir.vec
                            if (auxGridB.checkBounds(next) && auxGridB[next] == 9) {
                                auxGridB[next] = 0
                                curr = next
                                continue
                            }

                            val newDir = Direction4.entries.find {
                                val tryNext = curr + it.vec
                                auxGridB.checkBounds(tryNext) && auxGridB[tryNext] == 9
                            }

                            sides += 1

                            if (newDir == null) {
                                break
                            }

                            dir = newDir
                        }
                    }

                    result.addAndGet(area * sides)
                }
            }
        }

        println("Part 2:")
        println(result)
    }
}