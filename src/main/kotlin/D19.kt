import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap

fun main() {
    run {
        val input = readInput("D19.txt").lines()
        val patterns = input.first().split(", ")
        val cache = Object2IntOpenHashMap<String>()
        cache.defaultReturnValue(-1)

        fun match(str: String, pattern: String): Boolean {
            if (str.isEmpty()) return true
            if (!str.startsWith(pattern)) return false

            val remaining = str.substring(pattern.length)
            if (remaining.isEmpty()) return true

            return cache.getOrPut(remaining) {
                if (patterns.any { match(remaining, it) }) 1 else 0
            } == 1
        }

        fun match(str: String): Boolean {
            return cache.getOrPut(str) {
                if (patterns.any { match(str, it) }) 1 else 0
            } == 1
        }

        val targets = input.drop(2)
        val count = targets.count { match(it) }

        println("Part 1:")
        println(count)
    }

    run {
        val input = readInput("D19.txt").lines()
        val patterns = input.first().split(", ")
        val cache = Object2LongOpenHashMap<String>()
        cache.defaultReturnValue(-1)

        fun match(str: String, pattern: String): Long {
            if (str.isEmpty()) return 1
            if (!str.startsWith(pattern)) return 0

            val remaining = str.substring(pattern.length)
            if (remaining.isEmpty()) return 1

            return cache.getOrPut(remaining) {
                patterns.sumOf { match(remaining, it) }
            }
        }

        fun match(str: String): Long {
            return cache.getOrPut(str) {
                patterns.sumOf { match(str, it) }
            }
        }

        val targets = input.drop(2)
        val count = targets.sumOf { match(it).toLong() }

        println("Part 2:")
        println(count)
    }
}