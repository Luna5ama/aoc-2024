fun main() {
    val input = readInput("D8.txt").toCharMatrix()
    val antennas = input.withXY()
        .filter { it.value != '.' }
        .groupBy { it.value }
        .mapValues { entry -> entry.value.map { it.xy } }

    run {
        val antinodes = IntMatrix(input.rows, input.cols)

        antennas.values.forEach { list ->
            list.cartesianProduct(2)
                .forEach inner@{ (a, b) ->
                    if (b == a) return@inner
                    val diff = b - a
                    val c = b + diff
                    if (input.checkBounds(c)) {
                        antinodes[c] = 1
                    }
                }
        }

        val count = antinodes.flatten().sum()
        println("Part 1:")
        println(count)
    }

    run {
        val antinodes = IntMatrix(input.rows, input.cols)

        antennas.values.forEach { list ->
            list.cartesianProduct(2)
                .forEach inner@{ (a, b) ->
                    if (b == a) return@inner
                    val diff = b - a
                    var c = b
                    while (input.checkBounds(c)) {
                        antinodes[c] = 1
                        c += diff
                    }
                }
        }

        val count = antinodes.flatten().sum()
        println("Part 2:")
        println(count)
    }
}