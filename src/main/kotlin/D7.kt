import java.math.BigInteger

fun main() {
    run {
        val input = readInput("D7.txt").lineSequence()
            .map { line -> line.splitToSequence(": ", " ").map { it.toBigInteger() }.toList() }

        fun test(list: List<BigInteger>): Int {
            val operatorCount = list.size - 2
            val operator = IntArray(operatorCount) { 0 }

            fun next(): Boolean {
                var i = operatorCount - 1
                while (i >= 0 && operator[i] == 1) {
                    operator[i] = 0
                    i--
                }
                if (i < 0) {
                    return false
                }
                operator[i] = 1
                return true
            }

            var validCount = 0

            while (true) {
                var result = list[1]
                for (i in 0 until operatorCount) {
                    result = when (operator[i]) {
                        0 -> result + list[i + 2]
                        1 -> result * list[i + 2]
                        else -> error("Invalid operator")
                    }
                }
                if (result == list[0]) {
                    validCount++
                }
                if (!next()) {
                    return validCount
                }
            }
        }

        val count = input.sumOf {
            if (test(it) > 0) {
                it[0]
            } else {
                BigInteger.ZERO
            }
        }
        println("Part 1:")
        println(count)
    }

    run {
        val input = readInput("D7.txt").lineSequence()
            .map { line -> line.splitToSequence(": ", " ").map { it.toBigInteger() }.toList() }

        fun test(list: List<BigInteger>): Int {
            val operatorCount = list.size - 2
            val operator = IntArray(operatorCount) { 0 }

            fun next(): Boolean {
                var i = operatorCount - 1
                while (i >= 0 && operator[i] == 2) {
                    operator[i] = 0
                    i--
                }
                if (i < 0) {
                    return false
                }
                operator[i]++
                return true
            }

            var validCount = 0

            while (true) {
                var result = list[1]
                for (i in 0 until operatorCount) {
                    result = when (operator[i]) {
                        0 -> result + list[i + 2]
                        1 -> result * list[i + 2]
                        2 -> (result.toString() + list[i + 2].toString()).toBigInteger()
                        else -> error("Invalid operator")
                    }
                }
                if (result == list[0]) {
                    validCount++
                }
                if (!next()) {
                    return validCount
                }
            }
        }

        val count = input.sumOf {
            if (test(it) > 0) {
                it[0]
            } else {
                BigInteger.ZERO
            }
        }
        println("Part 2:")
        println(count)
    }

}