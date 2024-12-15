fun main() {
    run {
        val input = readInput("D15.txt").lines()
        val emptyLine = input.indexOf("")
        val mat = input.subList(0, emptyLine).toCharMatrix()
        val moves = input.subList(emptyLine + 1, input.size).joinToString("")
        var robotPos = mat.withXY()
            .find { it.value == '@' }!!
            .xy
        moves.asSequence()
            .map {
                when (it) {
                    '<' -> Direction4.LEFT
                    '>' -> Direction4.RIGHT
                    '^' -> Direction4.UP
                    'v' -> Direction4.DOWN
                    else -> error("Unknown direction: $it")
                }
            }.forEach { moveDir ->
                var checkPos = robotPos + moveDir
                var pushedCount = 0
                while (mat.checkBounds(checkPos)) {
                    when (mat[checkPos]) {
                        '.' -> break
                        'O' -> pushedCount++
                        '#' -> return@forEach
                    }
                    checkPos += moveDir
                }

                mat[robotPos] = '.'
                robotPos += moveDir
                mat[robotPos] = '@'
                var writePos = robotPos + moveDir
                repeat(pushedCount) {
                    mat[writePos] = 'O'
                    writePos += moveDir
                }
            }

        val sum = mat.withXY()
            .sumOf { (xy, value) ->
                if (value != 'O') return@sumOf 0

                xy.x + 100 * xy.y
            }

        println("Part 1:")
        println(sum)
    }

    run {
        val input = readInput("D15.txt").lines()
        val emptyLine = input.indexOf("")
        val mat0 = input.subList(0, emptyLine).toCharMatrix()
        val moves = input.subList(emptyLine + 1, input.size).joinToString("")

        val mat = CharMatrix(mat0.rows, mat0.cols * 2)

        for (y in mat0.yRange) {
            for (x in mat0.xRange) {
                val x1 = x * 2
                when (mat0[x, y]) {
                    '#' -> {
                        mat[x1, y] = '#'
                        mat[x1 + 1, y] = '#'
                    }
                    'O' -> {
                        mat[x1, y] = '['
                        mat[x1 + 1, y] = ']'
                    }
                    '.' -> {
                        mat[x1, y] = '.'
                        mat[x1 + 1, y] = '.'
                    }
                    '@' -> {
                        mat[x1, y] = '@'
                        mat[x1 + 1, y] = '.'
                    }
                }
            }
        }

        var robotPos = mat.withXY()
            .find { it.value == '@' }!!
            .xy

        fun tryMove(xy: IntVec2, moveDir: Direction4): Boolean {
            return when (mat[xy]) {
                '#' -> false
                '.' -> true
                '@' -> {
                    tryMove(xy + moveDir, moveDir)
                }
                '[' -> {
                    when (moveDir) {
                        Direction4.LEFT -> tryMove(xy + moveDir, moveDir)
                        Direction4.RIGHT -> tryMove(xy + moveDir + moveDir, moveDir)
                        else -> tryMove(xy + moveDir, moveDir) && tryMove(xy + moveDir + Direction4.RIGHT, moveDir)
                    }
                }
                ']' -> {
                    tryMove(xy + Direction4.LEFT, moveDir)
                }
                else -> {
                    error("Unknown tile: ${mat[xy]}")
                }
            }
        }

        fun move(xy: IntVec2, moveDir: Direction4) {
            when (mat[xy]) {
                '#', '.' -> {}
                '@' -> {
                    move(xy + moveDir, moveDir)

                    mat[xy] = '.'
                    mat[xy + moveDir] = '@'
                }
                '[' -> {
                    when (moveDir) {
                        Direction4.LEFT -> move(xy + moveDir, moveDir)
                        Direction4.RIGHT -> move(xy + moveDir + moveDir, moveDir)
                        else -> {
                            move(xy + moveDir, moveDir)
                            move(xy + moveDir + Direction4.RIGHT, moveDir)
                        }
                    }

                    mat[xy] = '.'
                    mat[xy + Direction4.RIGHT] = '.'
                    mat[xy + moveDir] = '['
                    mat[xy + Direction4.RIGHT + moveDir] = ']'
                }
                ']' -> {
                    move(xy + Direction4.LEFT, moveDir)
                }
                else -> {
                    error("Unknown tile: ${mat[xy]}")
                }
            }
        }

//        mat.print()

        moves.asSequence()
            .map {
                when (it) {
                    '<' -> Direction4.LEFT
                    '>' -> Direction4.RIGHT
                    '^' -> Direction4.UP
                    'v' -> Direction4.DOWN
                    else -> error("Unknown direction: $it")
                }
            }.forEachIndexed { i, moveDir ->
                if (tryMove(robotPos, moveDir)) {
                    move(robotPos, moveDir)
                    robotPos += moveDir
                }
//                println("${i + 1}: $moveDir")
//                mat.print()
//                println()
            }
        mat.print()

        val sum = mat.withXY()
            .sumOf { (xy, value) ->
                if (value != '[') return@sumOf 0

                xy.x + 100 * xy.y
            }

        println("Part 1:")
        println(sum)
    }
}