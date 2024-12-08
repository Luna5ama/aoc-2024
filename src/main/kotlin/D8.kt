fun main() {
    run {
        val input = readInput("D8.txt").toCharMatrix()
        val antennas = mutableMapOf<Char, MutableList<IntVec2>>()
        for (y in input.yRange) {
            for (x in input.xRange) {
                val c = input[x, y]
                if (c != '.') {
                    antennas.computeIfAbsent(c) { mutableListOf() }.add(IntVec2(x, y))
                }
            }
        }

        val antinodes = IntMatrix(input.rows, input.cols)

        antennas.forEach { freq, list ->
            list.forEach { a ->
                 list.forEach loopB@ { b ->
                    if (b == a) return@loopB
                    val diff = b - a
                    val c = b + diff
                    if (input.checkBounds(c)) {
                        antinodes[c] = 1
                    }
                }
            }
        }

        val count = antinodes.flatten().sum()
        println("Part 1:")
        println(count)
    }

    run {
        val input = readInput("D8.txt").toCharMatrix()
        val antennas = mutableMapOf<Char, MutableList<IntVec2>>()
        for (y in input.yRange) {
            for (x in input.xRange) {
                val c = input[x, y]
                if (c != '.') {
                    antennas.computeIfAbsent(c) { mutableListOf() }.add(IntVec2(x, y))
                }
            }
        }

        val antinodes = IntMatrix(input.rows, input.cols)

        antennas.forEach { freq, list ->
            list.forEach { a ->
                list.forEach loopB@ { b ->
                    if (b == a) return@loopB
                    val diff = b - a
                    var c = b
                    while (input.checkBounds(c)) {
                        antinodes[c] = 1
                        c += diff
                    }
                }
            }
        }

        val count = antinodes.flatten().sum()
        println("Part 2:")
        println(count)
    }
}