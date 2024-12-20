import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import it.unimi.dsi.fastutil.objects.ObjectHeapPriorityQueue
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet

fun main() {
    run {
        data class Node(val pos: IntVec2, val dir: Direction4, val totalWeight: Long) : Comparable<Node> {
            override fun compareTo(other: Node): Int {
                return totalWeight.compareTo(other.totalWeight)
            }
        }

        val input = readInput("D16.txt").toCharMatrix()
        val startNode = input.withXY()
            .find { it.value == 'S' }!!
            .let { Node(it.xy, Direction4.RIGHT, 0) }

        val queue = ObjectHeapPriorityQueue<Node>()
        val explored = ObjectOpenHashSet<IntVec2>()
        queue.enqueue(startNode)

        while (!queue.isEmpty) {
            val node = queue.dequeue()
            if (!explored.add(node.pos)) continue
            val c = input[node.pos]
            if (c == 'E') {
                println("Part 1:")
                println(node.totalWeight)
                break
            } else {
                fun checkAndEnqueue(node: Node) {
                    if (input[node.pos] == '#') return
                    queue.enqueue(node)
                }
                checkAndEnqueue(Node(node.pos + node.dir, node.dir, node.totalWeight + 1))
                checkAndEnqueue(Node(node.pos + node.dir.left, node.dir.left, node.totalWeight + 1001))
                checkAndEnqueue(Node(node.pos + node.dir.right, node.dir.right, node.totalWeight + 1001))
                checkAndEnqueue(Node(node.pos + node.dir.back, node.dir.back, node.totalWeight + 2001))
            }
        }
    }

    run {
        class Node(val pos: IntVec2, val dir: Direction4, val totalWeight: Long, val prev: Node?) :
            Comparable<Node> {
            override fun compareTo(other: Node): Int {
                return totalWeight.compareTo(other.totalWeight)
            }
        }

        val input = readInput("D16.txt").toCharMatrix()
        val (startPos, startNode) = input.withXY()
            .find { it.value == 'S' }!!
            .let { it.xy to Node(it.xy, Direction4.RIGHT, 0, null) }

        val endPos = input.withXY()
            .find { it.value == 'E' }!!
            .xy

        fun find(
            startNode: Node,
        ): Object2ObjectOpenHashMap<IntVec2, Node> {
            val explored = Object2ObjectOpenHashMap<IntVec2, Node>()
            val queue = ObjectHeapPriorityQueue<Node>()
            queue.enqueue(startNode)

            while (!queue.isEmpty) {
                val node = queue.dequeue()
                if (explored.putIfAbsent(node.pos, node) != null) continue

                fun checkAndEnqueue(node: Node) {
                    if (input[node.pos] == '#') return
                    queue.enqueue(node)
                }

                checkAndEnqueue(Node(node.pos + node.dir, node.dir, node.totalWeight + 1, node))
                checkAndEnqueue(Node(node.pos + node.dir.left, node.dir.left, node.totalWeight + 1001, node))
                checkAndEnqueue(Node(node.pos + node.dir.right, node.dir.right, node.totalWeight + 1001, node))
            }

            return explored
        }

        val passedTiles = ObjectOpenHashSet<IntVec2>()

        fun addPassedTile(endNode: Node) {
            var nodeIter: Node? = endNode
            while (nodeIter != null) {
                passedTiles.add(nodeIter.pos)
                nodeIter = nodeIter.prev
            }
        }

        val fromStart = find(startNode)
        val firstPath = fromStart[endPos]!!
        addPassedTile(firstPath)

        fromStart.values.parallelStream()
            .map { find(it)[endPos] }
            .toList()
            .asSequence()
            .mapNotNull { it }
            .filter { it.totalWeight == firstPath.totalWeight }
            .forEach(::addPassedTile)

        println("Part 2:")
        println(passedTiles.size)
    }
}