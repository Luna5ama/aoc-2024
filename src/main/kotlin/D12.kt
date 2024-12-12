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
        val auxGrid = IntMatrix(input.rows + 2, input.cols + 2)
        var result = 0
        for (y in input.yRange) {
            for (x in input.xRange) {
                val startC = input[x, y]
                if (startC == '.') continue

                val queue = ObjectArrayFIFOQueue<IntVec2>()
                queue.enqueue(IntVec2(x, y))
                var area = 0

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

                val perimeter = auxGrid.flatten().sumOf { max(it, 0) }

                result += area * perimeter

                for (ay in auxGrid.yRange) {
                    for (ax in auxGrid.xRange) {
                        auxGrid[ax, ay] = 0
                    }
                }
            }
        }

        println("Part 1:")
        println(result)
    }

    runBlocking {
        val input = readInput("D12.txt").toCharMatrix()
        val result = AtomicInteger()
       coroutineScope {
            for (y in input.yRange) {
                for (x in input.xRange) {
                    val startC = input[x, y]
                    if (startC == '.') continue

                    val queue = ObjectArrayFIFOQueue<IntVec2>()
                    queue.enqueue(IntVec2(x, y))
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
                        val auxGridB = IntMatrix(auxGridA.rows * 4, auxGridA.cols * 4)

                        for (ay in auxGridA.yRange) {
                            for (ax in auxGridA.xRange) {
                                for (by in 0..3) {
                                    for (bx in 0..3) {
                                        auxGridB[ax * 4 + bx, ay * 4 + by] = auxGridA[ax, ay]
                                    }
                                }
                            }
                        }

                        for (by in auxGridB.yRange) {
                            for (bx in auxGridB.xRange) {
                                if (auxGridB[bx, by] == 1) continue
                                outer@ for (dy in -1..1) {
                                    for (dx in -1..1) {
                                        if (dy == 0 && dx == 0) continue
                                        val next = IntVec2(bx + dx, by + dy)
                                        if (auxGridB.checkBounds(next) && auxGridB[next] == 1) {
                                            auxGridB[bx, by] = 9
                                            break@outer
                                        }
                                    }
                                }
                            }
                        }

                        for (ay in auxGridB.yRange) {
                            for (ax in auxGridB.xRange) {
                                val n = auxGridB[ax, ay]
                                if (n != 9) continue

                                var curr = IntVec2(ax, ay)
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
                        }

                        result.addAndGet(area * sides)
                    }
                }
            }
        }

        println("Part 2:")
        println(result)
    }
}