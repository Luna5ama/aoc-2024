fun main() {
    val input = readInput("D4.txt").lines().filter { it.isNotBlank() }
    val rows = input.size
    val cols = input[0].length
    val xRange = 0..<cols
    val yRange = 0..<rows

    val searchWord = "XMAS"

    run {
        fun search(step: Pair<Int, Int>): Int {
            var count = 0
            for (y in yRange) {
                outer@ for (x in xRange) {
                    for ((i, c) in searchWord.withIndex()) {
                        val cy = y + i * step.first
                        val cx = x + i * step.second
                        if (cy !in yRange || cx !in xRange || input[cy][cx] != c) {
                            continue@outer
                        }
                    }
                    count++
                }
            }
            return count
        }

        var sum = 0
        sum += search(1 to 0)
        sum += search(-1 to 0)
        sum += search(0 to 1)
        sum += search(0 to -1)
        sum += search(-1 to -1)
        sum += search(-1 to 1)
        sum += search(1 to -1)
        sum += search(1 to 1)
        println("Part 1:")
        println(sum)
    }

    run {
        fun search(pattern: String): Int {
            val patternLines = pattern.lines()
            var count = 0
            for (y in yRange) {
                outer@ for (x in xRange) {
                    for (dy in -1..1) {
                        for (dx in -1..1) {
                            val c = patternLines[dy + 1][dx + 1]
                            val cy = y + dy
                            val cx = x + dx
                            if (cy !in yRange || cx !in xRange || (c != '*' && input[cy][cx] != c)) {
                                continue@outer
                            }
                        }
                    }
                    count++
                }
            }
            return count
        }

        var sum = 0
        sum += search(
            """
            M*S
            *A*
            M*S
            """.trimIndent()
        )
        sum += search(
            """
            S*M
            *A*
            S*M
            """.trimIndent()
        )
        sum += search(
            """
            M*M
            *A*
            S*S
            """.trimIndent()
        )
        sum += search(
            """
            S*S
            *A*
            M*M
            """.trimIndent()
        )
        println("Part 2:")
        println(sum)
    }
}