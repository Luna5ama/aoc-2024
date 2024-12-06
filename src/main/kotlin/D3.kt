fun main() {
    val mulRegex = """(mul)\((\d+),((\d)+)\)""".toRegex()
    val doRegex = """(do)\(\)""".toRegex()
    val dontRegex = """(don't)\(\)""".toRegex()
    run {
        val input = readInput("D3.txt")
        mulRegex.find(input, 0)
        val sum = mulRegex.findAll(input).sumOf {
            it.groupValues[2].toInt() * it.groupValues[3].toInt()
        }
        println("Part 1:")
        println(sum)
    }

    run {
        val input = readInput("D3.txt")
        val allMatches = mutableListOf<MatchResult>()
        allMatches.addAll(mulRegex.findAll(input))
        allMatches.addAll(doRegex.findAll(input))
        allMatches.addAll(dontRegex.findAll(input))
        allMatches.sortBy { it.range.first }

        var enabled = true
        val sum = allMatches.sumOf {
            when (it.groupValues[1]) {
                "mul" -> {
                    if (enabled) {
                        it.groupValues[2].toInt() * it.groupValues[3].toInt()
                    } else {
                        0
                    }
                }
                "do" -> {
                    enabled = true
                    0
                }
                "don't" -> {
                    enabled = false
                    0
                }
                else -> 0
            }
        }

        println("Part 2:")
        println(sum)
    }
}