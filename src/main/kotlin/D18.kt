fun main() {
    run {
        val input = readInput("D18.txt").lineSequence()
            .map { it.split(',') }
            .map { IntVec2(it[0].toInt(), it[1].toInt()) }
            .toList()

        val maxI = 70
        val grid = IntMatrix(maxI + 1, maxI + 1)
        grid.data.fill(-1)

        input.take(1024)
            .forEach { grid[it] = -2 }

        val start = IntVec2(0, 0)
        val end = IntVec2(maxI, maxI)

        grid[start] = 0

        for (step in 0..Int.MAX_VALUE) {
            val posList = grid.withXY()
                .filter { it.value == step }
                .map { it.xy }
                .toList()

            if (posList.isEmpty()) {
                break
            }

            posList.forEach {
                for (dir in Direction4.entries) {
                    val newPos = it + dir
                    if (!grid.checkBounds(newPos)) continue
                    if (grid[newPos] != -1) continue
                    grid[newPos] = step + 1
                }
            }
        }

        println("Part 1:")
        println(grid[end])
    }

    run {
        val input = readInput("D18.txt").lineSequence()
            .map { it.split(',') }
            .map { IntVec2(it[0].toInt(), it[1].toInt()) }
            .toList()

        val maxI = 70
        val grid = IntMatrix(maxI + 1, maxI + 1)
        for (nBytes in 1024..<input.size) {
            grid.data.fill(-1)

            val nBytesList = input.take(nBytes)
            nBytesList
                .forEach { grid[it] = -2 }

            val start = IntVec2(0, 0)
            val end = IntVec2(maxI, maxI)

            grid[start] = 0

            for (step in 0..Int.MAX_VALUE) {
                val posList = grid.withXY()
                    .filter { it.value == step }
                    .map { it.xy }
                    .toList()

                if (posList.isEmpty()) {
                    break
                }

                posList.forEach {
                    for (dir in Direction4.entries) {
                        val newPos = it + dir
                        if (!grid.checkBounds(newPos)) continue
                        if (grid[newPos] != -1) continue
                        grid[newPos] = step + 1
                    }
                }
            }

            if (grid[end] == -1) {
                println("Part 2:")
                val blockedByte = nBytesList.last()
                println("${blockedByte.x},${blockedByte.y}")
                break
            }
        }
    }
}