fun main() {
    val input = readInput("D6.txt")

    run {
        val mat = input.toCharMatrix()
        var cx = -1
        var cy = -1
        var dir = Direction4.UP

        outer@ for (y in mat.yRange) {
            for (x in mat.xRange) {
                if (mat[x, y] == '^') {
                    cy = y
                    cx = x
                    dir = Direction4.UP
                    break@outer
                }
            }
        }

        var count = 0

        while (true) {
            if (mat[cx, cy] != 'X') {
                mat[cx, cy] = 'X'
                count++
            }

            val nx = cx + dir.dx
            val ny = cy + dir.dy

            if (!mat.checkBounds(nx, ny)) {
                break
            }

            if (mat[nx, ny] == '#') {
                dir = dir.right
                continue
            }

            cx = nx
            cy = ny
        }

        println("Part 1:")
        println(count)
    }

    run {
        val matOrig = input.toCharMatrix()
        var sx = -1
        var sy = -1
        var sDir = Direction4.UP

        outer@ for (y in matOrig.yRange) {
            for (x in matOrig.xRange) {
                if (matOrig[x, y] == '^') {
                    sy = y
                    sx = x
                    sDir = Direction4.UP
                    break@outer
                }
            }
        }

        var count = 0

        for (y in matOrig.yRange) {
            for (x in matOrig.xRange) {
                val c = matOrig[x, y]
                if (c == '#' || c == '^') {
                    continue
                }

                val mat = matOrig.copy()
                val loopBit = IntMatrix(matOrig.rows, matOrig.cols)
                mat[x, y] = '#'

                var cx = sx
                var cy = sy
                var dir = sDir

                while (true) {
                    val nx = cx + dir.dx
                    val ny = cy + dir.dy

                    if (!mat.checkBounds(nx, ny)) {
                        break
                    }

                    val nc = mat[nx, ny]

                    if (nc == '#') {
                        val prevLoopBit = loopBit[nx, ny]
                        if (prevLoopBit and dir.bitMask != 0) {
                            count++
                            break
                        }

                        loopBit[nx, ny] = prevLoopBit or dir.bitMask
                        dir = dir.right
                        continue
                    }

                    cx = nx
                    cy = ny
                }
            }
        }

        println("Part 2:")
        println(count)
    }

}