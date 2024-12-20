fun main() {
    class Computer(val reg: LongArray, val program: List<Int>) {
        var pc = 0
        val outputs = mutableListOf<Int>()
        fun step(instOpcode: Int, instOperand: Int) {
            val comboOperand = when (instOperand) {
                0, 1, 2, 3 -> instOperand.toLong()
                4, 5, 6 -> reg[instOperand - 4]
                else -> error("WTF")
            }
            when (instOpcode) {
                0 -> {
                    reg[0] = reg[0] / (1L shl comboOperand.toInt())
                    pc += 2
                }
                1 -> {
                    reg[1] = reg[1] xor instOperand.toLong()
                    pc += 2
                }
                2 -> {
                    reg[1] = comboOperand % 8L
                    pc += 2
                }
                3 -> {
                    if (reg[0] == 0L) {
                        pc += 2
                    } else {
                        pc = instOperand
                    }
                }
                4 -> {
                    reg[1] = reg[1] xor reg[2]
                    pc += 2
                }
                5 -> {
                    outputs += (comboOperand % 8L).toInt()
                    pc += 2
                }
                6 -> {
                    reg[1] = reg[0] / (1L shl comboOperand.toInt())
                    pc += 2
                }
                7 -> {
                    reg[2] = reg[0] / (1L shl comboOperand.toInt())
                    pc += 2
                }
            }
        }

        fun run() {
            while (pc + 1 < program.size) {
                step(program[pc], program[pc + 1])
                let {}
            }
        }
    }

    run {
        val input = readInput("D17.txt")
        val stuff = input.lineSequence()
            .filter { it.isNotBlank() }
            .map { it.split(": ") }
            .toList()
        val reg = stuff.take(3)
            .map { it[1].toLong() }
            .toLongArray()
        val program = stuff[3][1].split(',').map { it.toInt() }
        val computer = Computer(reg, program)
        computer.run()

        println("Part 1:")
        println(computer.outputs.joinToString(","))
    }

    run {
        val input = readInput("D17.txt")
        val stuff = input.lineSequence()
            .filter { it.isNotBlank() }
            .map { it.split(": ") }
            .toList()
        val reg = stuff.take(3)
            .map { it[1].toLong() }
            .toLongArray()
        val program = stuff[3][1].split(',').map { it.toInt() }

        fun LongRange.comp(n: Int, lastBits: Int, last: List<Long>) =
            this.asSequence()
                .flatMap {
                    last.asSequence().map { l -> (it shl lastBits) or l }
                }
                .map {
                    val computer = Computer(reg.copyOf(), program)
                    computer.reg[0] = it
                    computer.run()
                    it to computer.outputs
                }
                .filter { it.second.size >= n }
                .filter {
                    for (i in 0..<n) {
                        if (it.second[i] != program[i]) {
                            return@filter false
                        }
                    }
                    return@filter true
                }
                .map { it.first }
                .toList()


        val part2 = (0..16).asSequence()
            .fold((0L..1023L).comp(1, 0, listOf(0))) { acc, n ->
                (0L..7L).comp(n, 10 + n * 3, acc)
            }


        println("Part 2:")
        println(part2.size)
        println(part2.first())
    }
}