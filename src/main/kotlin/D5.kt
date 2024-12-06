import it.unimi.dsi.fastutil.ints.IntComparator

fun main() {
    val inputLines = readInput("D5.txt").lines()
    val splitIndex = inputLines.indexOf("")

    val ruleLines = inputLines.subList(0, splitIndex)
    val updateLines = inputLines.subList(splitIndex + 1, inputLines.size)

    run {
        val cantBeBefore = mutableMapOf<Int, MutableSet<Int>>()
        fun addRule(first: Int, second: Int) {
            cantBeBefore.computeIfAbsent(first) { mutableSetOf() }.add(second)
        }
        ruleLines.forEach { ruleLine ->
            val (first, second) = ruleLine.split("|")
            addRule(first.toInt(), second.toInt())
        }
        val sum = updateLines.sumOf { updateLine ->
            val updates = updateLine.splitToSequence(',')
                .map { it.toInt() }
                .toList()

            val cumulative = mutableSetOf(updates[0])

            updates.drop(1).forEach { update ->
                val rules = cantBeBefore[update]
                if (rules != null && cumulative.any { it in rules }) {
                    return@sumOf 0
                }
                cumulative.add(update)
            }

            updates[updates.size / 2]
        }
        println("Part 1:")
        println(sum)
    }

    run {
        class RuleEntry(val me: Int) {
            val beforeMe = mutableSetOf<Int>()
            val afterMe = mutableSetOf<Int>()
        }

        val rules = mutableMapOf<Int, RuleEntry>()
        fun addRule(first: Int, second: Int) {
            rules.computeIfAbsent(first, ::RuleEntry).afterMe.add(second)
            rules.computeIfAbsent(second, ::RuleEntry).beforeMe.add(first)
        }

        ruleLines.forEach { ruleLine ->
            val (first, second) = ruleLine.split("|")
            addRule(first.toInt(), second.toInt())
        }

        val comparator = IntComparator { a, b ->
            val ruleB = rules[b]
            if (ruleB != null) {
                if (a in ruleB.beforeMe) {
                    return@IntComparator -1
                } else if (a in ruleB.afterMe) {
                    return@IntComparator 1
                }
            }

            return@IntComparator 0
        }

        val sum = updateLines.sumOf { updateLine ->
            val updates = updateLine.splitToSequence(',')
                .map { it.toInt() }
                .toList()

            val updatesSorted = updates.sortedWith(comparator)

            if (updatesSorted != updates) {
                updatesSorted[updates.size / 2]
            } else {
                0
            }
        }
        println("Part 2:")
        println(sum)
    }
}