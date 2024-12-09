import it.unimi.dsi.fastutil.shorts.ShortArrayList
import java.util.*

fun main() {
    run {
        val input = readInput("D9.txt")
        val disk = ShortArrayList()
        var currentID = 0
        input.asSequence()
            .windowed(2, 2, true)
            .forEach {
                val f = it[0].digitToInt()
                val e = it.getOrElse(1) { '0' }.digitToInt()
                repeat(f) {
                    disk.add(currentID.toShort())
                }
                repeat(e) {
                    disk.add(-1)
                }
                currentID++
            }
        var left = disk.indexOf(-1)
        var right = disk.size - 1
        while (right > left) {
            if (disk.getShort(right).toInt() == -1) {
                right--
                continue
            }

            if (disk.getShort(left).toInt() != -1) {
                left++
                continue
            }

            disk.set(left, disk.getShort(right))
            disk.set(right, -1)
            left++
            right--
        }
        var checkSum = 0L
        for (i in 0..<disk.size) {
            val id = disk.getShort(i).toInt()
            if (id == -1) continue
            checkSum += i.toLong() * id.toLong()
        }
        println("Part 1:")
        println(checkSum)
    }

    data class Block(val index: Int, val size: Int)

    run {
        val input = readInput("D9.txt")
        val disk = ShortArrayList()
        val emptyBlocks = mutableListOf<Block>()
        val fileBlocks = LinkedList<Block>()
        var currentID = 0
        input.asSequence()
            .windowed(2, 2, true)
            .forEach {
                val f = it[0].digitToInt()
                val e = it.getOrElse(1) { '0' }.digitToInt()
                if (f > 0) {
                    fileBlocks.addFirst(Block(disk.size, f))
                }
                repeat(f) {
                    disk.add(currentID.toShort())
                }
                if (e > 0) {
                    emptyBlocks.add(Block(disk.size, e))
                }
                repeat(e) {
                    disk.add(-1)
                }
                currentID++
            }

        emptyBlocks.forEach { emptyBlock ->
            val iter = fileBlocks.iterator()
            var emptyStart = emptyBlock.index
            var emptySize = emptyBlock.size
            while (iter.hasNext()) {
                val fileBlock = iter.next()
                if (fileBlock.index + fileBlock.size <= emptyStart) {
                    break
                }
                if (fileBlock.size <= emptySize) {
                    val fileID = disk.getShort(fileBlock.index)
                    for (i in 0..<fileBlock.size) {
                        disk.set(emptyStart + i, fileID)
                        disk.set(fileBlock.index + i, -1)
                    }
                    iter.remove()
                    emptyStart += fileBlock.size
                    emptySize -= fileBlock.size
                }
            }
        }

        var checkSum = 0L
        for (i in 0..<disk.size) {
            val id = disk.getShort(i).toInt()
            if (id == -1) continue
            checkSum += i.toLong() * id.toLong()
        }
        println("Part 2:")
        println(checkSum)
    }
}