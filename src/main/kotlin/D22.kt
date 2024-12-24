fun main() {
    fun evolve(initial: Int, iterations: Int): Int {
        var value = initial
        repeat(iterations) {
            value = ((value shl 6) xor value) and 0xFFFFFF
            value = ((value shr 5) xor value) and 0xFFFFFF
            value = ((value shl 11) xor value) and 0xFFFFFF
        }
        return value
    }

    fun evolve(initial: Int): Int {
        var value = initial
        value = ((value shl 6) xor value) and 0xFFFFFF
        value = ((value shr 5) xor value) and 0xFFFFFF
        value = ((value shl 11) xor value) and 0xFFFFFF
        return value
    }

    run {
        val iterations = 2000
        val part1 = readInput("D22.txt").lines()
            .map { it.toInt() }
            .map { evolve(it, iterations) }
            .sumOf { it.toLong() }

        println("Part 1:")
        println(part1)
    }

    run {
        val iterations = 2000
        val prices = readInput("D22.txt")
            .lines()
            .parallelStream()
            .map { it.toInt() }
            .map {
                (1..iterations)
                    .runningFold(it) { acc, _ -> evolve(acc) }
                    .map { it % 10 }
            }
            .toList()
        val changes = prices
            .parallelStream()
            .map {
                it.windowed(2, 1)
                    .map { it[1] - it[0] }
            }
            .toList()
        val changeSubs = (prices zip changes)
            .parallelStream()
            .map { (p, c) ->
                c.withIndex()
                    .windowed(4, 1)
                    .asReversed()
                    .associate { it.map { it.value } to p[it.last().index + 1] }
            }
            .toList()

        val part2 = changeSubs.parallelStream()
            .flatMap { it.keys.stream() }
            .distinct()
            .mapToInt { changeSub ->
                changeSubs.sumOf {
                    it.getOrDefault(changeSub, 0)
                }
            }
            .max().asInt

        println("Part 2:")
        println(part2)
    }
}