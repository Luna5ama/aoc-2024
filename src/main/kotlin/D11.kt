import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap

fun main() {
    fun iteration(map: Long2LongOpenHashMap): Long2LongOpenHashMap {
        val newMap = Long2LongOpenHashMap()
        newMap.defaultReturnValue(0L)
        map.long2LongEntrySet().fastForEach {
            val stone = it.longKey
            val digStr = stone.toString()
            when {
                stone == 0L -> {
                    newMap.addTo(1L, it.longValue)
                }
                (digStr.length and 1) == 0 -> {
                    newMap.addTo(digStr.substring(0, digStr.length / 2).toLong(), it.longValue)
                    newMap.addTo(digStr.substring(digStr.length / 2).toLong(), it.longValue)
                }
                else -> {
                    newMap.addTo(stone * 2024, it.longValue)
                }
            }
        }
        return newMap
    }

    fun doStuff(iterations: Int): Long {
        return readInput("D11.txt")
            .splitToSequence(' ')
            .map { it.toLong() }
            .sumOf {
                var stones = Long2LongOpenHashMap()
                stones.defaultReturnValue(0L)
                stones.put(it, 1L)

                repeat(iterations) {
                    stones = iteration(stones)
                }

                stones.values.sum()
            }

    }

    run {
        println("Part 1:")
        println(doStuff(25))
    }

    run {
        println("Part 2:")
        println(doStuff(75))
    }
}